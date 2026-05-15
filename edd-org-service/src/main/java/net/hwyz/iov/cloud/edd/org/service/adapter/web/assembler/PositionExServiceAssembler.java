package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import net.hwyz.iov.cloud.edd.org.api.vo.PositionExService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.PositionPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 对外服务岗位转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface PositionExServiceAssembler {

    PositionExServiceAssembler INSTANCE = Mappers.getMapper(PositionExServiceAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param positionPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    PositionExService fromPo(PositionPo positionPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param positionExService 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    PositionPo toPo(PositionExService positionExService);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param positionPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<PositionExService> fromPoList(List<PositionPo> positionPoList);

}
