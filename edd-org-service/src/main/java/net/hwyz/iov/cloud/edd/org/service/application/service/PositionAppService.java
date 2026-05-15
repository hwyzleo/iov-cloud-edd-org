package net.hwyz.iov.cloud.edd.org.service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.service.application.assembler.PositionAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreatePositionCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdatePositionCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.query.PositionQuery;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.PositionDto;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Position;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionAppService {

    private final PositionRepository positionRepository;
    private final PositionAssembler positionAssembler;

    @Transactional
    public PositionDto createPosition(CreatePositionCmd cmd) {
        log.info("创建岗位: {}", cmd.getCode());

        if (positionRepository.existsByCode(cmd.getCode(), null)) {
            throw new BusinessException("岗位编码已存在: " + cmd.getCode());
        }

        Position position = positionAssembler.toDomain(cmd);
        positionRepository.save(position);

        return positionAssembler.toDto(position);
    }

    @Transactional
    public PositionDto updatePosition(UpdatePositionCmd cmd) {
        log.info("更新岗位: {}", cmd.getId());

        Position position = positionRepository.findById(cmd.getId())
            .orElseThrow(() -> new BusinessException("岗位不存在: " + cmd.getId()));

        if (positionRepository.existsByCode(cmd.getCode(), cmd.getId())) {
            throw new BusinessException("岗位编码已存在: " + cmd.getCode());
        }

        positionAssembler.updateDomain(cmd, position);
        positionRepository.save(position);

        return positionAssembler.toDto(position);
    }

    public PositionDto getPositionById(Long id) {
        log.info("查询岗位: {}", id);
        Position position = positionRepository.findById(id)
            .orElseThrow(() -> new BusinessException("岗位不存在: " + id));
        return positionAssembler.toDto(position);
    }

    public PositionDto getPositionByCode(String code) {
        log.info("查询岗位: {}", code);
        Position position = positionRepository.findByCode(code)
            .orElseThrow(() -> new BusinessException("岗位不存在: " + code));
        return positionAssembler.toDto(position);
    }

    public List<PositionDto> searchPositions(PositionQuery query) {
        log.info("查询岗位列表");
        net.hwyz.iov.cloud.edd.org.service.domain.query.PositionQuery domainQuery =
            net.hwyz.iov.cloud.edd.org.service.domain.query.PositionQuery.builder()
                .code(query.getCode())
                .name(query.getName())
                .build();
        List<Position> positions = positionRepository.findByConditions(domainQuery);
        return positionAssembler.toDtoList(positions);
    }

    @Transactional
    public void deletePositions(Long[] ids) {
        log.info("删除岗位: {}", ids);
        for (Long id : ids) {
            positionRepository.delete(id);
        }
    }

    public boolean checkCodeUnique(Long id, String code) {
        return !positionRepository.existsByCode(code, id);
    }
}
