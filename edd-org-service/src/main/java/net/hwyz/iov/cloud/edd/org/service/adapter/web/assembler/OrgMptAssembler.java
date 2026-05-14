package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import net.hwyz.iov.cloud.edd.org.api.vo.OrgMpt;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateOrganizationCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateOrganizationCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.OrganizationDto;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.OrgPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台组织结构转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface OrgMptAssembler {

    OrgMptAssembler INSTANCE = Mappers.getMapper(OrgMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param orgPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "children", expression = "java(new java.util.ArrayList<>())")
    })
    OrgMpt fromPo(OrgPo orgPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param orgMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    OrgPo toPo(OrgMpt orgMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param orgPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<OrgMpt> fromPoList(List<OrgPo> orgPoList);

    /**
     * DTO转MPT对象
     *
     * @param dto DTO
     * @return MPT对象
     */
    @Mappings({
            @Mapping(target = "children", expression = "java(new java.util.ArrayList<>())")
    })
    OrgMpt fromDto(OrganizationDto dto);

    /**
     * DTO列表转MPT对象列表
     *
     * @param dtoList DTO列表
     * @return MPT对象列表
     */
    List<OrgMpt> fromDtoList(List<OrganizationDto> dtoList);

    /**
     * MPT对象转创建命令
     *
     * @param orgMpt MPT对象
     * @return 创建命令
     */
    CreateOrganizationCmd toCreateCmd(OrgMpt orgMpt);

    /**
     * MPT对象转更新命令
     *
     * @param orgMpt MPT对象
     * @return 更新命令
     */
    UpdateOrganizationCmd toUpdateCmd(OrgMpt orgMpt);

}
