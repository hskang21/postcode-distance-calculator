package com.calculator.postcode.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class GeographicalDistanceSearchRequest {

    private BigInteger id;
    private String srcPostcode;
    private String destPostcode;

}
