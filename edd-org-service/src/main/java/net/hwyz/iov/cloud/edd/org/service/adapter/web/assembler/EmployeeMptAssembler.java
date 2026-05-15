package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeMpt;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.EmployeeDto;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台员工转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface EmployeeMptAssembler {

    EmployeeMptAssembler INSTANCE = Mappers.getMapper(EmployeeMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param employeePo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    EmployeeMpt fromPo(EmployeePo employeePo);

    /**
     * 数据传输对象转数据对象
     *
     * @param employeeMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    EmployeePo toPo(EmployeeMpt employeeMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param employeePoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<EmployeeMpt> fromPoList(List<EmployeePo> employeePoList);

    /**
     * DTO转MPT对象
     *
     * @param dto DTO
     * @return MPT对象
     */
    @Mappings({})
    EmployeeMpt fromDto(EmployeeDto dto);

    /**
     * DTO列表转MPT对象列表
     *
     * @param dtoList DTO列表
     * @return MPT对象列表
     */
    List<EmployeeMpt> fromDtoList(List<EmployeeDto> dtoList);

    /**
     * MPT对象转创建命令
     *
     * @param employeeMpt MPT对象
     * @return 创建命令
     */
    CreateEmployeeCmd toCreateCmd(EmployeeMpt employeeMpt);

    /**
     * MPT对象转更新命令
     *
     * @param employeeMpt MPT对象
     * @return 更新命令
     */
    UpdateEmployeeCmd toUpdateCmd(EmployeeMpt employeeMpt);

}
