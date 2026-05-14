package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject.OrganizationCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {

    @Test
    void testActivateWhenEnabledShouldSucceed() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .enable(true)
                .build();

        assertDoesNotThrow(org::activate);
        assertTrue(org.getEnable());
    }

    @Test
    void testActivateWhenDisabledShouldThrowException() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .enable(false)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, org::activate);
        assertEquals("只有启用状态的组织才能激活", exception.getMessage());
    }

    @Test
    void testDeactivateWhenEnabledShouldSetDisable() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .enable(true)
                .build();

        org.deactivate();
        assertFalse(org.getEnable());
    }

    @Test
    void testDeactivateWhenDisabledShouldDoNothing() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .enable(false)
                .build();

        org.deactivate();
        assertFalse(org.getEnable());
    }

    @Test
    void testChangeParentShouldUpdateParentAndAncestors() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .parentId(10L)
                .ancestors("1,2,3")
                .build();

        org.changeParent(20L, "4,5,6");

        assertEquals(20L, org.getParentId());
        assertEquals("4,5,6", org.getAncestors());
    }

    @Test
    void testChangeParentToSelfShouldThrowException() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .parentId(10L)
                .ancestors("1,2,3")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> org.changeParent(1L, "1"));
        assertEquals("不能将组织设置为自己的子组织", exception.getMessage());
    }

    @Test
    void testChangeParentWithNullIdShouldSucceed() {
        Organization org = Organization.builder()
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .parentId(10L)
                .ancestors("1,2,3")
                .build();

        assertDoesNotThrow(() -> org.changeParent(1L, "1"));
        assertEquals(1L, org.getParentId());
    }

    @Test
    void testUpdateInfoShouldUpdateNonNullFields() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Old Name")
                .orgType("OLD_TYPE")
                .sort(1)
                .build();

        org.updateInfo("New Name", "NEW_TYPE", 2);

        assertEquals("New Name", org.getName());
        assertEquals("NEW_TYPE", org.getOrgType());
        assertEquals(2, org.getSort());
    }

    @Test
    void testUpdateInfoShouldNotUpdateNullFields() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Old Name")
                .orgType("OLD_TYPE")
                .sort(1)
                .build();

        org.updateInfo(null, null, null);

        assertEquals("Old Name", org.getName());
        assertEquals("OLD_TYPE", org.getOrgType());
        assertEquals(1, org.getSort());
    }

    @Test
    void testIsRootWhenParentIdIsNullShouldReturnTrue() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .parentId(null)
                .build();

        assertTrue(org.isRoot());
    }

    @Test
    void testIsRootWhenParentIdIsZeroShouldReturnTrue() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .parentId(0L)
                .build();

        assertTrue(org.isRoot());
    }

    @Test
    void testIsRootWhenParentIdIsNotZeroShouldReturnFalse() {
        Organization org = Organization.builder()
                .id(1L)
                .code(new OrganizationCode("ORG001"))
                .name("Test Org")
                .parentId(10L)
                .build();

        assertFalse(org.isRoot());
    }
}
