package com.distance.optimizer.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author FarazAhmed
 */
public class EntityHelper {

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNotNull(Object o) {
        return !isNull(o);
    }

    public static Boolean isListPopulated(List<?> list) {
        return list != null && !list.isEmpty();
    }

    public static Boolean isListNotPopulated(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static Boolean isSetPopulated(Set<?> set) {
        return set != null && !set.isEmpty();
    }

    public static Boolean isListPopulatedNotNull(List<?> list) {
        list.removeAll(Collections.singleton(null));
        return list != null && !list.isEmpty();
    }

    public static boolean isStringSet(String str) {
        if ((str != null) && !str.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isStringNotSet(String str) {
        if ((str == null) || str.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isIdSet(Integer... ids) {
        if (ids == null){
            return false;
        }
        for (Integer id : ids) {
            if ( !((id != null) && (id > 0)) )
                return false;
        }

        return true;
    }

    public static <T> List<T> safeList(List<T> list) {
        return list == null ? Arrays.<T>asList() : list;
    }
}
