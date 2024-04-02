package com.calculator.postcode.model;

import com.calculator.postcode.constant.GeographicalConstant;
import com.calculator.postcode.entity.DistancesEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Slf4j
@Data
@NoArgsConstructor
public class PostcodeDistance {

    private BigInteger id;
    private String srcPostcode;
    private BigDecimal srcLatitude;
    private BigDecimal srcLongitude;
    private String destPostcode;
    private BigDecimal destLatitude;
    private BigDecimal destLongitude;
    private BigDecimal distance;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public PostcodeDistance(DistancesEntity entity) {
        this.id = entity.getId();
        this.srcPostcode = entity.getSrcPostcode();
        this.srcLatitude = entity.getSrcLatitude();
        this.srcLongitude = entity.getSrcLongitude();
        this.destPostcode = entity.getDestPostcode();
        this.destLatitude = entity.getDestLatitude();
        this.destLongitude = entity.getDestLongitude();
        this.distance = entity.getDistance().setScale(GeographicalConstant.DEFAULT_DISTANCE_SCALE, GeographicalConstant.DEFAULT_DISTANCE_ROUNDING_MODE);
        this.createdDate = entity.getCreatedDate();
        this.updatedDate = entity.getUpdatedDate();
    }

}
