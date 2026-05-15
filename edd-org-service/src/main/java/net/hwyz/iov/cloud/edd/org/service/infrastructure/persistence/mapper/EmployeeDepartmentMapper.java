package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeeDepartmentPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeDepartmentMapper extends BaseDao<EmployeeDepartmentPo, Long> {

    List<EmployeeDepartmentPo> selectPoByEmployeeId(Long employeeId);

    void deletePoByEmployeeId(Long employeeId);
}