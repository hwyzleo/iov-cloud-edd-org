package net.hwyz.iov.cloud.edd.org.service.domain.repository;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Department;
import net.hwyz.iov.cloud.edd.org.service.domain.query.DepartmentQuery;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository {

    Optional<Department> findById(Long id);

    Optional<Department> findByCode(String code);

    List<Department> findByConditions(DepartmentQuery query);

    List<Department> findAll();

    void save(Department department);

    void delete(Long id);

    boolean existsByCode(String code, Long excludeId);

    List<Department> findByParentId(Long parentId);
}
