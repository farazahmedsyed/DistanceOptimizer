package com.distance.optimizer.utils;

import java.io.Serializable;

/**
 * @author FarazAhmed
 */
public class ResponseHeader implements Serializable {

    private Boolean isError;
    private Integer statusCode;
    private String message;
    private String token;

    public ResponseHeader() {
    }

    public ResponseHeader(Boolean isError, Integer statusCode, String message) {
        this.isError = isError;
        this.statusCode = statusCode;
        this.message = message;
    }

    public ResponseHeader(Boolean isError, Integer statusCode, String message, String token) {
        this.isError = isError;
        this.statusCode = statusCode;
        this.message = message;
        this.token = token;
    }

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
