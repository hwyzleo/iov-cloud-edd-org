package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Employee;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeePoConverter {

    Employee toDomain(EmployeePo employeePo);

    EmployeePo toPo(Employee employee);

    List<Employee> toDomainList(List<EmployeePo> employeePoList);

    List<EmployeePo> toPoList(List<Employee> employees);
}
