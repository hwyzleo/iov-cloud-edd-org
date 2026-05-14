package net.hwyz.iov.cloud.edd.org.service.application.assembler;

import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateOrganizationCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateOrganizationCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.OrganizationDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.OrganizationTreeDto;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationAssembler {

    @Mapping(target = "code", expression = "java(new net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject.OrganizationCode(cmd.getCode()))")
    Organization toDomain(CreateOrganizationCmd cmd);

    @Mapping(target = "code", expression = "java(new net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject.OrganizationCode(cmd.getCode()))")
    Organization toDomain(UpdateOrganizationCmd cmd);

    @Mapping(target = "code", expression = "java(organization.getCode().getValue())")
    OrganizationDto toDto(Organization organization);

    List<OrganizationDto> toDtoList(List<Organization> organizations);

    @Mapping(target = "code", expression = "java(organization.getCode().getValue())")
    OrganizationTreeDto toTreeDto(Organization organization);

    List<OrganizationTreeDto> toTreeDtoList(List<Organization> organizations);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    @Mapping(target = "code", expression = "java(new net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject.OrganizationCode(cmd.getCode()))")
    void updateDomain(UpdateOrganizationCmd cmd, @MappingTarget Organization organization);
}
