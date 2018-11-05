package com.distance.optimizer.utils;

/**
 * @author FarazAhmed
 */
public enum StatusCodes implements StatusCodeEnum<StatusCodes> {
    FAILURE(0, "Failure"),
    SUCCESS(1, "Success"),
    NO_RECORD_FOUND(6, "No Record Found."),
    USER_NOT_AUTHENTICATED(7, "User not Authenticated."),
    FAILED_TO_GET_API_RESPONSE(10, "Failed to Get API Response."),


    BAD_REQUEST(400, "Bad Request");

    private int code;
    private String message;

    StatusCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
