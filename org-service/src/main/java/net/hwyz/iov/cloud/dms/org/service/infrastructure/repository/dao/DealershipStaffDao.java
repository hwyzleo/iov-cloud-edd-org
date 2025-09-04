package net.hwyz.iov.cloud.dms.org.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.dms.org.service.infrastructure.repository.po.DealershipStaffPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.mpt.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 销售门店员工 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2024-12-30
 */
@Mapper
public interface DealershipStaffDao extends BaseDao<DealershipStaffPo, Long> {

    /**
     * 通过用户ID查询门店员工
     *
     * @param userId 用户ID
     * @return 门店信息
     */
    DealershipStaffPo selectPoByUserId(Long userId);

    /**
     * 通过关键词查询员工
     *
     * @param key 关键词
     * @return 员工信息
     */
    List<SysUser> selectSysUserByKey(String key);

}
