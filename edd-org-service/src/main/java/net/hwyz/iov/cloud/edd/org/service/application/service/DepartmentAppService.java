package net.hwyz.iov.cloud.edd.org.service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.service.application.assembler.DepartmentAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateDepartmentCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateDepartmentCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.query.DepartmentQuery;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentTreeDto;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Department;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentAppService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentAssembler departmentAssembler;

    @Transactional
    public DepartmentDto createDepartment(CreateDepartmentCmd cmd) {
        log.info("创建部门: {}", cmd.getName());

        if (cmd.getCode() != null && departmentRepository.existsByCode(cmd.getCode(), null)) {
            throw new BusinessException("部门编码已存在: " + cmd.getCode());
        }

        Department department = departmentAssembler.toDomain(cmd);

        if (cmd.getParentId() != null && cmd.getParentId() > 0) {
            Department parent = departmentRepository.findById(cmd.getParentId())
                .orElseThrow(() -> new BusinessException("父部门不存在: " + cmd.getParentId()));
            department.setAncestors(parent.getAncestors() + "," + cmd.getParentId());
        } else {
            department.setAncestors("0");
        }

        departmentRepository.save(department);

        return departmentAssembler.toDto(department);
    }

    @Transactional
    public DepartmentDto updateDepartment(UpdateDepartmentCmd cmd) {
        log.info("更新部门: {}", cmd.getId());

        Department department = departmentRepository.findById(cmd.getId())
            .orElseThrow(() -> new BusinessException("部门不存在: " + cmd.getId()));

        if (cmd.getCode() != null && departmentRepository.existsByCode(cmd.getCode(), cmd.getId())) {
            throw new BusinessException("部门编码已存在: " + cmd.getCode());
        }

        departmentAssembler.updateDomain(cmd, department);

        if (cmd.getParentId() != null && cmd.getParentId() > 0) {
            Department parent = departmentRepository.findById(cmd.getParentId())
                .orElseThrow(() -> new BusinessException("父部门不存在: " + cmd.getParentId()));
            department.setAncestors(parent.getAncestors() + "," + cmd.getParentId());
        } else {
            department.setAncestors("0");
        }

        departmentRepository.save(department);

        return departmentAssembler.toDto(department);
    }

    public DepartmentDto getDepartmentById(Long id) {
        log.info("查询部门: {}", id);
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new BusinessException("部门不存在: " + id));
        return departmentAssembler.toDto(department);
    }

    public DepartmentDto getDepartmentByCode(String code) {
        log.info("查询部门: {}", code);
        Department department = departmentRepository.findByCode(code)
            .orElseThrow(() -> new BusinessException("部门不存在: " + code));
        return departmentAssembler.toDto(department);
    }

    public List<DepartmentDto> searchDepartments(DepartmentQuery query) {
        log.info("查询部门列表");
        net.hwyz.iov.cloud.edd.org.service.domain.query.DepartmentQuery domainQuery =
            net.hwyz.iov.cloud.edd.org.service.domain.query.DepartmentQuery.builder()
                .code(query.getCode())
                .name(query.getName())
                .parentId(query.getParentId())
                .build();
        List<Department> departments = departmentRepository.findByConditions(domainQuery);
        return departmentAssembler.toDtoList(departments);
    }

    public List<DepartmentTreeDto> getDepartmentTree(DepartmentQuery query) {
        log.info("查询部门树");
        net.hwyz.iov.cloud.edd.org.service.domain.query.DepartmentQuery domainQuery =
            net.hwyz.iov.cloud.edd.org.service.domain.query.DepartmentQuery.builder()
                .code(query.getCode())
                .name(query.getName())
                .parentId(query.getParentId())
                .build();
        List<Department> departments = departmentRepository.findByConditions(domainQuery);
        return buildTree(departments, 0L);
    }

    @Transactional
    public void deleteDepartments(Long[] ids) {
        log.info("删除部门: {}", ids);
        for (Long id : ids) {
            departmentRepository.delete(id);
        }
    }

    public boolean checkCodeUnique(Long id, String code) {
        return !departmentRepository.existsByCode(code, id);
    }

    private List<DepartmentTreeDto> buildTree(List<Department> departments, Long parentId) {
        return departments.stream()
            .filter(dept -> {
                if (parentId == 0) {
                    return dept.getParentId() == null || dept.getParentId() == 0;
                }
                return parentId.equals(dept.getParentId());
            })
            .map(dept -> {
                DepartmentTreeDto dto = departmentAssembler.toTreeDto(dept);
                dto.setChildren(buildTree(departments, dept.getId()));
                return dto;
            })
            .collect(Collectors.toList());
    }
}
