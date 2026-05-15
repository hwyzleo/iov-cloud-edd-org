package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Department;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DepartmentPo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentPoConverter {

    Department toDomain(DepartmentPo departmentPo);

    DepartmentPo toPo(Department department);

    List<Department> toDomainList(List<DepartmentPo> departmentPoList);

    List<DepartmentPo> toPoList(List<Department> departments);
}
