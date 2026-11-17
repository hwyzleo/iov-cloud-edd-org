package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipStaffPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface DealershipStaffMapper extends BaseDao<DealershipStaffPo, Long> {

    /**
     * 通过员工ID查询门店员工
     *
     * @param employeeId 员工ID
     * @return 门店信息
     */
    List<DealershipStaffPo> selectPoByEmployeeId(Long employeeId);

    /**
     * 通过关键词查询员工主数据（工号/姓名/手机号）
     *
     * @param key 关键词
     * @return 员工信息
     */
    List<EmployeePo> selectEmployeeByKey(String key);

    /**
     * 批量删除指定员工的门店员工关联
     *
     * @param employeeIds 员工ID集合
     * @return 删除行数
     */
    int deletePoByEmployeeIds(@Param("employeeIds") List<Long> employeeIds);

}
