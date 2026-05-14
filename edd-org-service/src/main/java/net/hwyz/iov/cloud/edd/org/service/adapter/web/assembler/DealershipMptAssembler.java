package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import net.hwyz.iov.cloud.edd.org.api.vo.DealershipMpt;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台门店转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface DealershipMptAssembler {

    DealershipMptAssembler INSTANCE = Mappers.getMapper(DealershipMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param dealershipPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    DealershipMpt fromPo(DealershipPo dealershipPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param dealershipMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    DealershipPo toPo(DealershipMpt dealershipMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param dealershipPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<DealershipMpt> fromPoList(List<DealershipPo> dealershipPoList);

}
