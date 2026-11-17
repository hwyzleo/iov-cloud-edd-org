package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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

    /**
     * 增量查询员工变更（含逻辑删除的墓碑记录，EDD-ORG-DSN-CR-004）
     *
     * @param since  水位（modify_time 下界，不含）
     * @param offset 偏移量
     * @param limit  每页条数
     * @return 员工变更列表，按 modify_time, id 升序
     */
    List<EmployeePo> selectChanges(@Param("since") Date since, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 统计增量变更数量
     *
     * @param since 水位（modify_time 下界，不含）
     * @return 数量
     */
    long countChanges(@Param("since") Date since);

    /**
     * 检索在职员工（供 MPT 用户绑定选人）
     *
     * @param key   关键词（工号/姓名/手机号，模糊，已含 %）
     * @param limit 返回上限
     * @return 在职员工列表
     */
    List<EmployeePo> selectActiveByKey(@Param("key") String key, @Param("limit") int limit);

}
