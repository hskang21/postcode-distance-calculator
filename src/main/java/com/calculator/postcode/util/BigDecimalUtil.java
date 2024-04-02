package com.calculator.postcode.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

    private BigDecimalUtil() {
    }

    public static boolean isNotEqual(BigDecimal left, BigDecimal right) {
        return !isEqual(left, right);
    }

    public static boolean isEqual(BigDecimal left, BigDecimal right) {
        if (left == null && right == null) {
            return true;
        }
        if (left != null && right != null) {
            return left.compareTo(right) == 0;
        }
        return false;
    }

}
