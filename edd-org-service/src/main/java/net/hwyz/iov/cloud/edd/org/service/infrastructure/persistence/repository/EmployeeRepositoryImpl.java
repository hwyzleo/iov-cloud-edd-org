package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Employee;
import net.hwyz.iov.cloud.edd.org.service.domain.query.EmployeeQuery;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.EmployeeRepository;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter.EmployeePoConverter;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.EmployeeDepartmentMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.EmployeeMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.EmployeePositionMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeeDepartmentPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePositionPo;
import net.hwyz.iov.cloud.edd.framework.common.util.ParamHelper;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeMapper employeeMapper;
    private final EmployeeDepartmentMapper employeeDepartmentMapper;
    private final EmployeePositionMapper employeePositionMapper;
    private final EmployeePoConverter converter;

    @Override
    public Optional<Employee> findById(Long id) {
        EmployeePo employeePo = employeeMapper.selectPoById(id);
        return Optional.ofNullable(employeePo).map(converter::toDomain);
    }

    @Override
    public Optional<Employee> findByCode(String code) {
        EmployeePo employeePo = employeeMapper.selectPoByCode(code);
        return Optional.ofNullable(employeePo).map(converter::toDomain);
    }

    @Override
    public Optional<Employee> findByEiamUserId(Long eiamUserId) {
        EmployeePo employeePo = employeeMapper.selectPoByEiamUserId(eiamUserId);
        return Optional.ofNullable(employeePo).map(converter::toDomain);
    }

    @Override
    public List<Employee> findByConditions(EmployeeQuery query) {
        Map<String, Object> params = buildQueryParams(query);
        List<EmployeePo> employeePoList = employeeMapper.selectPoByMap(params);
        return converter.toDomainList(employeePoList);
    }

    @Override
    public List<Employee> findAll() {
        List<EmployeePo> employeePoList = employeeMapper.selectPoByMap(new HashMap<>());
        return converter.toDomainList(employeePoList);
    }

    @Override
    public void save(Employee employee) {
        EmployeePo employeePo = converter.toPo(employee);
        if (employee.getId() == null) {
            employeeMapper.insertPo(employeePo);
            employee.setId(employeePo.getId());
        } else {
            employeeMapper.updatePo(employeePo);
        }
    }

    @Override
    public void delete(Long id) {
        employeeMapper.physicalDeletePo(id);
    }

    @Override
    public boolean existsByCode(String code, Long excludeId) {
        EmployeePo employeePo = employeeMapper.selectPoByCode(code);
        return employeePo != null && !employeePo.getId().equals(excludeId);
    }

    private Map<String, Object> buildQueryParams(EmployeeQuery query) {
        Map<String, Object> params = new HashMap<>();
        if (query.getCode() != null) params.put("code", query.getCode());
        if (query.getName() != null) params.put("name", ParamHelper.fuzzyQueryParam(query.getName()));
        if (query.getGender() != null) params.put("gender", query.getGender());
        if (query.getPhone() != null) params.put("phone", ParamHelper.fuzzyQueryParam(query.getPhone()));
        if (query.getEiamAccount() != null) params.put("eiamAccount", query.getEiamAccount());
        if (query.getEnable() != null) params.put("enable", query.getEnable());
        return params;
    }

    @Override
    public void saveDepartments(Long employeeId, List<Long> departmentIds) {
        if (departmentIds == null || departmentIds.isEmpty()) return;
        for (int i = 0; i < departmentIds.size(); i++) {
            EmployeeDepartmentPo po = EmployeeDepartmentPo.builder()
                .employeeId(employeeId)
                .departmentId(departmentIds.get(i))
                .isPrimary(i == 0)
                .build();
            employeeDepartmentMapper.insert(po);
        }
    }

    @Override
    public void savePositions(Long employeeId, List<Long> positionIds) {
        if (positionIds == null || positionIds.isEmpty()) return;
        for (int i = 0; i < positionIds.size(); i++) {
            EmployeePositionPo po = EmployeePositionPo.builder()
                .employeeId(employeeId)
                .positionId(positionIds.get(i))
                .isPrimary(i == 0)
                .build();
            employeePositionMapper.insert(po);
        }
    }

    @Override
    public void deleteDepartments(Long employeeId) {
        employeeDepartmentMapper.delete(new LambdaQueryWrapper<EmployeeDepartmentPo>()
            .eq(EmployeeDepartmentPo::getEmployeeId, employeeId));
    }

    @Override
    public void deletePositions(Long employeeId) {
        employeePositionMapper.delete(new LambdaQueryWrapper<EmployeePositionPo>()
            .eq(EmployeePositionPo::getEmployeeId, employeeId));
    }

    @Override
    public List<Long> findDepartmentIds(Long employeeId) {
        return employeeDepartmentMapper.selectList(new LambdaQueryWrapper<EmployeeDepartmentPo>()
            .eq(EmployeeDepartmentPo::getEmployeeId, employeeId)
            .orderByDesc(EmployeeDepartmentPo::getIsPrimary))
            .stream()
            .map(EmployeeDepartmentPo::getDepartmentId)
            .collect(Collectors.toList());
    }

    @Override
    public List<Long> findPositionIds(Long employeeId) {
        return employeePositionMapper.selectList(new LambdaQueryWrapper<EmployeePositionPo>()
            .eq(EmployeePositionPo::getEmployeeId, employeeId)
            .orderByDesc(EmployeePositionPo::getIsPrimary))
            .stream()
            .map(EmployeePositionPo::getPositionId)
            .collect(Collectors.toList());
    }
}
