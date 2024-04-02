package com.calculator.postcode.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class GeographicalSearchRequest {

    private BigInteger id;
    private String postcode;

}
