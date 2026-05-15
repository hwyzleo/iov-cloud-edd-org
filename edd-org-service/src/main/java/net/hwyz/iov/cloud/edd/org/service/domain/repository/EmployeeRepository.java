package net.hwyz.iov.cloud.edd.org.service.domain.repository;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Employee;
import net.hwyz.iov.cloud.edd.org.service.domain.query.EmployeeQuery;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    Optional<Employee> findById(Long id);

    Optional<Employee> findByCode(String code);

    Optional<Employee> findByEiamUserId(Long eiamUserId);

    List<Employee> findByConditions(EmployeeQuery query);

    List<Employee> findAll();

    void save(Employee employee);

    void delete(Long id);

    boolean existsByCode(String code, Long excludeId);

    void saveDepartments(Long employeeId, List<Long> departmentIds);

    void savePositions(Long employeeId, List<Long> positionIds);

    void deleteDepartments(Long employeeId);

    void deletePositions(Long employeeId);

    List<Long> findDepartmentIds(Long employeeId);

    List<Long> findPositionIds(Long employeeId);
}
