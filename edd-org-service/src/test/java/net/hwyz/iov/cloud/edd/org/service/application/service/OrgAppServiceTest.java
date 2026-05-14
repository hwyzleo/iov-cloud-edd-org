package net.hwyz.iov.cloud.edd.org.service.application.service;

import net.hwyz.iov.cloud.edd.org.service.application.assembler.OrganizationAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.dto.cmd.CreateOrganizationCmd;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.OrganizationDto;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Organization;
import net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject.OrganizationCode;
import net.hwyz.iov.cloud.edd.org.service.domain.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrgAppServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationAssembler organizationAssembler;

    @InjectMocks
    private OrgAppService orgAppService;

    @Test
    void should_create_organization_successfully() {
        // Given
        CreateOrganizationCmd cmd = CreateOrganizationCmd.builder()
            .code("ORG001")
            .name("Test Organization")
            .orgType("DEALER")
            .parentId(0L)
            .enable(true)
            .sort(1)
            .build();

        Organization organization = Organization.builder()
            .code(new OrganizationCode("ORG001"))
            .name("Test Organization")
            .orgType("DEALER")
            .enable(true)
            .sort(1)
            .build();

        OrganizationDto expectedDto = OrganizationDto.builder()
            .id(1L)
            .code("ORG001")
            .name("Test Organization")
            .orgType("DEALER")
            .enable(true)
            .sort(1)
            .build();

        when(organizationRepository.existsByCode("ORG001", null)).thenReturn(false);
        when(organizationAssembler.toDomain(cmd)).thenReturn(organization);
        doNothing().when(organizationRepository).save(any(Organization.class));
        when(organizationAssembler.toDto(organization)).thenReturn(expectedDto);

        // When
        OrganizationDto result = orgAppService.createOrganization(cmd);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(organizationRepository).save(organization);
    }

    @Test
    void should_throw_exception_when_code_already_exists() {
        // Given
        CreateOrganizationCmd cmd = CreateOrganizationCmd.builder()
            .code("ORG001")
            .name("Test Organization")
            .orgType("DEALER")
            .parentId(0L)
            .enable(true)
            .sort(1)
            .build();

        when(organizationRepository.existsByCode("ORG001", null)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> orgAppService.createOrganization(cmd))
            .isInstanceOf(BusinessException.class)
            .hasMessage("组织代码已存在: ORG001");
    }

    @Test
    void should_get_organization_by_id() {
        // Given
        Long orgId = 1L;
        Organization organization = Organization.builder()
            .id(orgId)
            .code(new OrganizationCode("ORG001"))
            .name("Test Organization")
            .build();

        OrganizationDto expectedDto = OrganizationDto.builder()
            .id(orgId)
            .code("ORG001")
            .name("Test Organization")
            .build();

        when(organizationRepository.findById(orgId)).thenReturn(Optional.of(organization));
        when(organizationAssembler.toDto(organization)).thenReturn(expectedDto);

        // When
        OrganizationDto result = orgAppService.getOrganizationById(orgId);

        // Then
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void should_throw_exception_when_organization_not_found() {
        // Given
        Long orgId = 999L;
        when(organizationRepository.findById(orgId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orgAppService.getOrganizationById(orgId))
            .isInstanceOf(BusinessException.class)
            .hasMessage("组织不存在: 999");
    }
}
