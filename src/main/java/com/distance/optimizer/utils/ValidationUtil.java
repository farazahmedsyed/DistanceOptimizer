package com.distance.optimizer.utils;

import java.util.Objects;

public class ValidationUtil {

    private ValidationUtil() {
        throw new AssertionError("constructor not allowed");
    }

    public static Boolean isValidLatLngString(String loc) {
        String[] arr = loc.split(",");
        if (arr.length == 2) {
            if (isDoubleAndValidLocation(arr[0]) && isDoubleAndValidLocation(arr[1])) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static boolean isDoubleAndValidLocation(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void requireNonNull(Object... objects) {
        Objects.requireNonNull(objects);
        for (Object object : objects) {
            Objects.requireNonNull(object);
        }
    }

    public static void requireNull(Object object) {
        if (object != null){
            throw new RuntimeException("object is not null");
        }
    }

}
