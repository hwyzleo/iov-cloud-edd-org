package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import net.hwyz.iov.cloud.edd.org.api.vo.DepartmentExService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DepartmentPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 对外服务部门转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface DepartmentExServiceAssembler {

    DepartmentExServiceAssembler INSTANCE = Mappers.getMapper(DepartmentExServiceAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param departmentPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    DepartmentExService fromPo(DepartmentPo departmentPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param departmentExService 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    DepartmentPo toPo(DepartmentExService departmentExService);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param departmentPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<DepartmentExService> fromPoList(List<DepartmentPo> departmentPoList);

}
