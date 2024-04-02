package com.calculator.postcode.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = DistancesEntity.TABLE_NAME)
public class DistancesEntity {

    public static final String TABLE_NAME = "DISTANCES";

    public static final String ID = "ID";
    public static final String SRC_POSTCODE = "SRC_POSTCODE";
    public static final String SRC_LATITUDE = "SRC_LATITUDE";
    public static final String SRC_LONGITUDE = "SRC_LONGITUDE";
    public static final String DEST_POSTCODE = "DEST_POSTCODE";
    public static final String DEST_LATITUDE = "DEST_LATITUDE";
    public static final String DEST_LONGITUDE = "DEST_LONGITUDE";
    public static final String DISTANCE = "DISTANCE";
    public static final String CREATED_DATE = "CREATED_DATE";
    public static final String UPDATED_DATE = "UPDATED_DATE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private BigInteger id;

    @Column(name = SRC_POSTCODE)
    private String srcPostcode;

    @Column(name = SRC_LATITUDE)
    private BigDecimal srcLatitude;

    @Column(name = SRC_LONGITUDE)
    private BigDecimal srcLongitude;

    @Column(name = DEST_POSTCODE)
    private String destPostcode;

    @Column(name = DEST_LATITUDE)
    private BigDecimal destLatitude;

    @Column(name = DEST_LONGITUDE)
    private BigDecimal destLongitude;

    @Column(name = DISTANCE)
    private BigDecimal distance;

    @Column(name = CREATED_DATE)
    private LocalDateTime createdDate;

    @Column(name = UPDATED_DATE)
    private LocalDateTime updatedDate;

}
