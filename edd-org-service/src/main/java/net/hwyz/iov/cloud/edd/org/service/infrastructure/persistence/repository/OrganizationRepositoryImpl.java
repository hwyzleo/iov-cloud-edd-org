package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Organization;
import net.hwyz.iov.cloud.edd.org.service.domain.query.OrganizationQuery;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.OrganizationRepository;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.converter.OrganizationPoConverter;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.OrgMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.OrgPo;
import net.hwyz.iov.cloud.framework.common.util.ParamHelper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {

    private final OrgMapper orgMapper;
    private final OrganizationPoConverter converter;

    @Override
    public Optional<Organization> findById(Long id) {
        OrgPo orgPo = orgMapper.selectPoById(id);
        return Optional.ofNullable(orgPo).map(converter::toDomain);
    }

    @Override
    public Optional<Organization> findByCode(String code) {
        OrgPo orgPo = orgMapper.selectPoByCode(code);
        return Optional.ofNullable(orgPo).map(converter::toDomain);
    }

    @Override
    public List<Organization> findByConditions(OrganizationQuery query) {
        Map<String, Object> params = buildQueryParams(query);
        List<OrgPo> orgPoList = orgMapper.selectPoByMap(params);
        return converter.toDomainList(orgPoList);
    }

    @Override
    public List<Organization> findAll() {
        List<OrgPo> orgPoList = orgMapper.selectPoByMap(new HashMap<>());
        return converter.toDomainList(orgPoList);
    }

    @Override
    public void save(Organization organization) {
        OrgPo orgPo = converter.toPo(organization);
        if (organization.getId() == null) {
            orgMapper.insertPo(orgPo);
            organization.setId(orgPo.getId());
        } else {
            orgMapper.updatePo(orgPo);
        }
    }

    @Override
    public void delete(Long id) {
        orgMapper.physicalDeletePo(id);
    }

    @Override
    public boolean existsByCode(String code, Long excludeId) {
        OrgPo orgPo = orgMapper.selectPoByCode(code);
        return orgPo != null && !orgPo.getId().equals(excludeId);
    }

    @Override
    public List<Organization> findByParentId(Long parentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        List<OrgPo> orgPoList = orgMapper.selectPoByMap(params);
        return converter.toDomainList(orgPoList);
    }

    private Map<String, Object> buildQueryParams(OrganizationQuery query) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", query.getCode());
        params.put("name", ParamHelper.fuzzyQueryParam(query.getName()));
        params.put("orgType", query.getOrgType());
        params.put("parentId", query.getParentId());
        params.put("enable", query.getEnable());
        return params;
    }
}
