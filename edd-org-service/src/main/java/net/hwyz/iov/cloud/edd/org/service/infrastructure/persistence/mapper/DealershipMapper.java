package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 销售门店 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2024-10-30
 */
@Mapper
public interface DealershipMapper extends BaseDao<DealershipPo, Long> {

    /**
     * 通过code查询门店信息
     *
     * @param code 门店编码
     * @return 门店信息
     */
    DealershipPo selectPoByCode(String code);

}
