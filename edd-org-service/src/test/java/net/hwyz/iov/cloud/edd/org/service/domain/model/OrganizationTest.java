package net.hwyz.iov.cloud.edd.org.service.domain.model;

import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate.Organization;
import net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject.OrganizationCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrganizationTest {

    @Test
    void should_create_organization_with_valid_data() {
        // Given
        Organization org = Organization.builder()
            .id(1L)
            .code(new OrganizationCode("ORG001"))
            .name("Test Organization")
            .orgType("DEALER")
            .enable(true)
            .sort(1)
            .build();

        // Then
        assertThat(org.getId()).isEqualTo(1L);
        assertThat(org.getCode().getValue()).isEqualTo("ORG001");
        assertThat(org.getName()).isEqualTo("Test Organization");
        assertThat(org.getOrgType()).isEqualTo("DEALER");
        assertThat(org.getEnable()).isTrue();
        assertThat(org.getSort()).isEqualTo(1);
    }

    @Test
    void should_activate_organization_when_enabled() {
        // Given
        Organization org = Organization.builder()
            .id(1L)
            .code(new OrganizationCode("ORG001"))
            .enable(true)
            .build();

        // When
        org.activate();

        // Then
        assertThat(org.getEnable()).isTrue();
    }

    @Test
    void should_throw_exception_when_activate_disabled_organization() {
        // Given
        Organization org = Organization.builder()
            .id(1L)
            .code(new OrganizationCode("ORG001"))
            .enable(false)
            .build();

        // When & Then
        assertThatThrownBy(() -> org.activate())
            .isInstanceOf(BusinessException.class)
            .hasMessage("只有启用状态的组织才能激活");
    }

    @Test
    void should_deactivate_organization() {
        // Given
        Organization org = Organization.builder()
            .id(1L)
            .code(new OrganizationCode("ORG001"))
            .enable(true)
            .build();

        // When
        org.deactivate();

        // Then
        assertThat(org.getEnable()).isFalse();
    }

    @Test
    void should_change_parent() {
        // Given
        Organization org = Organization.builder()
            .id(1L)
            .code(new OrganizationCode("ORG001"))
            .parentId(0L)
            .ancestors("0")
            .build();

        // When
        org.changeParent(2L, "0,2");

        // Then
        assertThat(org.getParentId()).isEqualTo(2L);
        assertThat(org.getAncestors()).isEqualTo("0,2");
    }

    @Test
    void should_throw_exception_when_change_parent_to_self() {
        // Given
        Organization org = Organization.builder()
            .id(1L)
            .code(new OrganizationCode("ORG001"))
            .parentId(0L)
            .ancestors("0")
            .build();

        // When & Then
        assertThatThrownBy(() -> org.changeParent(1L, "0,1"))
            .isInstanceOf(BusinessException.class)
            .hasMessage("不能将组织设置为自己的子组织");
    }

    @Test
    void should_update_info() {
        // Given
        Organization org = Organization.builder()
            .id(1L)
            .code(new OrganizationCode("ORG001"))
            .name("Old Name")
            .orgType("OLD_TYPE")
            .enable(true)
            .sort(1)
            .build();

        // When
        org.updateInfo("New Name", "NEW_TYPE", 2);

        // Then
        assertThat(org.getName()).isEqualTo("New Name");
        assertThat(org.getOrgType()).isEqualTo("NEW_TYPE");
        assertThat(org.getSort()).isEqualTo(2);
    }

    @Test
    void should_identify_root_organization() {
        // Given
        Organization rootOrg = Organization.builder()
            .id(1L)
            .parentId(0L)
            .build();

        Organization childOrg = Organization.builder()
            .id(2L)
            .parentId(1L)
            .build();

        // Then
        assertThat(rootOrg.isRoot()).isTrue();
        assertThat(childOrg.isRoot()).isFalse();
    }
}
