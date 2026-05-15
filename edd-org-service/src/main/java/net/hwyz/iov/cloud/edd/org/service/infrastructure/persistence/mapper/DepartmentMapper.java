package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DepartmentPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 部门 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2026-05-14
 */
@Mapper
public interface DepartmentMapper extends BaseDao<DepartmentPo, Long> {

    /**
     * 通过code查询部门
     *
     * @param code 部门编码
     * @return 部门
     */
    DepartmentPo selectPoByCode(String code);

    /**
     * 批量查询部门
     *
     * @param ids 部门ID集合
     * @return 部门列表
     */
    List<DepartmentPo> selectBatchIds(Collection<Long> ids);

}
