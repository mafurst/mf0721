package com.mfurst.mf0721.service;

import com.mfurst.mf0721.exception.ErrorCode;
import com.mfurst.mf0721.exception.ToolManagementException;
import com.mfurst.mf0721.model.RentalAgreement;
import com.mfurst.mf0721.model.ToolInformation;
import com.mfurst.mf0721.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This service is used for rental logic, such as validating rental information
 * or calculating rental amounts
 */
@Service
public class RentalService {

    @Autowired
    private DateUtil dateUtil;

    @Value("${application.formatting.date}")
    private String dateFormat;

    @Value("${application.formatting.currency}")
    private String currencyFormat;

    /**
     * Validate the amount of days of a rental
     * @param nDays
     * @throws ToolManagementException if rental period is not valid
     */
    public void validateRentalDays(int nDays) throws ToolManagementException{
        if (nDays < 1) throw new ToolManagementException(ErrorCode.RENT0001);
    }

    /**
     * Validate the discount percentage
     * @param percentage
     * @throws ToolManagementException
     */
    public void validateDiscountPercentage(int percentage) throws ToolManagementException {
        if (percentage < 0) throw new ToolManagementException(ErrorCode.RENT0003);
        if (percentage > 100) throw new ToolManagementException(ErrorCode.RENT0004);
    }

    /**
     * Convert a rental date string to a date object. This will also validate
     * if it is in the correct format.
     * @param rentalDateString
     * @throws ToolManagementException if string is not a valid date in mm/dd/yy format
     * @return
     */
    public LocalDate parseRentalDate(String rentalDateString) throws ToolManagementException {
        try {
            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern(dateFormat);
            return LocalDate.parse(rentalDateString, formatter);
        } catch (DateTimeParseException ex) {
            throw new ToolManagementException(ErrorCode.RENT0002);
        }
    }

    /**
     * Generate a rental agreement from user provided information
     * @param toolInfo information about the tool
     * @param checkoutDate checkout date as provided by user
     * @param rentalPeriod amount of days for the rental
     * @param discountPercentage discount percentage
     * @throws ToolManagementException if any data is invalid
     * @return
     */
    public RentalAgreement calculateRentalAgreement(ToolInformation toolInfo,
                        LocalDate checkoutDate, int rentalPeriod, int discountPercentage
    ) throws ToolManagementException{
        DateTimeFormatter dateFormatter =  DateTimeFormatter.ofPattern(dateFormat);
        DecimalFormat currencyFormatter = new DecimalFormat(currencyFormat);
        //Due date is checkout date plus rental period
        LocalDate dueDate = checkoutDate.plusDays(rentalPeriod);
        int weekendsBetweenDates = dateUtil.getNumberOfWeekendsBetweenTwoDates(checkoutDate, dueDate);
        int holidaysBetweenDates = dateUtil.getNumberOfHolidaysBetweenTwoDates(checkoutDate, dueDate);
        //Weekdays is any days that are not weekends or holidays in rental period
        int weekdays = rentalPeriod - weekendsBetweenDates - holidaysBetweenDates;

        int chargedRentalDays = 0;
        if (toolInfo.getToolType().isWeekday()) {
            chargedRentalDays += weekdays;
        }
        if (toolInfo.getToolType().isWeekend()) {
            chargedRentalDays += weekendsBetweenDates;
        }
        if (toolInfo.getToolType().isHoliday()) {
            chargedRentalDays += holidaysBetweenDates;
        }

        BigDecimal preDiscountCharge = toolInfo.getToolType().getCharge()
                .multiply(BigDecimal.valueOf(chargedRentalDays))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal discountAmount = preDiscountCharge
                .multiply(BigDecimal.valueOf(discountPercentage))
                .divide(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount)
                .setScale(2, RoundingMode.HALF_UP);

        RentalAgreement agreement = new RentalAgreement();
        agreement.setToolCode(toolInfo.getToolCode());
        agreement.setToolBrand(toolInfo.getToolBrand());
        agreement.setToolType(toolInfo.getToolType().getType());
        agreement.setFormattedCheckOutDate(dateFormatter.format(checkoutDate));
        agreement.setAmountOfRentalDays(rentalPeriod);
        agreement.setDiscountPercent(discountPercentage);
        agreement.setFormattedDueDate(dateFormatter.format(dueDate));
        agreement.setChargeDays(chargedRentalDays);
        agreement.setFormattedDailyChargeAmount(currencyFormatter.format(toolInfo.getToolType().getCharge()));
        agreement.setFormattedPreDiscountCharge(currencyFormatter.format(preDiscountCharge));
        agreement.setFormattedDiscountAmount(currencyFormatter.format(discountAmount));
        agreement.setFormattedFinalCharge(currencyFormatter.format(finalCharge));

        return agreement;
    }
}