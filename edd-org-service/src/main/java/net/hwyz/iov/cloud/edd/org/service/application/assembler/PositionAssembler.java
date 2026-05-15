package net.hwyz.iov.cloud.edd.org.service.application.assembler;

import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreatePositionCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdatePositionCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.PositionDto;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PositionAssembler {

    Position toDomain(CreatePositionCmd cmd);

    Position toDomain(UpdatePositionCmd cmd);

    PositionDto toDto(Position position);

    List<PositionDto> toDtoList(List<Position> positions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    void updateDomain(UpdatePositionCmd cmd, @MappingTarget Position position);
}
