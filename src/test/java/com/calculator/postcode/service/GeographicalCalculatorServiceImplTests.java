package com.calculator.postcode.service;

import com.calculator.postcode.MyPostalCodeCalculatorApplication;
import com.calculator.postcode.dao.DistancesDao;
import com.calculator.postcode.dao.PostcodesDao;
import com.calculator.postcode.entity.DistancesEntity;
import com.calculator.postcode.entity.PostcodesEntity;
import com.calculator.postcode.error.GeographicalError;
import com.calculator.postcode.exception.GeographicalException;
import com.calculator.postcode.model.GeographicalDistanceRequest;
import com.calculator.postcode.model.GeographicalDistanceSearchRequest;
import com.calculator.postcode.model.GeographicalSearchRequest;
import com.calculator.postcode.model.PostcodeDistance;
import com.calculator.postcode.model.PostcodeLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Geographical Calculator Test")
@ContextConfiguration(classes = MyPostalCodeCalculatorApplication.class)
public class GeographicalCalculatorServiceImplTests {

    @Mock
    private PostcodesDao postcodesDao;

    @Mock
    private DistancesDao distancesDao;

    private GeographicalCalculatorServiceImpl geographicalCalculatorService;

    @BeforeEach
    public void setUp() {
        geographicalCalculatorService = Mockito.spy(new GeographicalCalculatorServiceImpl(postcodesDao, distancesDao));
    }

