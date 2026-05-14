package net.hwyz.iov.cloud.edd.org.service.domain.repository;

import net.hwyz.iov.cloud.edd.org.service.application.dto.query.OrganizationQuery;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository {
    
    Optional<Organization> findById(Long id);
    
    Optional<Organization> findByCode(String code);
    
    List<Organization> findByConditions(OrganizationQuery query);
    
    List<Organization> findAll();
    
    void save(Organization organization);
    
    void delete(Long id);
    
    boolean existsByCode(String code, Long excludeId);
    
    List<Organization> findByParentId(Long parentId);
}