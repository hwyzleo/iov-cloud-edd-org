package net.hwyz.iov.cloud.edd.org.service.application.assembler;

import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.EmployeeDto;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeAssembler {

    Employee toDomain(CreateEmployeeCmd cmd);

    Employee toDomain(UpdateEmployeeCmd cmd);

    EmployeeDto toDto(Employee employee);

    List<EmployeeDto> toDtoList(List<Employee> employees);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    void updateDomain(UpdateEmployeeCmd cmd, @MappingTarget Employee employee);
}