    @Test
    @DisplayName("Test getPostcodeLocationList")
    public void test_getPostcodeLocationList() {
        Pageable pageable = PageRequest.of(0, 5);
        List<PostcodesEntity> postcodesEntityList = new ArrayList<>();
        for (int i = 1; i <= pageable.getPageSize(); i++) {
            PostcodesEntity postcodesEntity = new PostcodesEntity();
            postcodesEntity.setId(BigInteger.valueOf(i));
            postcodesEntity.setPostcode("postcode" + i);
            postcodesEntity.setLatitude(BigDecimal.valueOf(53.753322 + (i / 100.0)));
            postcodesEntity.setLongitude(BigDecimal.valueOf(-2.450445 - (i / 100.0)));
            postcodesEntityList.add(postcodesEntity);
        }
        Page<PostcodesEntity> mockPostcodesPage = new PageImpl<>(postcodesEntityList, pageable, postcodesEntityList.size());
        Mockito.when(postcodesDao.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(mockPostcodesPage);
        Page<PostcodeLocation> postcodeLocationPage = geographicalCalculatorService.getPostcodeLocationList(new GeographicalSearchRequest(), pageable);
        postcodeLocationPage.forEach(postcodeLocation -> {
            Assertions.assertNotNull(postcodeLocation.getId());
            Assertions.assertNotNull(postcodeLocation.getPostcode());
            Assertions.assertNotNull(postcodeLocation.getLatitude());
            Assertions.assertNotNull(postcodeLocation.getLongitude());
        });
    }

    @Test
    @DisplayName("Test getPostcodeDistanceList")
    public void test_getPostcodeDistanceList() {
        Pageable pageable = PageRequest.of(0, 5);
        List<DistancesEntity> distancesEntityList = new ArrayList<>();
        for (int i = 1; i <= pageable.getPageSize(); i++) {
            DistancesEntity distanceEntity = new DistancesEntity();
            distanceEntity.setSrcPostcode("srcPostcode" + i);
            distanceEntity.setSrcLatitude(BigDecimal.valueOf(53.753322 + (i / 100.0)));
            distanceEntity.setSrcLongitude(BigDecimal.valueOf(-2.450445 - (i / 100.0)));
            distanceEntity.setDestPostcode("destPostcode" + i);
            distanceEntity.setDestLatitude(BigDecimal.valueOf(57.417971 + (i / 100.0)));
            distanceEntity.setDestLongitude(BigDecimal.valueOf(-6.211535 - (i / 100.0)));
            distanceEntity.setDistance(BigDecimal.valueOf(251.12 - (i / 100.0)));
            distancesEntityList.add(distanceEntity);
        }
        Page<DistancesEntity> postcodeDistancePage = new PageImpl<>(distancesEntityList, pageable, distancesEntityList.size());
        Mockito.when(distancesDao.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(postcodeDistancePage);
        Page<PostcodeDistance> postcodeDistancePageResult = geographicalCalculatorService.getPostcodeDistanceList(new GeographicalDistanceSearchRequest(), pageable);
        postcodeDistancePageResult.forEach(postcodeDistance -> {
            Assertions.assertNotNull(postcodeDistance.getSrcPostcode());
            Assertions.assertNotNull(postcodeDistance.getSrcLatitude());
            Assertions.assertNotNull(postcodeDistance.getSrcLongitude());
            Assertions.assertNotNull(postcodeDistance.getDestPostcode());
            Assertions.assertNotNull(postcodeDistance.getDestLatitude());
            Assertions.assertNotNull(postcodeDistance.getDestLongitude());
            Assertions.assertNotNull(postcodeDistance.getDistance());
        });
    }

    @Test
    @DisplayName("Test updatePostcodeLocation - validate postcode id")
    public void test_updatePostcodeLocation_validatePostcodeId() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            PostcodeLocation location = new PostcodeLocation();
            location.setId(null);
            location.setPostcode("postcode1");
            location.setLatitude(BigDecimal.valueOf(53.753322));
            location.setLongitude(BigDecimal.valueOf(-2.450445));
            geographicalCalculatorService.updatePostcodeLocation(location);
        });
        validatePostcodeException(exception, GeographicalError.INVALID_POSTCODE_ID);
    }

    @Test
    @DisplayName("Test updatePostcodeLocation - validate postcode")
    public void test_updatePostcodeLocation_validatePostcode() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            PostcodeLocation location = new PostcodeLocation();
            location.setId(BigInteger.ONE);
            location.setPostcode(null);
            location.setLatitude(BigDecimal.valueOf(53.753322));
            location.setLongitude(BigDecimal.valueOf(-2.450445));
            geographicalCalculatorService.updatePostcodeLocation(location);
        });
        validatePostcodeException(exception, GeographicalError.POSTCODE_IS_MANDATORY);
    }

    @Test
    @DisplayName("Test updatePostcodeLocation - validate latitude")
    public void test_updatePostcodeLocation_validateLatitude() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            PostcodeLocation location = new PostcodeLocation();
            location.setId(BigInteger.ONE);
            location.setPostcode("postcode1");
            location.setLatitude(null);
            location.setLongitude(BigDecimal.valueOf(-2.450445));
            geographicalCalculatorService.updatePostcodeLocation(location);
        });
        validatePostcodeException(exception, GeographicalError.LATITUDE_IS_MANDATORY);
    }

    @Test
    @DisplayName("Test updatePostcodeLocation - validate longitude")
    public void test_updatePostcodeLocation_validateLongitude() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            PostcodeLocation location = new PostcodeLocation();
            location.setId(BigInteger.ONE);
            location.setPostcode("postcode1");
            location.setLatitude(BigDecimal.valueOf(53.753322));
            location.setLongitude(null);
            geographicalCalculatorService.updatePostcodeLocation(location);
        });
        validatePostcodeException(exception, GeographicalError.LONGITUDE_IS_MANDATORY);
    }

    @Test
    @DisplayName("Test updatePostcodeLocation - validate postcode not found")
    public void test_updatePostcodeLocation_validatePostcodeNotFound() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            PostcodeLocation location = new PostcodeLocation();
            location.setId(BigInteger.ONE);
            location.setPostcode("postcode1");
            location.setLatitude(BigDecimal.valueOf(53.753322));
            location.setLongitude(BigDecimal.valueOf(-2.450445));
            Mockito.when(postcodesDao.findById(Mockito.any())).thenReturn(Optional.empty());
            geographicalCalculatorService.updatePostcodeLocation(location);
        });
        validatePostcodeException(exception, GeographicalError.POSTCODE_NOT_FOUND);
    }

    @Test
    @DisplayName("Test updatePostcodeLocation - success without changes")
    public void test_updatePostcodeLocation_successWithoutChanges() throws GeographicalException {
        PostcodeLocation location = new PostcodeLocation();
        location.setId(BigInteger.ONE);
        location.setPostcode("postcode1");
        location.setLatitude(BigDecimal.valueOf(53.753322));
        location.setLongitude(BigDecimal.valueOf(-2.450445));
        PostcodesEntity postcodesEntity = new PostcodesEntity();
        postcodesEntity.setId(location.getId());
        postcodesEntity.setPostcode(location.getPostcode());
        postcodesEntity.setLatitude(location.getLatitude());
        postcodesEntity.setLongitude(location.getLongitude());
        Mockito.when(postcodesDao.findById(Mockito.any())).thenReturn(Optional.of(postcodesEntity));
        Mockito.verify(postcodesDao, Mockito.times(0)).save(Mockito.any());
        Assertions.assertTrue(geographicalCalculatorService.updatePostcodeLocation(location));
    }

    @Test
    @DisplayName("Test updatePostcodeLocation - success with changes")
    public void test_updatePostcodeLocation_successWithChanges() throws GeographicalException {
        PostcodesEntity oriPostcodesEntity = new PostcodesEntity();
        oriPostcodesEntity.setId(BigInteger.ONE);
        oriPostcodesEntity.setPostcode("postcode1");
        oriPostcodesEntity.setLatitude(BigDecimal.valueOf(53.553322));
        oriPostcodesEntity.setLongitude(BigDecimal.valueOf(-2.550445));
        PostcodeLocation location = new PostcodeLocation();
        location.setId(BigInteger.ONE);
        location.setPostcode("postcode1");
        location.setLatitude(BigDecimal.valueOf(53.753322));
        location.setLongitude(BigDecimal.valueOf(-2.450445));
        PostcodesEntity postcodesEntity = new PostcodesEntity();
        postcodesEntity.setId(location.getId());
        postcodesEntity.setPostcode(location.getPostcode());
        postcodesEntity.setLatitude(location.getLatitude());
        postcodesEntity.setLongitude(location.getLongitude());
        Mockito.when(postcodesDao.findById(Mockito.any())).thenReturn(Optional.of(oriPostcodesEntity));
        Mockito.when(postcodesDao.save(Mockito.any())).thenReturn(postcodesEntity);
        Assertions.assertTrue(geographicalCalculatorService.updatePostcodeLocation(location));
    }

    @Test
    @DisplayName("Test calculateDistance - validate source postcode")
    public void test_calculateDistance_validateSourcePostcode() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            GeographicalDistanceRequest request = new GeographicalDistanceRequest();
            request.setSourcePostcode(null);
            request.setDestinationPostcode("postcode1");
            geographicalCalculatorService.calculateDistance(request);
        });
        validatePostcodeException(exception, GeographicalError.SOURCE_DESTINATION_POSTCODE_IS_MANDATORY);
    }

    @Test
    @DisplayName("Test calculateDistance - validate destination postcode")
    public void test_calculateDistance_validateDestinationPostcode() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            GeographicalDistanceRequest request = new GeographicalDistanceRequest();
            request.setSourcePostcode("postcode1");
            request.setDestinationPostcode(null);
            geographicalCalculatorService.calculateDistance(request);
        });
        validatePostcodeException(exception, GeographicalError.SOURCE_DESTINATION_POSTCODE_IS_MANDATORY);
    }

    @Test
    @DisplayName("Test calculateDistance - validate source and destination postcode")
    public void test_calculateDistance_validateSourceDestinationPostcode() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            GeographicalDistanceRequest request = new GeographicalDistanceRequest();
            request.setSourcePostcode(null);
            request.setDestinationPostcode(null);
            geographicalCalculatorService.calculateDistance(request);
        });
        validatePostcodeException(exception, GeographicalError.SOURCE_DESTINATION_POSTCODE_IS_MANDATORY);
    }

    @Test
    @DisplayName("Test calculateDistance - validate source and destination postcode not found")
    public void test_calculateDistance_validateSourceDestinationPostcodeNotFound() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            GeographicalDistanceRequest request = new GeographicalDistanceRequest();
            request.setSourcePostcode("postcode1");
            request.setDestinationPostcode("postcode2");
            Mockito.when(postcodesDao.findByPostcodeIn(Mockito.any())).thenReturn(new ArrayList<>());
            geographicalCalculatorService.calculateDistance(request);
        });
        validatePostcodeException(exception, GeographicalError.INVALID_SOURCE_DESTINATION_POSTCODE);
    }

    @Test
    @DisplayName("Test calculateDistance - validate source postcode not found")
    public void test_calculateDistance_validateSourcePostcodeNotFound() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            GeographicalDistanceRequest request = new GeographicalDistanceRequest();
            request.setSourcePostcode("postcode1");
            request.setDestinationPostcode("postcode2");
            List<PostcodesEntity> postcodes = new ArrayList<>();
            PostcodesEntity postcodesEntity = new PostcodesEntity();
            postcodesEntity.setPostcode("postcode2");
            postcodes.add(postcodesEntity);
            Mockito.when(postcodesDao.findByPostcodeIn(Mockito.any())).thenReturn(postcodes);
            geographicalCalculatorService.calculateDistance(request);
        });
        validatePostcodeException(exception, GeographicalError.INVALID_SOURCE_POSTCODE);
    }

    @Test
    @DisplayName("Test calculateDistance - validate destination postcode not found")
    public void test_calculateDistance_validateDestinationPostcodeNotFound() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            GeographicalDistanceRequest request = new GeographicalDistanceRequest();
            request.setSourcePostcode("postcode1");
            request.setDestinationPostcode("postcode2");
            List<PostcodesEntity> postcodes = new ArrayList<>();
            PostcodesEntity postcodesEntity1 = new PostcodesEntity();
            postcodesEntity1.setPostcode("postcode1");
            postcodes.add(postcodesEntity1);
            Mockito.when(postcodesDao.findByPostcodeIn(Mockito.any())).thenReturn(postcodes);
            geographicalCalculatorService.calculateDistance(request);
        });
        validatePostcodeException(exception, GeographicalError.INVALID_DESTINATION_POSTCODE);
    }

    @Test
    @DisplayName("Test calculateDistance - validate source postcode incomplete")
    public void test_calculateDistance_validateSourcePostcodeIncomplete() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            GeographicalDistanceRequest request = new GeographicalDistanceRequest();
            request.setSourcePostcode("postcode1");
            request.setDestinationPostcode("postcode2");
            List<PostcodesEntity> postcodes = new ArrayList<>();
            PostcodesEntity postcodesEntity1 = new PostcodesEntity();
            postcodesEntity1.setPostcode("postcode1");
            postcodesEntity1.setLatitude(null);
            postcodesEntity1.setLongitude(null);
            postcodes.add(postcodesEntity1);
            PostcodesEntity postcodesEntity2 = new PostcodesEntity();
            postcodesEntity2.setPostcode("postcode2");
            postcodesEntity2.setLatitude(BigDecimal.valueOf(53.753322));
            postcodesEntity2.setLongitude(BigDecimal.valueOf(-2.450445));
            postcodes.add(postcodesEntity2);
            Mockito.when(postcodesDao.findByPostcodeIn(Mockito.any())).thenReturn(postcodes);
            geographicalCalculatorService.calculateDistance(request);
        });
        validatePostcodeException(exception, GeographicalError.INCOMPLETE_SOURCE_POSTCODE);
    }

    @Test
    @DisplayName("Test calculateDistance - validate destination postcode incomplete")
    public void test_calculateDistance_validateDestinationPostcodeIncomplete() {
        GeographicalException exception = Assertions.assertThrows(GeographicalException.class, () -> {
            GeographicalDistanceRequest request = new GeographicalDistanceRequest();
            request.setSourcePostcode("postcode1");
            request.setDestinationPostcode("postcode2");
            List<PostcodesEntity> postcodes = new ArrayList<>();
            PostcodesEntity postcodesEntity1 = new PostcodesEntity();
            postcodesEntity1.setPostcode("postcode1");
            postcodesEntity1.setLatitude(BigDecimal.valueOf(53.753322));
            postcodesEntity1.setLongitude(BigDecimal.valueOf(-2.450445));
            postcodes.add(postcodesEntity1);
            PostcodesEntity postcodesEntity2 = new PostcodesEntity();
            postcodesEntity2.setPostcode("postcode2");
            postcodesEntity2.setLatitude(null);
            postcodesEntity2.setLongitude(null);
            postcodes.add(postcodesEntity2);
            Mockito.when(postcodesDao.findByPostcodeIn(Mockito.any())).thenReturn(postcodes);
            geographicalCalculatorService.calculateDistance(request);
        });
        validatePostcodeException(exception, GeographicalError.INCOMPLETE_DESTINATION_POSTCODE);
    }

    private void validatePostcodeException(GeographicalException exception, GeographicalError error) {
        Assertions.assertEquals(error.getErrorCode(), exception.getErrorCode());
        Assertions.assertEquals(error.getErrorMessage(), exception.getMessage());
    }

}
