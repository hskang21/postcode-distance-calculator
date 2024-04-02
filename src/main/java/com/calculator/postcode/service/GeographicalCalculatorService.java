package com.calculator.postcode.service;

import com.calculator.postcode.exception.GeographicalException;
import com.calculator.postcode.model.GeographicalDistanceRequest;
import com.calculator.postcode.model.GeographicalDistanceResponse;
import com.calculator.postcode.model.GeographicalDistanceSearchRequest;
import com.calculator.postcode.model.GeographicalSearchRequest;
import com.calculator.postcode.model.PostcodeDistance;
import com.calculator.postcode.model.PostcodeLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeographicalCalculatorService {

    Page<PostcodeLocation> getPostcodeLocationList(GeographicalSearchRequest request, Pageable pageable);

    Page<PostcodeDistance> getPostcodeDistanceList(GeographicalDistanceSearchRequest request, Pageable pageable);

    boolean updatePostcodeLocation(PostcodeLocation location) throws GeographicalException;

    GeographicalDistanceResponse calculateDistance(GeographicalDistanceRequest request) throws GeographicalException;

}
