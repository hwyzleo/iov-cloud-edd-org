package net.hwyz.iov.cloud.edd.org.service.domain.repository;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.DealershipStaff;

import java.util.List;
import java.util.Optional;

public interface DealershipStaffRepository {
    
    Optional<DealershipStaff> findById(Long id);
    
    List<DealershipStaff> findByDealershipId(Long dealershipId);
    
    void save(DealershipStaff staff);
    
    void delete(Long id);
}