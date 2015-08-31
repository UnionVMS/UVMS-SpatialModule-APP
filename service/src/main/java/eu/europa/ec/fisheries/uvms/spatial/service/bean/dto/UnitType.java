package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

public enum UnitType {

    METERS(1),
    KILOMETERS(1000),
    NAUTICAL_MILES(1020);// FIXME wrong values

    private double unit;

    UnitType(double unit) {
        this.unit = unit;
    }

    public double getUnit() {
        return unit;
    }
}
