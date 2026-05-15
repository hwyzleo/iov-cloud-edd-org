package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    @Test
    void testChangeParentShouldUpdateParentAndAncestors() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .parentId(10L)
                .ancestors("1,2,3")
                .build();

        dept.changeParent(20L, "4,5,6");

        assertEquals(20L, dept.getParentId());
        assertEquals("4,5,6", dept.getAncestors());
    }

    @Test
    void testChangeParentToSelfShouldThrowException() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .parentId(10L)
                .ancestors("1,2,3")
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> dept.changeParent(1L, "1"));
        assertEquals("不能将部门设置为自己的子部门", exception.getMessage());
    }

    @Test
    void testChangeParentWithNullIdShouldSucceed() {
        Department dept = Department.builder()
                .name("Test Dept")
                .parentId(10L)
                .ancestors("1,2,3")
                .build();

        assertDoesNotThrow(() -> dept.changeParent(1L, "1"));
        assertEquals(1L, dept.getParentId());
    }

    @Test
    void testIsRootWhenParentIdIsNullShouldReturnTrue() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .parentId(null)
                .build();

        assertTrue(dept.isRoot());
    }

    @Test
    void testIsRootWhenParentIdIsZeroShouldReturnTrue() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .parentId(0L)
                .build();

        assertTrue(dept.isRoot());
    }

    @Test
    void testIsRootWhenParentIdIsNotZeroShouldReturnFalse() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .parentId(10L)
                .build();

        assertFalse(dept.isRoot());
    }

    @Test
    void testActivateWhenEnabledShouldSucceed() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .enable(true)
                .build();

        assertDoesNotThrow(dept::activate);
        assertTrue(dept.getEnable());
    }

    @Test
    void testActivateWhenDisabledShouldThrowException() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .enable(false)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, dept::activate);
        assertEquals("只有启用状态的部门才能激活", exception.getMessage());
    }

    @Test
    void testDeactivateWhenEnabledShouldSetDisable() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .enable(true)
                .build();

        dept.deactivate();
        assertFalse(dept.getEnable());
    }

    @Test
    void testDeactivateWhenDisabledShouldDoNothing() {
        Department dept = Department.builder()
                .id(1L)
                .name("Test Dept")
                .enable(false)
                .build();

        dept.deactivate();
        assertFalse(dept.getEnable());
    }

    @Test
    void testUpdateInfoShouldUpdateNonNullFields() {
        Department dept = Department.builder()
                .id(1L)
                .name("Old Name")
                .leader("Old Leader")
                .phone("1234567890")
                .email("old@example.com")
                .sort(1)
                .build();

        dept.updateInfo("New Name", "New Leader", "0987654321", "new@example.com", 2);

        assertEquals("New Name", dept.getName());
        assertEquals("New Leader", dept.getLeader());
        assertEquals("0987654321", dept.getPhone());
        assertEquals("new@example.com", dept.getEmail());
        assertEquals(2, dept.getSort());
    }

    @Test
    void testUpdateInfoShouldNotUpdateNullFields() {
        Department dept = Department.builder()
                .id(1L)
                .name("Old Name")
                .leader("Old Leader")
                .phone("1234567890")
                .email("old@example.com")
                .sort(1)
                .build();

        dept.updateInfo(null, null, null, null, null);

        assertEquals("Old Name", dept.getName());
        assertEquals("Old Leader", dept.getLeader());
        assertEquals("1234567890", dept.getPhone());
        assertEquals("old@example.com", dept.getEmail());
        assertEquals(1, dept.getSort());
    }
}
