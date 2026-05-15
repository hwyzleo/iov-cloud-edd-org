package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import net.hwyz.iov.cloud.edd.org.api.vo.DepartmentMpt;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateDepartmentCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateDepartmentCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentDto;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DepartmentPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理后台部门转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface DepartmentMptAssembler {

    DepartmentMptAssembler INSTANCE = Mappers.getMapper(DepartmentMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param departmentPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "children", expression = "java(new java.util.ArrayList<>())")
    })
    DepartmentMpt fromPo(DepartmentPo departmentPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param departmentMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    DepartmentPo toPo(DepartmentMpt departmentMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param departmentPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<DepartmentMpt> fromPoList(List<DepartmentPo> departmentPoList);

    /**
     * DTO转MPT对象
     *
     * @param dto DTO
     * @return MPT对象
     */
    @Mappings({
            @Mapping(target = "children", expression = "java(new java.util.ArrayList<>())")
    })
    DepartmentMpt fromDto(DepartmentDto dto);

    /**
     * DTO列表转MPT对象列表
     *
     * @param dtoList DTO列表
     * @return MPT对象列表
     */
    List<DepartmentMpt> fromDtoList(List<DepartmentDto> dtoList);

    /**
     * MPT对象转创建命令
     *
     * @param departmentMpt MPT对象
     * @return 创建命令
     */
    CreateDepartmentCmd toCreateCmd(DepartmentMpt departmentMpt);

    /**
     * MPT对象转更新命令
     *
     * @param departmentMpt MPT对象
     * @return 更新命令
     */
    UpdateDepartmentCmd toUpdateCmd(DepartmentMpt departmentMpt);

}
