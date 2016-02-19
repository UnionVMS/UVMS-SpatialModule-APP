package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

public enum MeasurementUnit {

    METERS(1),
    KILOMETERS(1000),
    NAUTICAL_MILES(1852),
    MILES(1609.344);

    private double ratio;

    MeasurementUnit(double ratio) {
        this.ratio = ratio;
    }

    public double getRatio() {
        return ratio;
    }

    public static MeasurementUnit getMeasurement(String unit) {
        for (MeasurementUnit measurementUnit : values()) {
            if (measurementUnit.name().equalsIgnoreCase(unit)) {
                return measurementUnit;
            }
        }
        throw new SpatialServiceException(SpatialServiceErrors.WRONG_MEASUREMENT_UNIT, unit);
    }
}
