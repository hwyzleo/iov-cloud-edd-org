package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.OrgPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 组织结构 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-01-04
 */
@Mapper
public interface OrgMapper extends BaseDao<OrgPo, Long> {

    /**
     * 通过code查询组织结构
     *
     * @param code 组织结构编码
     * @return 组织结构
     */
    OrgPo selectPoByCode(String code);

}
