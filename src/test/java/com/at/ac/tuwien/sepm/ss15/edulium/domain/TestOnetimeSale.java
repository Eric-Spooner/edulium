package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Unit Test for the OnetimeSale domain class
 */
public class TestOnetimeSale {
    @Test
    public void testIsAt_shouldAcceptTime() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-17T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-17T10:16:30");
        LocalDateTime time3 = LocalDateTime.parse("2015-06-17T10:16:00");

        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(time1);
        onetimeSale.setToTime(time2);

        assertTrue(onetimeSale.isAt(time3));
    }

    @Test
    public void testIsAt_shouldNotAcceptBefore() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-17T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-17T10:16:30");
        LocalDateTime time3 = LocalDateTime.parse("2015-06-17T10:14:00");

        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(time1);
        onetimeSale.setToTime(time2);

        assertFalse(onetimeSale.isAt(time3));
    }

    @Test
    public void testIsAt_shouldNotAcceptAfter() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-17T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-17T10:16:30");
        LocalDateTime time3 = LocalDateTime.parse("2015-06-17T10:18:00");

        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(time1);
        onetimeSale.setToTime(time2);

        assertFalse(onetimeSale.isAt(time3));
    }
}
