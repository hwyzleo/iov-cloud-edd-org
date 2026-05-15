package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testResignShouldSetLeaveDateAndDisable() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Test Employee")
                .enable(true)
                .build();

        LocalDate leaveDate = LocalDate.of(2026, 5, 14);
        employee.resign(leaveDate);

        assertEquals(leaveDate, employee.getLeaveDate());
        assertFalse(employee.getEnable());
    }

    @Test
    void testIsActiveWhenEnabledAndNoLeaveDateShouldReturnTrue() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Test Employee")
                .enable(true)
                .leaveDate(null)
                .build();

        assertTrue(employee.isActive());
    }

    @Test
    void testIsActiveWhenDisabledShouldReturnFalse() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Test Employee")
                .enable(false)
                .leaveDate(null)
                .build();

        assertFalse(employee.isActive());
    }

    @Test
    void testIsActiveWhenHasLeaveDateShouldReturnFalse() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Test Employee")
                .enable(true)
                .leaveDate(LocalDate.of(2026, 5, 14))
                .build();

        assertFalse(employee.isActive());
    }

    @Test
    void testActivateWhenEnabledShouldSucceed() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Test Employee")
                .enable(true)
                .build();

        assertDoesNotThrow(employee::activate);
        assertTrue(employee.getEnable());
    }

    @Test
    void testActivateWhenDisabledShouldThrowException() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Test Employee")
                .enable(false)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, employee::activate);
        assertEquals("只有启用状态的员工才能激活", exception.getMessage());
    }

    @Test
    void testDeactivateWhenEnabledShouldSetDisable() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Test Employee")
                .enable(true)
                .build();

        employee.deactivate();
        assertFalse(employee.getEnable());
    }

    @Test
    void testDeactivateWhenDisabledShouldDoNothing() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Test Employee")
                .enable(false)
                .build();

        employee.deactivate();
        assertFalse(employee.getEnable());
    }

    @Test
    void testUpdateInfoShouldUpdateNonNullFields() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Old Name")
                .gender(1)
                .birthday(LocalDate.of(1990, 1, 1))
                .phone("1234567890")
                .email("old@example.com")
                .idCard("110101199001011234")
                .sort(1)
                .build();

        LocalDate newBirthday = LocalDate.of(1995, 5, 5);
        employee.updateInfo("New Name", 2, newBirthday, "0987654321", "new@example.com", "110101199505051234", 2);

        assertEquals("New Name", employee.getName());
        assertEquals(2, employee.getGender());
        assertEquals(newBirthday, employee.getBirthday());
        assertEquals("0987654321", employee.getPhone());
        assertEquals("new@example.com", employee.getEmail());
        assertEquals("110101199505051234", employee.getIdCard());
        assertEquals(2, employee.getSort());
    }

    @Test
    void testUpdateInfoShouldNotUpdateNullFields() {
        Employee employee = Employee.builder()
                .id(1L)
                .code("EMP001")
                .name("Old Name")
                .gender(1)
                .birthday(LocalDate.of(1990, 1, 1))
                .phone("1234567890")
                .email("old@example.com")
                .idCard("110101199001011234")
                .sort(1)
                .build();

        employee.updateInfo(null, null, null, null, null, null, null);

        assertEquals("Old Name", employee.getName());
        assertEquals(1, employee.getGender());
        assertEquals(LocalDate.of(1990, 1, 1), employee.getBirthday());
        assertEquals("1234567890", employee.getPhone());
        assertEquals("old@example.com", employee.getEmail());
        assertEquals("110101199001011234", employee.getIdCard());
        assertEquals(1, employee.getSort());
    }
}
