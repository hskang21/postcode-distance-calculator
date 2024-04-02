package com.calculator.postcode.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Entity
@Table(name = PostcodesEntity.TABLE_NAME)
public class PostcodesEntity {

    public static final String TABLE_NAME = "POSTCODES";

    public static final String ID = "ID";
    public static final String POSTCODE = "POSTCODE";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    @Id
    @Column(name = ID)
    private BigInteger id;

    @Column(name = POSTCODE)
    private String postcode;

    @Column(name = LATITUDE)
    private BigDecimal latitude;

    @Column(name = LONGITUDE)
    private BigDecimal longitude;

}
