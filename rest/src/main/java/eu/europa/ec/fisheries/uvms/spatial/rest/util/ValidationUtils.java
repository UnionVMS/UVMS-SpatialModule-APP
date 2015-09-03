package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
public class ValidationUtils {
    public static void validateInputParameters(Double lat, Double lon) {
        validateCoordinates(lat, lon);
    }

    public static void validateInputParameters(Double lat, Double lon, List<String> areaTypes) {
        validateCoordinates(lat, lon);
        validateAreaTypes(areaTypes);
    }

    private static void validateCoordinates(Double lat, Double lon) {
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

    private static void validateAreaTypes(List<String> areaTypes) {
        if (isEmpty(areaTypes)) {
            throw new SpatialServiceException(SpatialServiceErrors.MISSING_AREA_TYPE);
        }
    }
}
