package com.distance.optimizer.exception;

import com.distance.optimizer.utils.StatusCodes;

/**
 * @author FarazAhmed
 */
public class DistanceOptimizerException extends Exception {

    private static final Integer DEFAULT_ERROR_CODE = StatusCodes.FAILURE.getCode();
    private Integer code;

    public DistanceOptimizerException() {
        super();
        this.code = DEFAULT_ERROR_CODE;
    }

    public DistanceOptimizerException(String message) {
        super(message);
        this.code = DEFAULT_ERROR_CODE;
    }

    public DistanceOptimizerException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT_ERROR_CODE;
    }

    public DistanceOptimizerException(Throwable cause) {
        super(cause);
        this.code = DEFAULT_ERROR_CODE;
    }

    public DistanceOptimizerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = DEFAULT_ERROR_CODE;
    }

    public DistanceOptimizerException(Integer code) {
        this.code = code;
    }

    public DistanceOptimizerException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public DistanceOptimizerException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public DistanceOptimizerException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public DistanceOptimizerException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public DistanceOptimizerException(StatusCodes e){
        super(e.getMessage());
        this.code = e.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
