package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public interface SpatialFunction {

    String stIntersects(Double latitude, Double longitude);

    String stDistance(Double latitude, Double longitude);

    String stClosestPoint(Double latitude, Double longitude);

    String castAsUnlimitedLength();

    String toWkt(String value);

    String limit(int i);
}
