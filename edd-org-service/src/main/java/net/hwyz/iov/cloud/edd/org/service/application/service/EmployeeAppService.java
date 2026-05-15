package net.hwyz.iov.cloud.edd.org.service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.service.application.assembler.EmployeeAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.query.EmployeeQuery;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.EmployeeDto;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Employee;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.EmployeeRepository;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DepartmentMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.PositionMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DepartmentPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.PositionPo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeAppService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeAssembler employeeAssembler;
    private final DepartmentMapper departmentMapper;
    private final PositionMapper positionMapper;

    @Transactional
    public EmployeeDto createEmployee(CreateEmployeeCmd cmd) {
        log.info("创建员工: {}", cmd.getCode());

        if (employeeRepository.existsByCode(cmd.getCode(), null)) {
            throw new BusinessException("员工工号已存在: " + cmd.getCode());
        }

        Employee employee = employeeAssembler.toDomain(cmd);
        employeeRepository.save(employee);

        if (cmd.getDepartmentIds() != null && !cmd.getDepartmentIds().isEmpty()) {
            employeeRepository.saveDepartments(employee.getId(), cmd.getDepartmentIds());
        }
        if (cmd.getPositionIds() != null && !cmd.getPositionIds().isEmpty()) {
            employeeRepository.savePositions(employee.getId(), cmd.getPositionIds());
        }

        EmployeeDto dto = employeeAssembler.toDto(employee);
        dto.setDepartmentIds(cmd.getDepartmentIds());
        dto.setPositionIds(cmd.getPositionIds());
        setDepartmentAndPositionNames(dto);
        return dto;
    }

    @Transactional
    public EmployeeDto updateEmployee(UpdateEmployeeCmd cmd) {
        log.info("更新员工: {}", cmd.getId());

        Employee employee = employeeRepository.findById(cmd.getId())
            .orElseThrow(() -> new BusinessException("员工不存在: " + cmd.getId()));

        if (employeeRepository.existsByCode(cmd.getCode(), cmd.getId())) {
            throw new BusinessException("员工工号已存在: " + cmd.getCode());
        }

        employeeAssembler.updateDomain(cmd, employee);
        employeeRepository.save(employee);

        employeeRepository.deleteDepartments(employee.getId());
        employeeRepository.deletePositions(employee.getId());
        if (cmd.getDepartmentIds() != null && !cmd.getDepartmentIds().isEmpty()) {
            employeeRepository.saveDepartments(employee.getId(), cmd.getDepartmentIds());
        }
        if (cmd.getPositionIds() != null && !cmd.getPositionIds().isEmpty()) {
            employeeRepository.savePositions(employee.getId(), cmd.getPositionIds());
        }

        EmployeeDto dto = employeeAssembler.toDto(employee);
        dto.setDepartmentIds(cmd.getDepartmentIds());
        dto.setPositionIds(cmd.getPositionIds());
        setDepartmentAndPositionNames(dto);
        return dto;
    }

    public EmployeeDto getEmployeeById(Long id) {
        log.info("查询员工: {}", id);
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new BusinessException("员工不存在: " + id));
        EmployeeDto dto = employeeAssembler.toDto(employee);
        dto.setDepartmentIds(employeeRepository.findDepartmentIds(id));
        dto.setPositionIds(employeeRepository.findPositionIds(id));
        setDepartmentAndPositionNames(dto);
        return dto;
    }

    public EmployeeDto getEmployeeByCode(String code) {
        log.info("查询员工: {}", code);
        Employee employee = employeeRepository.findByCode(code)
            .orElseThrow(() -> new BusinessException("员工不存在: " + code));
        EmployeeDto dto = employeeAssembler.toDto(employee);
        dto.setDepartmentIds(employeeRepository.findDepartmentIds(employee.getId()));
        dto.setPositionIds(employeeRepository.findPositionIds(employee.getId()));
        setDepartmentAndPositionNames(dto);
        return dto;
    }

    public EmployeeDto getEmployeeByEiamUserId(Long eiamUserId) {
        log.info("查询员工: {}", eiamUserId);
        Employee employee = employeeRepository.findByEiamUserId(eiamUserId)
            .orElseThrow(() -> new BusinessException("员工不存在: " + eiamUserId));
        EmployeeDto dto = employeeAssembler.toDto(employee);
        dto.setDepartmentIds(employeeRepository.findDepartmentIds(employee.getId()));
        dto.setPositionIds(employeeRepository.findPositionIds(employee.getId()));
        setDepartmentAndPositionNames(dto);
        return dto;
    }

    public List<EmployeeDto> searchEmployees(EmployeeQuery query) {
        log.info("查询员工列表");
        net.hwyz.iov.cloud.edd.org.service.domain.query.EmployeeQuery domainQuery =
            net.hwyz.iov.cloud.edd.org.service.domain.query.EmployeeQuery.builder()
                .code(query.getCode())
                .name(query.getName())
                .gender(query.getGender())
                .phone(query.getPhone())
                .eiamAccount(query.getEiamAccount())
                .build();
        List<Employee> employees = employeeRepository.findByConditions(domainQuery);
        List<EmployeeDto> dtos = employeeAssembler.toDtoList(employees);
        for (int i = 0; i < dtos.size(); i++) {
            EmployeeDto dto = dtos.get(i);
            Long employeeId = employees.get(i).getId();
            dto.setDepartmentIds(employeeRepository.findDepartmentIds(employeeId));
            dto.setPositionIds(employeeRepository.findPositionIds(employeeId));
            setDepartmentAndPositionNames(dto);
        }
        return dtos;
    }

    @Transactional
    public void deleteEmployees(Long[] ids) {
        log.info("删除员工: {}", ids);
        for (Long id : ids) {
            employeeRepository.deleteDepartments(id);
            employeeRepository.deletePositions(id);
            employeeRepository.delete(id);
        }
    }

    public boolean checkCodeUnique(Long id, String code) {
        return !employeeRepository.existsByCode(code, id);
    }

    private void setDepartmentAndPositionNames(EmployeeDto dto) {
        if (dto.getDepartmentIds() != null && !dto.getDepartmentIds().isEmpty()) {
            List<DepartmentPo> departments = departmentMapper.selectBatchIds(dto.getDepartmentIds());
            Map<Long, DepartmentPo> deptMap = departments.stream()
                .collect(Collectors.toMap(DepartmentPo::getId, Function.identity()));
            
            List<String> names = new ArrayList<>();
            for (int i = 0; i < dto.getDepartmentIds().size(); i++) {
                Long deptId = dto.getDepartmentIds().get(i);
                DepartmentPo dept = deptMap.get(deptId);
                String name = dept != null ? dept.getName() : "已删除";
                if (i == 0) {
                    name += "(主)";
                }
                names.add(name);
            }
            dto.setDepartmentNames(String.join(",", names));
        }
        
        if (dto.getPositionIds() != null && !dto.getPositionIds().isEmpty()) {
            List<PositionPo> positions = positionMapper.selectBatchIds(dto.getPositionIds());
            Map<Long, PositionPo> posMap = positions.stream()
                .collect(Collectors.toMap(PositionPo::getId, Function.identity()));
            
            List<String> names = new ArrayList<>();
            for (int i = 0; i < dto.getPositionIds().size(); i++) {
                Long posId = dto.getPositionIds().get(i);
                PositionPo pos = posMap.get(posId);
                String name = pos != null ? pos.getName() : "已删除";
                if (i == 0) {
                    name += "(主)";
                }
                names.add(name);
            }
            dto.setPositionNames(String.join(",", names));
        }
    }
}
