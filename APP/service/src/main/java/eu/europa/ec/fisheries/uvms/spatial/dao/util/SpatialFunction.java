package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public interface SpatialFunction {

    String stIntersects(Double latitude, Double longitude);

    String stDistance(Double latitude, Double longitude);

    String stClosestPoint(Double latitude, Double longitude);

    String limit(int i);
}
