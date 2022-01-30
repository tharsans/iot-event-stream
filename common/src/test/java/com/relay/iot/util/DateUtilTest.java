package com.relay.iot.util;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void isValid() {
        assertFalse(DateUtil.isValid(null));
        assertFalse(DateUtil.isValid(""));
        assertFalse(DateUtil.isValid("Invalid date"));
        assertFalse(DateUtil.isValid("2022-01-10 10:00:00"));
        //assertTrue(DateUtil.isValid("2022-01-10"));
    }

    @Test
    void testIsValid() {
        assertFalse(DateUtil.isValid("2022-01-10'T'10:00:00'Z'", null));
        assertFalse(DateUtil.isValid("2022-01-10'T'10:00:00'Z'", ""));
        assertFalse(DateUtil.isValid("2022-01-10'T'10:00:00'Z'", "YYYY/mm/DD"));
        //assertTrue(DateUtil.isValid("2022-01-10'", "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    void toOffsetDateTime() {
        assertThrows(DateTimeParseException.class, () -> DateUtil.toOffsetDateTime("2022-01-10'T'10:00:00'Z'"));
        //OffsetDateTime time = DateUtil.toOffsetDateTime("2022-01-01T10:30:00.000Z");
        //assertEquals("", time.toString());
    }

    @Test
    void testToOffsetDateTime() {
    }
}