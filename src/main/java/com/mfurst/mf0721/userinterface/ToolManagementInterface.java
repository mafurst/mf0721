package com.mfurst.mf0721.userinterface;

import com.mfurst.mf0721.exception.ToolManagementException;
import com.mfurst.mf0721.model.RentalAgreement;
import com.mfurst.mf0721.model.ToolInformation;
import com.mfurst.mf0721.service.RentalService;
import com.mfurst.mf0721.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This is a User Interface for tool management
 */
@Component
public class ToolManagementInterface {
    protected static final String RECEIPT_MESSAGE =
            "################################\n###     Rental Agreement     ###\n################################\n" +
            "Tool Code: %s\nTool Type: %s\nTool Brand: %s\nAmount of Days for Rental: %s\nCheckout Date: %s\nDue Date: %s\n" +
            "Daily Rental Charge: %s\nAmount of days charge is applied: %d\nCharge Before Any Discounts: %s\nDiscount Percentage: %d%%\n" +
            "Discount Amount: %s\nFinal Charge Amount: %s";

    @Autowired
    private ToolService toolService;
    @Autowired
    private RentalService rentalService;

    /**
     * Run the interface
     * @param outputStream The stream for printing messages to the user
     * @param inputStream The stream for reading input from the user
     * @param errorStream The stream for printing error messages to the user
     */
    public void run(PrintStream outputStream, InputStream inputStream, PrintStream errorStream) {
        Scanner inputScanner = new Scanner(inputStream);
        try {
            //Get tool information by asking user for tool code
            outputStream.print("Enter tool code to search by: ");
            String toolCode = inputScanner.next();
            ToolInformation toolInformation = toolService.getToolByCode(toolCode);

            //Get checkout date from the user
            outputStream.print("Enter date of rental (mm/dd/yy): ");
            String rawCheckoutDate = inputScanner.next();
            LocalDate checkoutDate = rentalService.parseRentalDate(rawCheckoutDate);

            //Get rental period from the user
            outputStream.print("Enter amount of days customer will be renting for: ");
            int rentalPeriod = inputScanner.nextInt();
            rentalService.validateRentalDays(rentalPeriod);

            //Get discount percent from the user
            outputStream.print("Enter discount percentage [0-100]: ");
            int discountPercentage = inputScanner.nextInt();
            rentalService.validateDiscountPercentage(discountPercentage);

            //Generate rental agreement
            RentalAgreement agreement = rentalService.calculateRentalAgreement(
                    toolInformation, checkoutDate, rentalPeriod, discountPercentage
            );

            outputRentalAgreement(agreement, outputStream);
        } catch (ToolManagementException ex) {
            errorStream.println(ex.getMessage());
        } catch (InputMismatchException ex) {
            errorStream.println("The value you have entered is invalid. Please try again with a different value.");
        } catch (Exception ex) {
            errorStream.println(String.format("An unexpected error occurred. Please make note of the exception below and try again. %s", ex.getMessage()));
        }
    }

    /**
     * Output a rental agreement to the user on the provided output stream
     * @param rentalAgreement
     * @param outputStream
     */
    protected void outputRentalAgreement(RentalAgreement rentalAgreement, PrintStream outputStream) {
        outputStream.println(
                String.format(RECEIPT_MESSAGE,
                        rentalAgreement.getToolCode(),
                        rentalAgreement.getToolType(),
                        rentalAgreement.getToolBrand(),
                        rentalAgreement.getAmountOfRentalDays(),
                        rentalAgreement.getFormattedCheckOutDate(),
                        rentalAgreement.getFormattedDueDate(),
                        rentalAgreement.getFormattedDailyChargeAmount(),
                        rentalAgreement.getChargeDays(),
                        rentalAgreement.getFormattedPreDiscountCharge(),
                        rentalAgreement.getDiscountPercent(),
                        rentalAgreement.getFormattedDiscountAmount(),
                        rentalAgreement.getFormattedFinalCharge()
                        )
        );
    }
}
