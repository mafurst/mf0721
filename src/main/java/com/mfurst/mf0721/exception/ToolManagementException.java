package com.mfurst.mf0721.exception;

/**
 * This class is a custom exception class for handling exceptions
 * from the tool inventory management.
 */
public class ToolManagementException extends Exception {
    private final ErrorCode errorCode;
    private Object[] errorArguments;

    /**
     * Create a new exception for something related to tool management
     * @param errorCode Error code that identifies the exception
     * @param args any related arguments to the error that will appear in the error message
     */
    public ToolManagementException(ErrorCode errorCode, Object... args) {
        super();
        this.errorCode = errorCode;
        this.errorArguments = args;
    }

    @Override
    public String getMessage() {
        return String.format(this.errorCode.getMessage(), errorArguments);
    }

    public ErrorCode getErrorCode() { return this.errorCode; }
}
