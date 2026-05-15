package net.hwyz.iov.cloud.edd.org.service.domain.repository;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Position;
import net.hwyz.iov.cloud.edd.org.service.domain.query.PositionQuery;

import java.util.List;
import java.util.Optional;

public interface PositionRepository {

    Optional<Position> findById(Long id);

    Optional<Position> findByCode(String code);

    List<Position> findByConditions(PositionQuery query);

    List<Position> findAll();

    void save(Position position);

    void delete(Long id);

    boolean existsByCode(String code, Long excludeId);
}
