package exception;

import utils.StatusCodes;

/**
 * Created by VenD on 8/24/2017.
 */
public class ROException extends Exception {

    private static final Integer DEFAULT_ERROR_CODE = StatusCodes.FAILURE.getCode();
    private Integer code;

    public ROException() {
        super();
        this.code = DEFAULT_ERROR_CODE;
    }

    public ROException(String message) {
        super(message);
        this.code = DEFAULT_ERROR_CODE;
    }

    public ROException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT_ERROR_CODE;
    }

    public ROException(Throwable cause) {
        super(cause);
        this.code = DEFAULT_ERROR_CODE;
    }

    public ROException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = DEFAULT_ERROR_CODE;
    }

    public ROException(Integer code) {
        this.code = code;
    }

    public ROException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ROException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ROException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public ROException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public ROException(StatusCodes e){
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
