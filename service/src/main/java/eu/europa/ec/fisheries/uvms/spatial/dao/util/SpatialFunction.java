package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public interface SpatialFunction {

    String stIntersects(Double latitude, Double longitude,Integer crs);

    String stDistance(Double latitude, Double longitude, Integer crs);

    String stClosestPoint(Double latitude, Double longitude, Integer crs);

    String castAsUnlimitedLength();

    String toWkt(String value);

    String limit(int i);
}
