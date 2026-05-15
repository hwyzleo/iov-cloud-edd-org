package net.hwyz.iov.cloud.edd.org.service.application.assembler;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.EmployeeDto;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Employee;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.EmployeeDepartmentMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.EmployeePositionMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeeDepartmentPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePositionPo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class EmployeeAssembler {

    private final EmployeeDepartmentMapper employeeDepartmentMapper;
    private final EmployeePositionMapper employeePositionMapper;

    public abstract Employee toDomain(CreateEmployeeCmd cmd);

    public abstract Employee toDomain(UpdateEmployeeCmd cmd);

    public abstract EmployeeDto toDto(Employee employee);

    public abstract List<EmployeeDto> toDtoList(List<Employee> employees);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    public abstract void updateDomain(UpdateEmployeeCmd cmd, @MappingTarget Employee employee);

    @AfterMapping
    protected void afterToDto(Employee employee, @MappingTarget EmployeeDto dto) {
        if (employee != null && employee.getId() != null) {
            List<EmployeeDepartmentPo> departmentRelations = employeeDepartmentMapper.lambdaQuery()
                .eq(EmployeeDepartmentPo::getEmployeeId, employee.getId())
                .orderByDesc(EmployeeDepartmentPo::getIsPrimary)
                .list();
            dto.setDepartmentIds(departmentRelations.stream()
                .map(EmployeeDepartmentPo::getDepartmentId)
                .collect(Collectors.toList()));

            List<EmployeePositionPo> positionRelations = employeePositionMapper.lambdaQuery()
                .eq(EmployeePositionPo::getEmployeeId, employee.getId())
                .orderByDesc(EmployeePositionPo::getIsPrimary)
                .list();
            dto.setPositionIds(positionRelations.stream()
                .map(EmployeePositionPo::getPositionId)
                .collect(Collectors.toList()));
        }
    }
}
