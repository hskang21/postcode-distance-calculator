package com.calculator.postcode.api;

import com.calculator.postcode.exception.GeographicalException;
import com.calculator.postcode.model.GeographicalDistanceRequest;
import com.calculator.postcode.model.GeographicalDistanceResponse;
import com.calculator.postcode.model.GeographicalDistanceSearchRequest;
import com.calculator.postcode.model.GeographicalSearchRequest;
import com.calculator.postcode.model.PostcodeDistance;
import com.calculator.postcode.model.PostcodeLocation;
import com.calculator.postcode.model.ResponseWrapper;
import com.calculator.postcode.service.GeographicalCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/geographical/v1")
public class GeographicalRestController {

    private final GeographicalCalculatorService geographicalCalculatorService;

    @PostMapping("/list")
    public ResponseWrapper<Page<PostcodeLocation>> getPostcodeLocationList(@RequestBody GeographicalSearchRequest request, Pageable pageable) {
        log.info("Get Postcode Location List");
        return new ResponseWrapper<Page<PostcodeLocation>>().success(geographicalCalculatorService.getPostcodeLocationList(request, pageable));
    }

    @PostMapping("/distance/list")
    public ResponseWrapper<Page<PostcodeDistance>> getPostcodeDistanceList(@RequestBody GeographicalDistanceSearchRequest request, Pageable pageable) {
        log.info("Get Postcode Distance List");
        return new ResponseWrapper<Page<PostcodeDistance>>().success(geographicalCalculatorService.getPostcodeDistanceList(request, pageable));
    }

    @PostMapping("/update")
    public ResponseWrapper<Boolean> updatePostcodeLocation(@RequestBody PostcodeLocation request) throws GeographicalException {
        log.info("Update Postcode Location");
        return new ResponseWrapper<Boolean>().success(geographicalCalculatorService.updatePostcodeLocation(request));
    }

    @PostMapping("/calculate")
    public ResponseWrapper<GeographicalDistanceResponse> calculateDistance(@RequestBody GeographicalDistanceRequest request) throws GeographicalException {
        log.info("Calculate Distance");
        return new ResponseWrapper<GeographicalDistanceResponse>().success(geographicalCalculatorService.calculateDistance(request));
    }

}
