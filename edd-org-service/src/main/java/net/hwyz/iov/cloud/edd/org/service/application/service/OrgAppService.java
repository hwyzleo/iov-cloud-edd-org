package net.hwyz.iov.cloud.edd.org.service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.service.application.assembler.OrganizationAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateOrganizationCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.UpdateOrganizationCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.query.OrganizationQuery;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.OrganizationDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.OrganizationTreeDto;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Organization;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrgAppService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationAssembler organizationAssembler;

    @Transactional
    public OrganizationDto createOrganization(CreateOrganizationCmd cmd) {
        log.info("创建组织: {}", cmd.getCode());

        if (organizationRepository.existsByCode(cmd.getCode(), null)) {
            throw new BusinessException("组织代码已存在: " + cmd.getCode());
        }

        Organization organization = organizationAssembler.toDomain(cmd);

        if (cmd.getParentId() != null && cmd.getParentId() > 0) {
            Organization parent = organizationRepository.findById(cmd.getParentId())
                .orElseThrow(() -> new BusinessException("父组织不存在: " + cmd.getParentId()));
            organization.setAncestors(parent.getAncestors() + "," + cmd.getParentId());
        } else {
            organization.setAncestors("0");
        }

        organizationRepository.save(organization);

        return organizationAssembler.toDto(organization);
    }

    @Transactional
    public OrganizationDto updateOrganization(UpdateOrganizationCmd cmd) {
        log.info("更新组织: {}", cmd.getId());

        Organization organization = organizationRepository.findById(cmd.getId())
            .orElseThrow(() -> new BusinessException("组织不存在: " + cmd.getId()));

        if (organizationRepository.existsByCode(cmd.getCode(), cmd.getId())) {
            throw new BusinessException("组织代码已存在: " + cmd.getCode());
        }

        organizationAssembler.updateDomain(cmd, organization);

        if (cmd.getParentId() != null && cmd.getParentId() > 0) {
            Organization parent = organizationRepository.findById(cmd.getParentId())
                .orElseThrow(() -> new BusinessException("父组织不存在: " + cmd.getParentId()));
            organization.setAncestors(parent.getAncestors() + "," + cmd.getParentId());
        } else {
            organization.setAncestors("0");
        }

        organizationRepository.save(organization);

        return organizationAssembler.toDto(organization);
    }

    public OrganizationDto getOrganizationById(Long id) {
        log.info("查询组织: {}", id);
        Organization organization = organizationRepository.findById(id)
            .orElseThrow(() -> new BusinessException("组织不存在: " + id));
        return organizationAssembler.toDto(organization);
    }

    public OrganizationDto getOrganizationByCode(String code) {
        log.info("查询组织: {}", code);
        Organization organization = organizationRepository.findByCode(code)
            .orElseThrow(() -> new BusinessException("组织不存在: " + code));
        return organizationAssembler.toDto(organization);
    }

    public List<OrganizationDto> searchOrganizations(OrganizationQuery query) {
        log.info("查询组织列表");
        net.hwyz.iov.cloud.edd.org.service.domain.query.OrganizationQuery domainQuery =
            net.hwyz.iov.cloud.edd.org.service.domain.query.OrganizationQuery.builder()
                .code(query.getCode())
                .name(query.getName())
                .orgType(query.getOrgType())
                .build();
        List<Organization> organizations = organizationRepository.findByConditions(domainQuery);
        return organizationAssembler.toDtoList(organizations);
    }

    public List<OrganizationTreeDto> getOrganizationTree(OrganizationQuery query) {
        log.info("查询组织树");
        net.hwyz.iov.cloud.edd.org.service.domain.query.OrganizationQuery domainQuery =
            net.hwyz.iov.cloud.edd.org.service.domain.query.OrganizationQuery.builder()
                .code(query.getCode())
                .name(query.getName())
                .orgType(query.getOrgType())
                .build();
        List<Organization> organizations = organizationRepository.findByConditions(domainQuery);
        return buildTree(organizations, 0L);
    }

    @Transactional
    public void deleteOrganizations(Long[] ids) {
        log.info("删除组织: {}", ids);
        for (Long id : ids) {
            organizationRepository.delete(id);
        }
    }

    public boolean checkCodeUnique(Long id, String code) {
        return !organizationRepository.existsByCode(code, id);
    }

    private List<OrganizationTreeDto> buildTree(List<Organization> organizations, Long parentId) {
        return organizations.stream()
            .filter(org -> {
                if (parentId == 0) {
                    return org.getParentId() == null || org.getParentId() == 0;
                }
                return parentId.equals(org.getParentId());
            })
            .map(org -> {
                OrganizationTreeDto dto = organizationAssembler.toTreeDto(org);
                dto.setChildren(buildTree(organizations, org.getId()));
                return dto;
            })
            .collect(Collectors.toList());
    }
}
