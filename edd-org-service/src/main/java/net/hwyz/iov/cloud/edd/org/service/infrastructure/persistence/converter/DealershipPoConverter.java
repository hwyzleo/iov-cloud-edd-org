package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter;

import net.hwyz.iov.cloud.edd.org.api.vo.Dealership;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 销售门店数据对象转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface DealershipPoConverter {

    DealershipPoConverter INSTANCE = Mappers.getMapper(DealershipPoConverter.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param orderPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    Dealership toDto(DealershipPo orderPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param orderDo 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    DealershipPo fromDo(Dealership orderDo);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param dealershipPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<Dealership> toDtoList(List<DealershipPo> dealershipPoList);

}
