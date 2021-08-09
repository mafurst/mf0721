package com.mfurst.mf0721.exception;

/**
 * Custom class for ErrorCodes pertaining to errors
 * that are encountered during application runtime
 */
public enum ErrorCode {
    TOOL0001("You must enter a tool code."),
    TOOL0002("Provided tool code [%s] is not valid."),
    TOOL0003("Tool with code [%s] does not have a valid type."),
    RENT0001("Rental periods must be at least 1 day. Please try again with a valid rental period."),
    RENT0002("Rental dates must match the format of mm/dd/yy. Please try again with a date that is formatted correctly."),
    RENT0003("Discount rate cannot be less than zero percent."),
    RENT0004("Discount rate cannot be greater than one-hundred percent."),
    DATE0001("Invalid holiday [%s].");
    private String message;

    /**
     * Default constructor for the enum.
     * @param message
     */
    private ErrorCode(String message) {
        this.message = message;
    }

    /**
     * Get the message that is associated with this error code.
     * Messages may contain format specifiers to allow for communicating
     * the cause of the related exception.
     * @return
     */
    public String getMessage() {
        return this.message;
    }
    public String toString() {
        return String.format("%s: %s", this.name(), this.message);
    }
}
