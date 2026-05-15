package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeMpt;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateEmployeeCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.EmployeeDto;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DepartmentMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.PositionMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DepartmentPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.PositionPo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 管理后台员工转换类
 *
 * @author hwyz_leo
 */
@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class EmployeeMptAssembler {

    private final DepartmentMapper departmentMapper;
    private final PositionMapper positionMapper;

    /**
     * 数据对象转数据传输对象
     *
     * @param employeePo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    public abstract EmployeeMpt fromPo(EmployeePo employeePo);

    /**
     * 数据传输对象转数据对象
     *
     * @param employeeMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    public abstract EmployeePo toPo(EmployeeMpt employeeMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param employeePoList 数据对象列表
     * @return 数据传输对象列表
     */
    public abstract List<EmployeeMpt> fromPoList(List<EmployeePo> employeePoList);

    /**
     * DTO转MPT对象
     *
     * @param dto DTO
     * @return MPT对象
     */
    @Mappings({})
    public abstract EmployeeMpt fromDto(EmployeeDto dto);

    /**
     * DTO列表转MPT对象列表
     *
     * @param dtoList DTO列表
     * @return MPT对象列表
     */
    public abstract List<EmployeeMpt> fromDtoList(List<EmployeeDto> dtoList);

    /**
     * MPT对象转创建命令
     *
     * @param employeeMpt MPT对象
     * @return 创建命令
     */
    public abstract CreateEmployeeCmd toCreateCmd(EmployeeMpt employeeMpt);

    /**
     * MPT对象转更新命令
     *
     * @param employeeMpt MPT对象
     * @return 更新命令
     */
    public abstract UpdateEmployeeCmd toUpdateCmd(EmployeeMpt employeeMpt);

    @AfterMapping
    protected void afterFromDto(EmployeeDto dto, @MappingTarget EmployeeMpt mpt) {
        if (dto != null) {
            setDepartmentNames(dto, mpt);
            setPositionNames(dto, mpt);
        }
    }

    private void setDepartmentNames(EmployeeDto dto, EmployeeMpt mpt) {
        if (dto.getDepartmentIds() != null && !dto.getDepartmentIds().isEmpty()) {
            List<DepartmentPo> departments = departmentMapper.selectBatchIds(dto.getDepartmentIds());
            Map<Long, DepartmentPo> deptMap = departments.stream()
                .collect(Collectors.toMap(DepartmentPo::getId, Function.identity()));
            
            List<String> names = new ArrayList<>();
            for (int i = 0; i < dto.getDepartmentIds().size(); i++) {
                Long deptId = dto.getDepartmentIds().get(i);
                DepartmentPo dept = deptMap.get(deptId);
                String name = dept != null ? dept.getName() : "已删除";
                if (i == 0) {
                    name += "(主)";
                }
                names.add(name);
            }
            mpt.setDepartmentNames(String.join(",", names));
        }
    }

    private void setPositionNames(EmployeeDto dto, EmployeeMpt mpt) {
        if (dto.getPositionIds() != null && !dto.getPositionIds().isEmpty()) {
            List<PositionPo> positions = positionMapper.selectBatchIds(dto.getPositionIds());
            Map<Long, PositionPo> posMap = positions.stream()
                .collect(Collectors.toMap(PositionPo::getId, Function.identity()));
            
            List<String> names = new ArrayList<>();
            for (int i = 0; i < dto.getPositionIds().size(); i++) {
                Long posId = dto.getPositionIds().get(i);
                PositionPo pos = posMap.get(posId);
                String name = pos != null ? pos.getName() : "已删除";
                if (i == 0) {
                    name += "(主)";
                }
                names.add(name);
            }
            mpt.setPositionNames(String.join(",", names));
        }
    }

}
