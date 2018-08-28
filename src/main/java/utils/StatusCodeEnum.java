package utils;

/**
 * Created by VenD on 8/24/2017.
 */
public interface StatusCodeEnum<E extends Enum<E>> {
    int getCode();
    String getMessage();
}
