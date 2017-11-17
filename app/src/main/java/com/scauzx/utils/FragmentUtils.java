package com.scauzx.utils;

/**
 * Created by Administrator on 2017/11/17.
 */

public class FragmentUtils {

    public static boolean isEquals(Object o1, Object o2) {
        return o1.getClass().getName() == o2.getClass().getName();
    }
}
