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
}
