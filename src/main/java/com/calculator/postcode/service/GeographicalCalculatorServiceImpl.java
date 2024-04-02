package com.calculator.postcode.service;

import com.calculator.postcode.constant.GeographicalConstant;
import com.calculator.postcode.dao.DistancesDao;
import com.calculator.postcode.dao.PostcodesDao;
import com.calculator.postcode.entity.DistancesEntity;
import com.calculator.postcode.entity.PostcodesEntity;
import com.calculator.postcode.error.GeographicalError;
import com.calculator.postcode.exception.GeographicalException;
import com.calculator.postcode.model.GeographicalDistanceRequest;
import com.calculator.postcode.model.GeographicalDistanceResponse;
import com.calculator.postcode.model.GeographicalDistanceSearchRequest;
import com.calculator.postcode.model.GeographicalSearchRequest;
import com.calculator.postcode.model.PostcodeDistance;
import com.calculator.postcode.model.PostcodeLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeographicalCalculatorServiceImpl implements GeographicalCalculatorService {

    private final PostcodesDao postcodesDao;
    private final DistancesDao distancesDao;

    @Override
    public Page<PostcodeLocation> getPostcodeLocationList(GeographicalSearchRequest request, Pageable pageable) {
        log.info("[Geographical Calculator Service] -- Get Postcode Location List: {}", request);
        Specification<PostcodesEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(request.getId())) {
                predicates.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }
            if (StringUtils.isNotBlank(request.getPostcode())) {
                predicates.add(criteriaBuilder.equal(root.get("postcode"), request.getPostcode()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return postcodesDao.findAll(specification, pageable)
                .map(PostcodeLocation::new);
    }

    @Override
    public Page<PostcodeDistance> getPostcodeDistanceList(GeographicalDistanceSearchRequest request, Pageable pageable) {
        log.info("[Geographical Calculator Service] -- Get Postcode Distance List: {}", request);
        Specification<DistancesEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(request.getId())) {
                predicates.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }
            if (StringUtils.isNotBlank(request.getSrcPostcode())) {
                predicates.add(criteriaBuilder.equal(root.get("srcPostcode"), request.getSrcPostcode()));
            }
            if (StringUtils.isNotBlank(request.getDestPostcode())) {
                predicates.add(criteriaBuilder.equal(root.get("destPostcode"), request.getDestPostcode()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return distancesDao.findAll(specification, pageable)
                .map(PostcodeDistance::new);
    }

    @Override
    @Transactional
    public boolean updatePostcodeLocation(PostcodeLocation location) throws GeographicalException {
        if (ObjectUtils.isEmpty(location.getId())) {
            throw new GeographicalException(GeographicalError.INVALID_POSTCODE_ID);
        }

        if (StringUtils.isBlank(location.getPostcode())) {
            throw new GeographicalException(GeographicalError.POSTCODE_IS_MANDATORY);
        }

        if (ObjectUtils.isEmpty(location.getLatitude())) {
            throw new GeographicalException(GeographicalError.LATITUDE_IS_MANDATORY);
        }

        if (ObjectUtils.isEmpty(location.getLongitude())) {
            throw new GeographicalException(GeographicalError.LONGITUDE_IS_MANDATORY);
        }

        PostcodesEntity postcodesEntity = postcodesDao.findById(location.getId())
                .orElseThrow(() -> new GeographicalException(GeographicalError.POSTCODE_NOT_FOUND));

        PostcodeLocation before = new PostcodeLocation(postcodesEntity);
        DiffResult<PostcodeLocation> diffResult = before.diff(location);
        log.info("[Geographical Calculator Service] -- Diff Postcode Location: [{}] {}", diffResult.getNumberOfDiffs(), diffResult);
        if (diffResult.getNumberOfDiffs() == 0) {
            log.info("[Geographical Calculator Service] -- No changes detected in Postcode Location");
            return true;
        }
        postcodesEntity.setPostcode(location.getPostcode());
        postcodesEntity.setLatitude(location.getLatitude());
        postcodesEntity.setLongitude(location.getLongitude());
        postcodesDao.save(postcodesEntity);
        return true;
    }

    @Override
    @Transactional
    public GeographicalDistanceResponse calculateDistance(GeographicalDistanceRequest request) throws GeographicalException {
        String sourcePostcode = request.getSourcePostcode();
        String destinationPostcode = request.getDestinationPostcode();
        if (StringUtils.isAnyBlank(sourcePostcode, destinationPostcode)) {
            throw new GeographicalException(GeographicalError.SOURCE_DESTINATION_POSTCODE_IS_MANDATORY);
        }

        List<PostcodesEntity> postcodes = postcodesDao.findByPostcodeIn(Arrays.asList(sourcePostcode, destinationPostcode));

        if (CollectionUtils.isEmpty(postcodes)) {
            throw new GeographicalException(GeographicalError.INVALID_SOURCE_DESTINATION_POSTCODE);
        }

        Map<String, PostcodesEntity> postcodeMap = postcodes.stream()
                .collect(Collectors.toMap(PostcodesEntity::getPostcode, Function.identity()));

        if (!postcodeMap.containsKey(sourcePostcode)) {
            throw new GeographicalException(GeographicalError.INVALID_SOURCE_POSTCODE);
        }

        if (!postcodeMap.containsKey(destinationPostcode)) {
            throw new GeographicalException(GeographicalError.INVALID_DESTINATION_POSTCODE);
        }

        PostcodesEntity sourcePostcodeEntity = postcodeMap.get(sourcePostcode);

        if (ObjectUtils.isEmpty(sourcePostcodeEntity.getLatitude()) || ObjectUtils.isEmpty(sourcePostcodeEntity.getLongitude())) {
            throw new GeographicalException(GeographicalError.INCOMPLETE_SOURCE_POSTCODE);
        }

        PostcodesEntity destinationPostcodeEntity = postcodeMap.get(destinationPostcode);

        if (ObjectUtils.isEmpty(destinationPostcodeEntity.getLatitude()) || ObjectUtils.isEmpty(destinationPostcodeEntity.getLongitude())) {
            throw new GeographicalException(GeographicalError.INCOMPLETE_DESTINATION_POSTCODE);
        }

        BigDecimal distance = BigDecimal.valueOf(
                calculateDistance(
                        sourcePostcodeEntity.getLatitude().doubleValue(),
                        sourcePostcodeEntity.getLongitude().doubleValue(),
                        destinationPostcodeEntity.getLatitude().doubleValue(),
                        destinationPostcodeEntity.getLongitude().doubleValue()
                )
        ).setScale(GeographicalConstant.DEFAULT_DISTANCE_SCALE, GeographicalConstant.DEFAULT_DISTANCE_ROUNDING_MODE);

        insertPostcodeDistanceRecord(sourcePostcodeEntity, destinationPostcodeEntity, distance);

        return new GeographicalDistanceResponse(
                new PostcodeLocation(sourcePostcodeEntity),
                new PostcodeLocation(destinationPostcodeEntity),
                distance, GeographicalConstant.DEFAULT_DISTANCE_UNIT);
    }

    private void insertPostcodeDistanceRecord(PostcodesEntity sourcePostcodeEntity, PostcodesEntity destinationPostcodeEntity, BigDecimal distance) {
        DistancesEntity distancesEntity = new DistancesEntity();
        distancesEntity.setSrcPostcode(sourcePostcodeEntity.getPostcode());
        distancesEntity.setSrcLatitude(sourcePostcodeEntity.getLatitude());
        distancesEntity.setSrcLongitude(sourcePostcodeEntity.getLongitude());
        distancesEntity.setDestPostcode(destinationPostcodeEntity.getPostcode());
        distancesEntity.setDestLatitude(destinationPostcodeEntity.getLatitude());
        distancesEntity.setDestLongitude(destinationPostcodeEntity.getLongitude());
        distancesEntity.setDistance(distance);
        LocalDateTime now = LocalDateTime.now();
        distancesEntity.setCreatedDate(now);
        distancesEntity.setUpdatedDate(now);
        distancesDao.save(distancesEntity);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lon1Radians = Math.toRadians(lon1);
        double lon2Radians = Math.toRadians(lon2);
        double lat1Radians = Math.toRadians(lat1);
        double lat2Radians = Math.toRadians(lat2);
        double a = haversine(lat1Radians, lat2Radians) + Math.cos(lat1Radians) * Math.cos(lat2Radians) * haversine(lon1Radians, lon2Radians);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (GeographicalConstant.EARTH_RADIUS * c);
    }

    private double haversine(double degree1, double degree2) {
        double diff = degree1 - degree2;
        return square(Math.sin(diff / 2));
    }

    private double square(double value) {
        return Math.pow(value, 2);
    }

}
