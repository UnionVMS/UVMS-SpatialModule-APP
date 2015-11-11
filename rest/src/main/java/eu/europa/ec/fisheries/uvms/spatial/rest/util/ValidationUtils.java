package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
public final class ValidationUtils {

    private ValidationUtils(){}

    public static void validateInputParameters(Double lat, Double lon) {
        validateCoordinates(lat, lon);
    }

    public static void validateCoordinates(Double lat, Double lon) {
        validateLatitude(lat);
        validateLongitude(lon);
    }

    private static void validateLatitude(Double lat) {
        if (lat == null) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_LATITUDE);
        }
    }

    private static void validateLongitude(Double lon) {
        if (lon == null) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_LONGITUDE);
        }
    }

    public static void validateAreaTypes(List<String> areaTypes) {
        if (isEmpty(areaTypes)) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_AREA_TYPE);
        }
    }

    public static void validateLocationTypes(List<String> locationTypes) {
        if (isEmpty(locationTypes)) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_LOCATION_TYPE);
        }
    }
}
