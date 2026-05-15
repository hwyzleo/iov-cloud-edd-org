package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testActivateWhenEnabledShouldSucceed() {
        Position position = Position.builder()
                .id(1L)
                .code("POS001")
                .name("Test Position")
                .enable(true)
                .build();

        assertDoesNotThrow(position::activate);
        assertTrue(position.getEnable());
    }

    @Test
    void testActivateWhenDisabledShouldThrowException() {
        Position position = Position.builder()
                .id(1L)
                .code("POS001")
                .name("Test Position")
                .enable(false)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, position::activate);
        assertEquals("只有启用状态的岗位才能激活", exception.getMessage());
    }

    @Test
    void testDeactivateWhenEnabledShouldSetDisable() {
        Position position = Position.builder()
                .id(1L)
                .code("POS001")
                .name("Test Position")
                .enable(true)
                .build();

        position.deactivate();
        assertFalse(position.getEnable());
    }

    @Test
    void testDeactivateWhenDisabledShouldDoNothing() {
        Position position = Position.builder()
                .id(1L)
                .code("POS001")
                .name("Test Position")
                .enable(false)
                .build();

        position.deactivate();
        assertFalse(position.getEnable());
    }

    @Test
    void testUpdateInfoShouldUpdateNonNullFields() {
        Position position = Position.builder()
                .id(1L)
                .code("POS001")
                .name("Old Name")
                .sort(1)
                .build();

        position.updateInfo("New Name", 2);

        assertEquals("New Name", position.getName());
        assertEquals(2, position.getSort());
    }

    @Test
    void testUpdateInfoShouldNotUpdateNullFields() {
        Position position = Position.builder()
                .id(1L)
                .code("POS001")
                .name("Old Name")
                .sort(1)
                .build();

        position.updateInfo(null, null);

        assertEquals("Old Name", position.getName());
        assertEquals(1, position.getSort());
    }
}
