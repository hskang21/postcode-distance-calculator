package com.calculator.postcode.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeographicalError implements BaseError {

    SOURCE_DESTINATION_POSTCODE_IS_MANDATORY(1000, "Invalid Source and Destination Postcode"),
    INVALID_SOURCE_DESTINATION_POSTCODE(1001, "Invalid Source and Destination Postcode"),
    INVALID_SOURCE_POSTCODE(1002, "Invalid Source Postcode"),
    INVALID_DESTINATION_POSTCODE(1003, "Invalid Destination Postcode"),
    INCOMPLETE_SOURCE_POSTCODE(1004, "Incomplete Source Postcode Details"),
    INCOMPLETE_DESTINATION_POSTCODE(1005, "Invalid Destination Postcode Details"),

    INVALID_POSTCODE_ID(2000, "Invalid Postcode ID"),
    POSTCODE_NOT_FOUND(2001, "Postcode Not Found"),
    POSTCODE_IS_MANDATORY(2002, "Postcode is mandatory"),
    LATITUDE_IS_MANDATORY(2003, "Latitude is mandatory"),
    LONGITUDE_IS_MANDATORY(2004, "Longitude is mandatory"),

    GENERAL_ERROR(9999, "General error"),
    ;

    private final int code;
    private final String errorMessage;
    private final String errorPrefix = "GE";

}
