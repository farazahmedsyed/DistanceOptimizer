package com.distance.optimizer.utils;

/**
 * @author FarazAhmed
 */
public interface StatusCodeEnum<E extends Enum<E>> {
    int getCode();
    String getMessage();
}
