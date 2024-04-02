package com.calculator.postcode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeographicalDistanceResponse {

    private PostcodeLocation source;
    private PostcodeLocation destination;
    private BigDecimal distance;
    private String unit;

}
