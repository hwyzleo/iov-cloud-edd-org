package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Position;
import net.hwyz.iov.cloud.edd.org.service.domain.query.PositionQuery;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.PositionRepository;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter.PositionPoConverter;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.PositionMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.PositionPo;
import net.hwyz.iov.cloud.framework.common.util.ParamHelper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class PositionRepositoryImpl implements PositionRepository {

    private final PositionMapper positionMapper;
    private final PositionPoConverter converter;

    @Override
    public Optional<Position> findById(Long id) {
        PositionPo positionPo = positionMapper.selectPoById(id);
        return Optional.ofNullable(positionPo).map(converter::toDomain);
    }

    @Override
    public Optional<Position> findByCode(String code) {
        PositionPo positionPo = positionMapper.selectPoByCode(code);
        return Optional.ofNullable(positionPo).map(converter::toDomain);
    }

    @Override
    public List<Position> findByConditions(PositionQuery query) {
        Map<String, Object> params = buildQueryParams(query);
        List<PositionPo> positionPoList = positionMapper.selectPoByMap(params);
        return converter.toDomainList(positionPoList);
    }

    @Override
    public List<Position> findAll() {
        List<PositionPo> positionPoList = positionMapper.selectPoByMap(new HashMap<>());
        return converter.toDomainList(positionPoList);
    }

    @Override
    public void save(Position position) {
        PositionPo positionPo = converter.toPo(position);
        if (position.getId() == null) {
            positionMapper.insertPo(positionPo);
            position.setId(positionPo.getId());
        } else {
            positionMapper.updatePo(positionPo);
        }
    }

    @Override
    public void delete(Long id) {
        positionMapper.physicalDeletePo(id);
    }

    @Override
    public boolean existsByCode(String code, Long excludeId) {
        PositionPo positionPo = positionMapper.selectPoByCode(code);
        return positionPo != null && !positionPo.getId().equals(excludeId);
    }

    private Map<String, Object> buildQueryParams(PositionQuery query) {
        Map<String, Object> params = new HashMap<>();
        if (query.getCode() != null) params.put("code", query.getCode());
        if (query.getName() != null) params.put("name", ParamHelper.fuzzyQueryParam(query.getName()));
        if (query.getEnable() != null) params.put("enable", query.getEnable());
        return params;
    }
}
