package net.hwyz.iov.cloud.edd.org.service.domain.repository;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Dealership;

import java.util.List;
import java.util.Optional;

public interface DealershipRepository {
    
    Optional<Dealership> findById(Long id);
    
    Optional<Dealership> findByCode(String code);
    
    List<Dealership> findAll();
    
    void save(Dealership dealership);
    
    void delete(Long id);
    
    boolean existsByCode(String code, Long excludeId);
}