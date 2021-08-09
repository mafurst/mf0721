package com.mfurst.mf0721.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used for storing rental agreement information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalAgreement {
    /**
     * Tool code as entered by user
     */
    private String toolCode;
    /**
     * Tool type from tool information
     */
    private String toolType;
    /**
     * Tool brand from tool information
     */
    private String toolBrand;
    /**
     * Amount of days of rental as entered by user
     */
    private int amountOfRentalDays;
    /**
     * Formatted check out date as provided by user
     */
    private String formattedCheckOutDate;
    /**
     * Formatted due date calculated from check out day and amount of rental days
     */
    private String formattedDueDate;
    /**
     * Formatted charge amount per day from the tool type
     */
    private String formattedDailyChargeAmount;
    /**
     * Count of chargeable days from after checkout day through and including due date
     */
    private int chargeDays;
    /**
     * Formatted charge before discount is applied
     */
    private String formattedPreDiscountCharge;
    /**
     * Discount percent as entered by user
     */
    private int discountPercent;
    /**
     * Formatted calculated discount amount, rounded half up to the nearest cent
     */
    private String formattedDiscountAmount;
    /**
     * Formatted final charge amount
     */
    private String formattedFinalCharge;
}
