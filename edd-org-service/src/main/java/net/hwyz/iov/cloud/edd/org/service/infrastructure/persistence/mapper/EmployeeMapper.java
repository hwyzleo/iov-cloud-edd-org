package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2026-05-14
 */
@Mapper
public interface EmployeeMapper extends BaseDao<EmployeePo, Long> {

    /**
     * 通过code查询员工
     *
     * @param code 员工编码
     * @return 员工
     */
    EmployeePo selectPoByCode(String code);

    /**
     * 通过eiamUserId查询员工
     *
     * @param eiamUserId EIAM用户ID
     * @return 员工
     */
    EmployeePo selectPoByEiamUserId(Long eiamUserId);

}
