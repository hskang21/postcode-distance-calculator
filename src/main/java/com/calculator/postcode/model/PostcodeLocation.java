package com.calculator.postcode.model;

import com.calculator.postcode.entity.PostcodesEntity;
import com.calculator.postcode.util.BigDecimalUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Data
@NoArgsConstructor
public class PostcodeLocation implements Diffable<PostcodeLocation> {

    private BigInteger id;
    private String postcode;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public PostcodeLocation(PostcodesEntity entity) {
        this.id = entity.getId();
        this.postcode = entity.getPostcode();
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
    }

    @Override
    public DiffResult<PostcodeLocation> diff(PostcodeLocation obj) {
        DiffBuilder<PostcodeLocation> db = new DiffBuilder<>(this, obj, ToStringStyle.JSON_STYLE)
                .append("postcode", this.getPostcode(), obj.getPostcode());
        if (BigDecimalUtil.isNotEqual(this.getLatitude(), obj.getLatitude())) {
            db.append("latitude", this.getLatitude(), obj.getLatitude());
        }
        if (BigDecimalUtil.isNotEqual(this.getLongitude(), obj.getLongitude())) {
            db.append("longitude", this.getLongitude(), obj.getLongitude());
        }
        return db.build();
    }

}
