package com.calculator.postcode.error;

public interface BaseError {

    String getErrorPrefix();

    int getCode();

    String getErrorMessage();

    default String getErrorCode() {
        return getErrorPrefix() + getCode();
    }

}
