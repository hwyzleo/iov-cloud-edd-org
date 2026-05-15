package net.hwyz.iov.cloud.edd.org.service.application.assembler;

import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateDepartmentCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateDepartmentCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentTreeDto;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentAssembler {

    Department toDomain(CreateDepartmentCmd cmd);

    Department toDomain(UpdateDepartmentCmd cmd);

    DepartmentDto toDto(Department department);

    List<DepartmentDto> toDtoList(List<Department> departments);

    DepartmentTreeDto toTreeDto(Department department);

    List<DepartmentTreeDto> toTreeDtoList(List<Department> departments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    void updateDomain(UpdateDepartmentCmd cmd, @MappingTarget Department department);
}
