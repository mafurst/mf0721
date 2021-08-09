package com.mfurst.mf0721.service;

import com.mfurst.mf0721.exception.ErrorCode;
import com.mfurst.mf0721.exception.ToolManagementException;
import com.mfurst.mf0721.model.RentalAgreement;
import com.mfurst.mf0721.model.ToolInformation;
import com.mfurst.mf0721.model.dto.Holiday;
import com.mfurst.mf0721.model.dto.ToolType;
import com.mfurst.mf0721.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
class RentalServiceTest {

    private DateUtil dateUtil;

    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        dateUtil = new DateUtil();
        ReflectionTestUtils.setField(dateUtil,"holidays", List.of(
                new Holiday("next-monday", 9, 1),
                new Holiday("closest-weekday", 7, 4)
        ));

        rentalService = new RentalService();
        ReflectionTestUtils.setField(rentalService, "dateUtil", dateUtil);
        ReflectionTestUtils.setField(rentalService, "dateFormat", "MM/dd/yy");
        ReflectionTestUtils.setField(rentalService, "currencyFormat", "$#,##0.00");
    }

    /**
     * Validate that we fail if rental period is zero days
     */
    @Test
    void validateRentalDaysShouldFailForZeroDays() {
        try {
            rentalService.validateRentalDays(0);
            fail("Rental service should fail if rental days is at or below 0");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.RENT0001, ex.getErrorCode());
        }
    }

    /**
     * Validate that we fail if rental period is negative
     */
    @Test
    void validateRentalDaysShouldFailForNegativeDays() {
        try {
            rentalService.validateRentalDays(-1);
            fail("Rental service should fail if rental days is at or below 0");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.RENT0001, ex.getErrorCode());
        }
    }

    /**
     * Validate that we fail if rental period is minimum days
     */
    @Test
    void validateRentalDaysShouldFailForMinimumDays() {
        try {
            rentalService.validateRentalDays(Integer.MIN_VALUE);
            fail("Rental service should fail if rental days is at or below 0");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.RENT0001, ex.getErrorCode());
        }
    }

    /**
     * Validate that ww do not fail if 1 day
     */
    @Test
    void validateRentalDaysShouldNotFailForOneDay() {
        try {
            rentalService.validateRentalDays(1);
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Validate that ww do not fail if maximum days
     */
    @Test
    void validateRentalDaysShouldNotFailForMaximumDays() {
        try {
            rentalService.validateRentalDays(Integer.MAX_VALUE);
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Verifying discount percentage should fail when less than zero
     */
    @Test
    void validateDiscountPercentageShouldFailIfLessThanZero() {
        try {
            rentalService.validateDiscountPercentage(-1);
            fail("Discount percentage should fail if below zero");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.RENT0003, ex.getErrorCode());
        }
    }

    /**
     * Verifying discount percentage should fail when minimum value
     */
    @Test
    void validateDiscountPercentageShouldFailIfAtMinimum() {
        try {
            rentalService.validateDiscountPercentage(Integer.MIN_VALUE);
            fail("Discount percentage should fail if below zero");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.RENT0003, ex.getErrorCode());
        }
    }

    /**
     * Verifying discount percentage should fail when greater than one-hundred
     */
    @Test
    void validateDiscountPercentageShouldFailIfGreaterThanOneHundred() {
        try {
            rentalService.validateDiscountPercentage(101);
            fail("Discount percentage should fail if greater than 100");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.RENT0004, ex.getErrorCode());
        }
    }

    /**
     * Verifying discount percentage should fail when greater than one-hundred
     */
    @Test
    void validateDiscountPercentageShouldFailIfAtMaximumValue() {
        try {
            rentalService.validateDiscountPercentage(Integer.MAX_VALUE);
            fail("Discount percentage should fail if greater than 100");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.RENT0004, ex.getErrorCode());
        }
    }

    /**
     * Verifying discount percentage should not fail when at zero
     */
    @Test
    void validateDiscountPercentageShouldNotFailIfAtZero() {
        try {
            rentalService.validateDiscountPercentage(0);
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Verifying discount percentage should not fail when at one-hundred
     */
    @Test
    void validateDiscountPercentageShouldNotFailIfAtOneHundred() {
        try {
            rentalService.validateDiscountPercentage(100);
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Verifying discount percentage should not fail when between zero and one-hundred
     */
    @Test
    void validateDiscountPercentageShouldNotFailIfBetweenZeroAndOneHundred() {
        try {
            rentalService.validateDiscountPercentage(1);
            rentalService.validateDiscountPercentage(50);
            rentalService.validateDiscountPercentage(99);
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test that incorrectly formatted dates fail parsing
     */
    @Test
    void parseRentalDateShouldFailForIncorrectlyFormattedDate() {
        try {
            rentalService.parseRentalDate("010101");
            fail("Parsing should fail for incorrectly formatted dates");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.RENT0002, ex.getErrorCode());
        }
    }

    /**
     * Test that correctly formatted dates parse correctly
     */
    @Test
    void parseRentalDateShouldParseCorrectlyFormattedDates() {
        try {
            LocalDate date = rentalService.parseRentalDate("01/01/01");
            assertTrue(LocalDate.of(2001,1,1).isEqual(date));
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test that rental agreements are properly calculated
     * when charging all days
     */
    @Test
    void calculateRentalAgreementWhenChargeForAllDays() {
        try {
            RentalAgreement expected = RentalAgreement.builder()
                    .amountOfRentalDays(31)
                    .chargeDays(31)
                    .discountPercent(10)
                    .formattedCheckOutDate("07/01/21")
                    .formattedDailyChargeAmount("$1.00")
                    .formattedDiscountAmount("$3.10")
                    .formattedFinalCharge("$27.90")
                    .formattedDueDate("08/01/21")
                    .formattedPreDiscountCharge("$31.00")
                    .toolBrand("Werner")
                    .toolCode("LADW")
                    .toolType("Ladder")
                    .build();
            RentalAgreement actual = rentalService.calculateRentalAgreement(
                    new ToolInformation("LADW", "Werner",
                            new ToolType(
                                "Ladder", BigDecimal.valueOf(1), true, true, true
                            )),
                    LocalDate.of(21, 7, 1), 31, 10
            );
            assertTrue(expected.equals(actual));
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test that rental agreements are properly calculated
     * when charging just weekdays
     */
    @Test
    void calculateRentalAgreementWhenChargeForWeekdays() {
        try {
            RentalAgreement expected = RentalAgreement.builder()
                    .amountOfRentalDays(31)
                    .chargeDays(20) //July of 2021 has 20 days that are not weekends or holidays
                    .discountPercent(0)
                    .formattedCheckOutDate("07/01/21")
                    .formattedDailyChargeAmount("$1.00")
                    .formattedDiscountAmount("$0.00")
                    .formattedFinalCharge("$20.00")
                    .formattedDueDate("08/01/21")
                    .formattedPreDiscountCharge("$20.00")
                    .toolBrand("Werner")
                    .toolCode("LADW")
                    .toolType("Ladder")
                    .build();
            RentalAgreement actual = rentalService.calculateRentalAgreement(
                    new ToolInformation("LADW", "Werner",
                            new ToolType(
                                "Ladder", BigDecimal.valueOf(1), true, false, false
                            )),
                    LocalDate.of(21, 7, 1), 31, 0
            );
            assertTrue(expected.equals(actual));
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test that rental agreements are properly calculated
     * when charging just weekends
     */
    @Test
    void calculateRentalAgreementWhenChargeForWeekends() {
        try {
            RentalAgreement expected = RentalAgreement.builder()
                    .amountOfRentalDays(31)
                    .chargeDays(10) //July of 2021 has 10 weekends
                    .discountPercent(37)
                    .formattedCheckOutDate("07/01/21")
                    .formattedDailyChargeAmount("$1.00")
                    .formattedDiscountAmount("$3.70")
                    .formattedFinalCharge("$6.30")
                    .formattedDueDate("08/01/21")
                    .formattedPreDiscountCharge("$10.00")
                    .toolBrand("Werner")
                    .toolCode("LADW")
                    .toolType("Ladder")
                    .build();
            RentalAgreement actual = rentalService.calculateRentalAgreement(
                    new ToolInformation("LADW", "Werner",
                            new ToolType(
                                "Ladder", BigDecimal.valueOf(1), false, true, false
                            )),
                    LocalDate.of(21, 7, 1), 31, 37
            );
            assertTrue(expected.equals(actual));
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test that rental agreements are properly calculated
     * when charging just holidays
     */
    @Test
    void calculateRentalAgreementWhenChargeForHolidays() {
        try {
            RentalAgreement expected = RentalAgreement.builder()
                    .amountOfRentalDays(31)
                    .chargeDays(1) //July of 2021 has 1 holidays
                    .discountPercent(47)
                    .formattedCheckOutDate("07/01/21")
                    .formattedDailyChargeAmount("$1.98")
                    .formattedDiscountAmount("$0.93")
                    .formattedFinalCharge("$1.05")
                    .formattedDueDate("08/01/21")
                    .formattedPreDiscountCharge("$1.98")
                    .toolBrand("Werner")
                    .toolCode("LADW")
                    .toolType("Ladder")
                    .build();
            RentalAgreement actual = rentalService.calculateRentalAgreement(
                    new ToolInformation("LADW", "Werner",
                            new ToolType(
                                "Ladder", BigDecimal.valueOf(1.98), false, false, true
                            )),
                    LocalDate.of(21, 7, 1), 31, 47
            );
            assertTrue(expected.equals(actual));
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }
}