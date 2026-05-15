package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Department;
import net.hwyz.iov.cloud.edd.org.service.domain.query.DepartmentQuery;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.DepartmentRepository;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter.DepartmentPoConverter;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DepartmentMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DepartmentPo;
import net.hwyz.iov.cloud.framework.common.util.ParamHelper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentMapper departmentMapper;
    private final DepartmentPoConverter converter;

    @Override
    public Optional<Department> findById(Long id) {
        DepartmentPo departmentPo = departmentMapper.selectPoById(id);
        return Optional.ofNullable(departmentPo).map(converter::toDomain);
    }

    @Override
    public Optional<Department> findByCode(String code) {
        DepartmentPo departmentPo = departmentMapper.selectPoByCode(code);
        return Optional.ofNullable(departmentPo).map(converter::toDomain);
    }

    @Override
    public List<Department> findByConditions(DepartmentQuery query) {
        Map<String, Object> params = buildQueryParams(query);
        List<DepartmentPo> departmentPoList = departmentMapper.selectPoByMap(params);
        return converter.toDomainList(departmentPoList);
    }

    @Override
    public List<Department> findAll() {
        List<DepartmentPo> departmentPoList = departmentMapper.selectPoByMap(new HashMap<>());
        return converter.toDomainList(departmentPoList);
    }

    @Override
    public void save(Department department) {
        DepartmentPo departmentPo = converter.toPo(department);
        if (department.getId() == null) {
            departmentMapper.insertPo(departmentPo);
            department.setId(departmentPo.getId());
        } else {
            departmentMapper.updatePo(departmentPo);
        }
    }

    @Override
    public void delete(Long id) {
        departmentMapper.physicalDeletePo(id);
    }

    @Override
    public boolean existsByCode(String code, Long excludeId) {
        DepartmentPo departmentPo = departmentMapper.selectPoByCode(code);
        return departmentPo != null && !departmentPo.getId().equals(excludeId);
    }

    @Override
    public List<Department> findByParentId(Long parentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        List<DepartmentPo> departmentPoList = departmentMapper.selectPoByMap(params);
        return converter.toDomainList(departmentPoList);
    }

    private Map<String, Object> buildQueryParams(DepartmentQuery query) {
        Map<String, Object> params = new HashMap<>();
        if (query.getCode() != null) params.put("code", query.getCode());
        if (query.getName() != null) params.put("name", ParamHelper.fuzzyQueryParam(query.getName()));
        if (query.getParentId() != null) params.put("parentId", query.getParentId());
        if (query.getEnable() != null) params.put("enable", query.getEnable());
        return params;
    }
}
