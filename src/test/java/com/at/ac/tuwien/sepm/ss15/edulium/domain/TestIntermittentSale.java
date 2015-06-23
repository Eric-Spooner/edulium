package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit Test for the IntermittentSale domain class
 */
public class TestIntermittentSale {

    @Test
    public void testIsAt_shouldAcceptSameTime() {
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-17T10:15:30.1");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(123L);
        intermittentSale1.setDuration(Duration.ofMinutes(60));

        HashSet<DayOfWeek> saleDays = new HashSet<>();
        saleDays.add(DayOfWeek.WEDNESDAY);
        intermittentSale1.setDaysOfSale(saleDays);

        intermittentSale1.setFromDayTime(time1);

        assertTrue(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay1() {
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-15T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(123L);
        intermittentSale1.setDuration(Duration.ofMinutes(60));
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay2() {
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-16T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(123L);
        intermittentSale1.setDuration(Duration.ofMinutes(60));
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay3() {
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-17T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(123L);
        intermittentSale1.setDuration(Duration.ofMinutes(60));
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay4() {
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-18T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(123L);
        intermittentSale1.setDuration(Duration.ofMinutes(60));
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay5() {
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-19T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(123L);
        intermittentSale1.setDuration(Duration.ofMinutes(60));
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay6() {
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-20T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(123L);
        intermittentSale1.setDuration(Duration.ofMinutes(60));
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }

    @Test
    public void testIsAt_shouldNotAcceptWrongDay7() {
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2015-06-21T10:15:30");

        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(123L);
        intermittentSale1.setDuration(Duration.ofMinutes(60));
        intermittentSale1.setFromDayTime(time1);

        assertFalse(intermittentSale1.isAt(time2));
    }
}
