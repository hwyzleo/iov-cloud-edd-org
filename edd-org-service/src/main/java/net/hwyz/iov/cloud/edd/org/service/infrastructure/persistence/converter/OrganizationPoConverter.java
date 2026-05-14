package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Organization;
import net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject.OrganizationCode;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.OrgPo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationPoConverter {
    
    Organization toDomain(OrgPo orgPo);
    
    OrgPo toPo(Organization organization);
    
    List<Organization> toDomainList(List<OrgPo> orgPoList);
    
    List<OrgPo> toPoList(List<Organization> organizations);
    
    default String map(OrganizationCode code) {
        return code != null ? code.getValue() : null;
    }
    
    default OrganizationCode map(String code) {
        return code != null ? new OrganizationCode(code) : null;
    }
}
