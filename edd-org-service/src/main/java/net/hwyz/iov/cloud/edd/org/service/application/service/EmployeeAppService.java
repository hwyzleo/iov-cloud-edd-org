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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeAppService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeAssembler employeeAssembler;

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

        return employeeAssembler.toDto(employee);
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

        return employeeAssembler.toDto(employee);
    }

    public EmployeeDto getEmployeeById(Long id) {
        log.info("查询员工: {}", id);
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new BusinessException("员工不存在: " + id));
        return employeeAssembler.toDto(employee);
    }

    public EmployeeDto getEmployeeByCode(String code) {
        log.info("查询员工: {}", code);
        Employee employee = employeeRepository.findByCode(code)
            .orElseThrow(() -> new BusinessException("员工不存在: " + code));
        return employeeAssembler.toDto(employee);
    }

    public EmployeeDto getEmployeeByEiamUserId(Long eiamUserId) {
        log.info("查询员工: {}", eiamUserId);
        Employee employee = employeeRepository.findByEiamUserId(eiamUserId)
            .orElseThrow(() -> new BusinessException("员工不存在: " + eiamUserId));
        return employeeAssembler.toDto(employee);
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
        return employeeAssembler.toDtoList(employees);
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
}
