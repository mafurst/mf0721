package com.mfurst.mf0721.util;

import com.mfurst.mf0721.exception.ErrorCode;
import com.mfurst.mf0721.exception.ToolManagementException;
import com.mfurst.mf0721.model.dto.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for date operations
 */
@Component
public class DateUtil {

    @Autowired
    private List<Holiday> holidays;

    /**
     * Get the number of weekend days between two dates
     * @param firstDate
     * @param secondDate
     * @return
     */
    public int getNumberOfWeekendsBetweenTwoDates(
            LocalDate firstDate, LocalDate secondDate) {
        int weekendsBetween = 0;
        while (!(firstDate.isEqual(secondDate) || firstDate.isAfter(secondDate))) {
            firstDate = firstDate.plusDays(1);
            if (firstDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    || firstDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                weekendsBetween++;
            }
        }
        return weekendsBetween;
    }

    /**
     * Calculate the number of observed holidays between two dates
     * @param firstDate
     * @param secondDate
     * @return
     */
    public int getNumberOfHolidaysBetweenTwoDates(
            LocalDate firstDate, LocalDate secondDate) throws ToolManagementException{
        //Always return 0 holidays if there are none, no need to do other logic
        if (holidays.isEmpty()) return 0;

        int totalHolidays = 0;
        List<Integer> uniqueYearsBetweenDates = getYearsBetweenTwoDates(firstDate, secondDate);

        for (Holiday holiday: holidays) {
            for (Integer year: uniqueYearsBetweenDates) {
                LocalDate holidayDate = getHolidayDate(holiday, year);
                if (firstDate.isBefore(holidayDate)
                    && ( secondDate.isAfter(holidayDate) || secondDate.isEqual(holidayDate) )
                ) {
                    //If the holiday is between the first date (non-inclusive) and second date (inclusive)
                    //then we need to include it in the total holidays
                    totalHolidays += 1;
                }
            }
        }

        return totalHolidays;
    }

    protected LocalDate getHolidayDate(Holiday holiday, int year) throws ToolManagementException{
        LocalDate holidayDate = LocalDate.of(year, holiday.getMonth(), holiday.getDay());
        switch(holiday.getType().toLowerCase()) {
            case "closest-weekday":
                //Weekday holidays are always the closest weekday to the actual holiday
                if (holidayDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) return holidayDate.minusDays(1);
                if (holidayDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) return holidayDate.plusDays(1);
                return holidayDate;
            case "next-monday":
                //Next Monday holidays are always the Monday following the date
                while (!holidayDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                    holidayDate = holidayDate.plusDays(1);
                }
                return holidayDate;
            default:
                throw new ToolManagementException(ErrorCode.DATE0001, holiday.toString());
        }
    }

    /**
     * Get a list of each unique year between two dates
     * @param firstDate
     * @param lastDate
     * @return
     */
    protected List<Integer> getYearsBetweenTwoDates(
            LocalDate firstDate, LocalDate lastDate) {
        if (firstDate.getYear() == lastDate.getYear()) return List.of(firstDate.getYear());
        int year = firstDate.getYear();
        List<Integer> years = new ArrayList<>();
        do {
            years.add(year++);
        } while (year <= lastDate.getYear());
        return years;
    }
}
