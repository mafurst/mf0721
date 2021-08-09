package com.mfurst.mf0721.util;

import com.mfurst.mf0721.exception.ErrorCode;
import com.mfurst.mf0721.exception.ToolManagementException;
import com.mfurst.mf0721.model.dto.Holiday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
class DateUtilTest {

    private DateUtil dateUtil;

    @BeforeEach
    void setUp() {
        dateUtil = new DateUtil();
    }

    @Test
    void getNumberOfWeekendDaysBetweenTwoWeekdaysOnTheSameWeek() {
        int numberOfDays = dateUtil.getNumberOfWeekendsBetweenTwoDates(
                LocalDate.of(2021, 8, 2), //Monday August 2nd
                LocalDate.of(2021, 8, 6) // Friday August 6th
        );
        assertEquals(0, numberOfDays);
    }

    @Test
    void getNumberOfWeekendDaysBetweenTwoWeekdaysOnDifferentWeeks() {
        int numberOfDays = dateUtil.getNumberOfWeekendsBetweenTwoDates(
                LocalDate.of(2021, 8, 2), //Monday August 2nd
                LocalDate.of(2021, 8, 13) // Friday August 13th
        );
        assertEquals(2, numberOfDays);
    }

    @Test
    void getNumberOfWeekendDaysBetweenTwoWeekendDaysOnSameWeek() {
        int numberOfDays = dateUtil.getNumberOfWeekendsBetweenTwoDates(
                LocalDate.of(2021, 8, 7), //Saturday August 7th
                LocalDate.of(2021, 8, 8) // Sunday August 8th
        );
        //We don't count the checkout date towards the weekend day count, but we do count the return date
        assertEquals(1, numberOfDays);
    }

    @Test
    void getNumberOfWeekendDaysWithFirstDateOnWeekendAndSecondNot() {
        int numberOfDays = dateUtil.getNumberOfWeekendsBetweenTwoDates(
                LocalDate.of(2021, 8, 8), //Sunday August 8th
                LocalDate.of(2021, 8, 10) // Tuesday August 10th
        );
        //We don't count the checkout date towards the weekend day count, but we do count the return date
        assertEquals(0, numberOfDays);
    }

    @Test
    void getNumberOfWeekendDaysWithFirstDateOnWeekendAndSecondOneWeekLater() {
        int numberOfDays = dateUtil.getNumberOfWeekendsBetweenTwoDates(
                LocalDate.of(2021, 8, 8), //Sunday August 8th
                LocalDate.of(2021, 8, 16) // Monday August 16th
        );
        //We don't count the checkout date towards the weekend day count, but we do count the return date
        assertEquals(2, numberOfDays);
    }

    @Test
    void getNumberOfHolidaysWhenThereAreNone() {
        ReflectionTestUtils.setField(dateUtil,"holidays", new ArrayList<>());
        try {
            int nHolidays = dateUtil.getNumberOfHolidaysBetweenTwoDates(
                    LocalDate.of(2020, 01, 01),
                    LocalDate.of(2020, 12, 31)
            );
            assertEquals(0, nHolidays);
        } catch (ToolManagementException ex) {
            //Always fail on any exception
            fail(ex.getMessage());
        }
    }

    @Test
    void getNumberOfHolidaysInOneRegularYear() {
        ReflectionTestUtils.setField(dateUtil,"holidays", List.of(
            new Holiday("next-monday", 9, 1),
            new Holiday("closest-weekday", 7, 4)
        ));
        try {
            int nHolidays = dateUtil.getNumberOfHolidaysBetweenTwoDates(
                    LocalDate.of(2020, 01, 01),
                    LocalDate.of(2020, 12, 31)
            );
            assertEquals(2, nHolidays);
        } catch (ToolManagementException ex) {
            //Always fail on any exception
            fail(ex.getMessage());
        }
    }

