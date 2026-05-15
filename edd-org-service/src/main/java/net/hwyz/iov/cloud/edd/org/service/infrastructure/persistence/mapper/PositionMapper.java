package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.PositionPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 岗位 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2026-05-14
 */
@Mapper
public interface PositionMapper extends BaseDao<PositionPo, Long> {

    /**
     * 通过code查询岗位
     *
     * @param code 岗位编码
     * @return 岗位
     */
    PositionPo selectPoByCode(String code);

    /**
     * 批量查询岗位
     *
     * @param ids 岗位ID集合
     * @return 岗位列表
     */
    List<PositionPo> selectBatchIds(Collection<Long> ids);

}
