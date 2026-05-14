package net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject;

import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationCodeTest {

    @Test
    void testValidCodeShouldSucceed() {
        OrganizationCode code = new OrganizationCode("ORG001");
        assertEquals("ORG001", code.getValue());
    }

    @Test
    void testNullCodeShouldThrowException() {
        assertThrows(BusinessException.class, () -> new OrganizationCode(null));
    }

    @Test
    void testEmptyCodeShouldThrowException() {
        assertThrows(BusinessException.class, () -> new OrganizationCode(""));
    }

    @Test
    void testBlankCodeShouldThrowException() {
        assertThrows(BusinessException.class, () -> new OrganizationCode("   "));
    }

    @Test
    void testCodeExceedingMaxLengthShouldThrowException() {
        String longCode = "A".repeat(51);
        assertThrows(BusinessException.class, () -> new OrganizationCode(longCode));
    }

    @Test
    void testCodeAtMaxLengthShouldSucceed() {
        String maxLengthCode = "A".repeat(50);
        OrganizationCode code = new OrganizationCode(maxLengthCode);
        assertEquals(maxLengthCode, code.getValue());
    }

    @Test
    void testIsValidWithValidValueShouldReturnTrue() {
        assertTrue(OrganizationCode.isValid("ORG001"));
    }

    @Test
    void testIsValidWithNullShouldReturnFalse() {
        assertFalse(OrganizationCode.isValid(null));
    }

    @Test
    void testIsValidWithEmptyShouldReturnFalse() {
        assertFalse(OrganizationCode.isValid(""));
    }

    @Test
    void testIsValidWithBlankShouldReturnFalse() {
        assertFalse(OrganizationCode.isValid("   "));
    }

    @Test
    void testIsValidWithTooLongShouldReturnFalse() {
        assertFalse(OrganizationCode.isValid("A".repeat(51)));
    }

    @Test
    void testEqualsAndHashCode() {
        OrganizationCode code1 = new OrganizationCode("ORG001");
        OrganizationCode code2 = new OrganizationCode("ORG001");
        OrganizationCode code3 = new OrganizationCode("ORG002");

        assertEquals(code1, code2);
        assertEquals(code1.hashCode(), code2.hashCode());
        assertNotEquals(code1, code3);
    }
}
