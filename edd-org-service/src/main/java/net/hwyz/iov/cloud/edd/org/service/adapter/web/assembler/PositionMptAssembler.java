package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import net.hwyz.iov.cloud.edd.org.api.vo.PositionMpt;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreatePositionCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdatePositionCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.PositionDto;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.PositionPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台岗位转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface PositionMptAssembler {

    PositionMptAssembler INSTANCE = Mappers.getMapper(PositionMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param positionPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    PositionMpt fromPo(PositionPo positionPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param positionMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    PositionPo toPo(PositionMpt positionMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param positionPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<PositionMpt> fromPoList(List<PositionPo> positionPoList);

    /**
     * DTO转MPT对象
     *
     * @param dto DTO
     * @return MPT对象
     */
    @Mappings({})
    PositionMpt fromDto(PositionDto dto);

    /**
     * DTO列表转MPT对象列表
     *
     * @param dtoList DTO列表
     * @return MPT对象列表
     */
    List<PositionMpt> fromDtoList(List<PositionDto> dtoList);

    /**
     * MPT对象转创建命令
     *
     * @param positionMpt MPT对象
     * @return 创建命令
     */
    CreatePositionCmd toCreateCmd(PositionMpt positionMpt);

    /**
     * MPT对象转更新命令
     *
     * @param positionMpt MPT对象
     * @return 更新命令
     */
    UpdatePositionCmd toUpdateCmd(PositionMpt positionMpt);

}
