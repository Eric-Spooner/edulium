package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Unit Test for the IntermittentSale domain class
 */
public class TestIntermittentSale {
    @Test
    public void testIsAt_shouldAcceptSameTime() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-17T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-17T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setDuration(60);
        intermittentSale1.setMonday(false);
        intermittentSale1.setTuesday(false);
        intermittentSale1.setWednesday(true);
        intermittentSale1.setThursday(false);
        intermittentSale1.setFriday(false);
        intermittentSale1.setSaturday(false);
        intermittentSale1.setSunday(false);
        intermittentSale1.setFromDayTime(time1);

        assertTrue(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay1() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-15T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-15T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setDuration(60);
        intermittentSale1.setMonday(false);
        intermittentSale1.setTuesday(false);
        intermittentSale1.setWednesday(false);
        intermittentSale1.setThursday(false);
        intermittentSale1.setFriday(false);
        intermittentSale1.setSaturday(false);
        intermittentSale1.setSunday(false);
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay2() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-16T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-16T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setDuration(60);
        intermittentSale1.setMonday(false);
        intermittentSale1.setTuesday(false);
        intermittentSale1.setWednesday(false);
        intermittentSale1.setThursday(false);
        intermittentSale1.setFriday(false);
        intermittentSale1.setSaturday(false);
        intermittentSale1.setSunday(false);
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay3() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-17T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-17T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setDuration(60);
        intermittentSale1.setMonday(false);
        intermittentSale1.setTuesday(false);
        intermittentSale1.setWednesday(false);
        intermittentSale1.setThursday(false);
        intermittentSale1.setFriday(false);
        intermittentSale1.setSaturday(false);
        intermittentSale1.setSunday(false);
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay4() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-18T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-18T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setDuration(60);
        intermittentSale1.setMonday(false);
        intermittentSale1.setTuesday(false);
        intermittentSale1.setWednesday(false);
        intermittentSale1.setThursday(false);
        intermittentSale1.setFriday(false);
        intermittentSale1.setSaturday(false);
        intermittentSale1.setSunday(false);
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay5() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-19T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-19T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setDuration(60);
        intermittentSale1.setMonday(false);
        intermittentSale1.setTuesday(false);
        intermittentSale1.setWednesday(false);
        intermittentSale1.setThursday(false);
        intermittentSale1.setFriday(false);
        intermittentSale1.setSaturday(false);
        intermittentSale1.setSunday(false);
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay6() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-20T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-20T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setDuration(60);
        intermittentSale1.setMonday(false);
        intermittentSale1.setTuesday(false);
        intermittentSale1.setWednesday(false);
        intermittentSale1.setThursday(false);
        intermittentSale1.setFriday(false);
        intermittentSale1.setSaturday(false);
        intermittentSale1.setSunday(false);
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay7() {
        LocalDateTime time1 = LocalDateTime.parse("2015-06-21T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-21T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setDuration(60);
        intermittentSale1.setMonday(false);
        intermittentSale1.setTuesday(false);
        intermittentSale1.setWednesday(false);
        intermittentSale1.setThursday(false);
        intermittentSale1.setFriday(false);
        intermittentSale1.setSaturday(false);
        intermittentSale1.setSunday(false);
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }
}
