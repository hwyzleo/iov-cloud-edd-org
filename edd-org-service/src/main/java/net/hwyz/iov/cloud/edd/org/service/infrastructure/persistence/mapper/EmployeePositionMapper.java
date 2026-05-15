package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePositionPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeePositionMapper extends BaseDao<EmployeePositionPo, Long> {

    List<EmployeePositionPo> selectPoByEmployeeId(Long employeeId);

    void deletePoByEmployeeId(Long employeeId);
}