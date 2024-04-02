package com.calculator.postcode.model;

import lombok.Data;

@Data
public class GeographicalDistanceRequest {

    private String sourcePostcode;
    private String destinationPostcode;

}
