package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

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
}