    @Test
    void getNumberOfHolidaysWhenJustBeforeLaborDay() {
        ReflectionTestUtils.setField(dateUtil,"holidays", List.of(
            new Holiday("next-monday", 9, 1),
            new Holiday("closest-weekday", 7, 4)
        ));
        try {
            //Labor day of 2021 is Monday September 6th
            int nHolidays = dateUtil.getNumberOfHolidaysBetweenTwoDates(
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 5)
            );
            assertEquals(0, nHolidays);
        } catch (ToolManagementException ex) {
            //Always fail on any exception
            fail(ex.getMessage());
        }
    }

    @Test
    void getNumberOfHolidaysWhenReturnedOnLaborDay() {
        ReflectionTestUtils.setField(dateUtil,"holidays", List.of(
            new Holiday("next-monday", 9, 1),
            new Holiday("closest-weekday", 7, 4)
        ));
        try {
            //Labor day of 2021 is Monday September 6th
            int nHolidays = dateUtil.getNumberOfHolidaysBetweenTwoDates(
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 6)
            );
            assertEquals(1, nHolidays);
        } catch (ToolManagementException ex) {
            //Always fail on any exception
            fail(ex.getMessage());
        }
    }

    @Test
    void getNumberOfHolidaysWhenReturnedDayAfterLaborDay() {
        ReflectionTestUtils.setField(dateUtil,"holidays", List.of(
            new Holiday("next-monday", 9, 1),
            new Holiday("closest-weekday", 7, 4)
        ));
        try {
            //Labor day of 2021 is Monday September 6th
            int nHolidays = dateUtil.getNumberOfHolidaysBetweenTwoDates(
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 7)
            );
            assertEquals(1, nHolidays);
        } catch (ToolManagementException ex) {
            //Always fail on any exception
            fail(ex.getMessage());
        }
    }

    @Test
    void getNumberOfHolidaysWhenCheckedOutOnLaborDay() {
        ReflectionTestUtils.setField(dateUtil,"holidays", List.of(
            new Holiday("next-monday", 9, 1),
            new Holiday("closest-weekday", 7, 4)
        ));
        try {
            //Labor day of 2021 is Monday September 6th
            int nHolidays = dateUtil.getNumberOfHolidaysBetweenTwoDates(
                    LocalDate.of(2021, 9, 6),
                    LocalDate.of(2020, 9, 8)
            );
            assertEquals(0, nHolidays);
        } catch (ToolManagementException ex) {
            //Always fail on any exception
            fail(ex.getMessage());
        }
    }


    /**
     * Check that if a holiday is supposed to be on the next closets Monday,
     * and it falls on a Monday, it should not change its date
     */
    @Test
    void testGetHolidayDateForNextMondayHolidayThatFallsOnMonday() {
        try {
            //Test with next monday holiday on a monday (Labor day of 2014)
            LocalDate testDate = dateUtil.getHolidayDate(new Holiday("next-monday", 9, 1), 2014);
            assertTrue(LocalDate.of(2014, 9, 1).isEqual(testDate));
            assertEquals(DayOfWeek.MONDAY, testDate.getDayOfWeek());
        } catch (ToolManagementException ex) {
            //On any exception, fail
            fail(ex.getMessage());
        }
    }

    /**
     * Test that for a holiday that falls on the next closest Monday, if it falls
     * on a weekday that isn't Monday, it will be on the next Monday
     */
    @Test
    void testGetHolidayDateForNextMondayHolidayThatFallsOnWeekday() {
        try {
            //Test with next monday holiday on a weekday (Labor day of 2021) September 1st is a Wednesday so Labor Day is September 6th
            LocalDate testDate = dateUtil.getHolidayDate(new Holiday("next-monday", 9, 1), 2021);
            assertTrue(LocalDate.of(2021, 9, 6).isEqual(testDate));
            assertEquals(DayOfWeek.MONDAY, testDate.getDayOfWeek());
        } catch (ToolManagementException ex) {
            //On any exception, fail
            fail(ex.getMessage());
        }
    }

    /**
     * Test that for a holiday that falls on the next closest Monday,
     * if it falls on a weekend, it will be on the next Monday
     */
    @Test
    void testGetHolidayDateForNextMondayHolidayThatFallsOnWeekend() {
        try {
            //Test with next monday holiday on a weekday (Labor day of 2019) September 1st is a Sunday so Labor Day is September 2nd
            LocalDate testDate = dateUtil.getHolidayDate(new Holiday("next-monday", 9, 1), 2019);
            assertTrue(LocalDate.of(2019, 9, 2).isEqual(testDate));
            assertEquals(DayOfWeek.MONDAY, testDate.getDayOfWeek());
        } catch (ToolManagementException ex) {
            //Fail on any exception
           fail(ex.getMessage());
        }
    }

    /**
     * Test for a holiday that falls on the closest weekday, that if it falls on
     * a weekday, it is not changed
     */
    @Test
    void testGetHolidayDateForClosestWeekdayHolidayThatFallsOnAWeekday() {
        try {
            //Test with closest weekday holiday that is on a weekday, July 4th of 2019 was on a Thursday
            LocalDate testDate = dateUtil.getHolidayDate(new Holiday("closest-weekday", 7, 4), 2019);
            assertTrue(LocalDate.of(2019, 7, 4).isEqual(testDate));
            assertNotEquals(DayOfWeek.SATURDAY, testDate.getDayOfWeek());
            assertNotEquals(DayOfWeek.SUNDAY, testDate.getDayOfWeek());
        } catch (ToolManagementException ex) {
            //Fail on any exception
            fail(ex.getMessage());
        }
    }

    /**
     * Test for a holiday that falls on the closes weekday, if it falls on a
     * Saturday, it will instead be celebrated on the Friday before
     */
    @Test
    void testGetHolidayDateForClosestWeekdayHolidayThatFallsOnASaturday() {
        try {
            //Test with closest weekday holiday that is on a Saturday, July 4th of 2020 was on a Saturday, so Independence day would be July 3rd
            LocalDate testDate = dateUtil.getHolidayDate(new Holiday("closest-weekday", 7, 4), 2020);
            assertTrue(LocalDate.of(2020, 7, 3).isEqual(testDate));
            assertEquals(DayOfWeek.FRIDAY, testDate.getDayOfWeek());
            assertNotEquals(DayOfWeek.SATURDAY, testDate.getDayOfWeek());
            assertNotEquals(DayOfWeek.SUNDAY, testDate.getDayOfWeek());
        } catch (ToolManagementException ex) {
            //Fail on any exception
            fail(ex.getMessage());
        }
    }

    /**
     * Test for a holiday that falls on the closest weekday, if it falls on
     * a Sunday, it will instead be celebrated on the Monday afterwards
     */
    @Test
    void testGetHolidayDateForClosestWeekdayHolidayThatFallsOnASunday() {
        try {
            //Test with closest weekday holiday that is on a Sunday, July 4th of 2021 was on a Sunday, so Independence day would be July 5th
            LocalDate testDate = dateUtil.getHolidayDate(new Holiday("closest-weekday", 7, 4), 2021);
            assertTrue(LocalDate.of(2021, 7, 5).isEqual(testDate));
            assertEquals(DayOfWeek.MONDAY, testDate.getDayOfWeek());
            assertNotEquals(DayOfWeek.SATURDAY, testDate.getDayOfWeek());
            assertNotEquals(DayOfWeek.SUNDAY, testDate.getDayOfWeek());
        } catch (ToolManagementException ex) {
            //Fail on any exception
            fail(ex.getMessage());
        }
    }

    /**
     * Test for a holiday with unknown type, an exception is thrown
     */
    @Test
    void testGetHolidayDateForUnknownHolidayType() {
        try {
            //Test with closest weekday holiday that is on a Sunday, July 4th of 2021 was on a Sunday, so Independence day would be July 5th
            dateUtil.getHolidayDate(new Holiday("invalid-type", 7, 4), 2021);
            fail("Invalid types should always fail holiday parsing");
        } catch (ToolManagementException ex) {
            assertEquals(ex.getErrorCode(), ErrorCode.DATE0001);
        }
    }

    /**
     * Test that when two dates are in the same year, we only get
     * one distinct year to check for Holidays
     */
    @Test
    void getYearsBetweenTwoDatesForTwoDatesInSameYear() {
        //We should just get one unique year for two dates in the same year
        List<Integer> years = dateUtil.getYearsBetweenTwoDates(
                LocalDate.of(2020,01,01),
                LocalDate.of(2020,12,31)
        );
        assertEquals(1, years.size());
        assertEquals(2020, years.get(0));
    }

    /**
     * Test that when two dates are not in the same year, we get all of the
     * included years
     */
    @Test
    void getYearsBetweenTwoDatesForTwoDatesOnDifferentYears() {
        List<Integer> years = dateUtil.getYearsBetweenTwoDates(
                LocalDate.of(2020,01,01),
                LocalDate.of(2021,01,01)
        );
        assertEquals(2, years.size());
        assertEquals(2020, years.get(0));
        assertEquals(2021, years.get(1));

        years = dateUtil.getYearsBetweenTwoDates(
                LocalDate.of(2020,12,31),
                LocalDate.of(2023,01,01)
        );
        assertEquals(4, years.size());
        assertEquals(2020, years.get(0));
        assertEquals(2021, years.get(1));
        assertEquals(2022, years.get(2));
        assertEquals(2023, years.get(3));
    }
}