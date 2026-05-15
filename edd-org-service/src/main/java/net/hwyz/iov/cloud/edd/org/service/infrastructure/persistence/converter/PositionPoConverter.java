package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter;

import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Position;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.PositionPo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PositionPoConverter {

    Position toDomain(PositionPo positionPo);

    PositionPo toPo(Position position);

    List<Position> toDomainList(List<PositionPo> positionPoList);

    List<PositionPo> toPoList(List<Position> positions);
}
