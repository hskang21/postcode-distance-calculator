package com.calculator.postcode.constant;

import java.math.RoundingMode;

public class GeographicalConstant {

    public static final double EARTH_RADIUS = 6371;
    public static final String DEFAULT_DISTANCE_UNIT = "km";
    public static final int DEFAULT_DISTANCE_SCALE = 2;
    public static final RoundingMode DEFAULT_DISTANCE_ROUNDING_MODE = RoundingMode.HALF_UP;

    private GeographicalConstant() {
    }

}
