package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeExService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 对外服务员工转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface EmployeeExServiceAssembler {

    EmployeeExServiceAssembler INSTANCE = Mappers.getMapper(EmployeeExServiceAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param employeePo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    EmployeeExService fromPo(EmployeePo employeePo);

    /**
     * 数据传输对象转数据对象
     *
     * @param employeeExService 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    EmployeePo toPo(EmployeeExService employeeExService);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param employeePoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<EmployeeExService> fromPoList(List<EmployeePo> employeePoList);

}
