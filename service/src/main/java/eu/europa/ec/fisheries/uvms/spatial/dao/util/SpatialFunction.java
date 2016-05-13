package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public interface SpatialFunction {

    String stIntersects(Double latitude, Double longitude);

    String closestAreaToPoint(String typeName, String tableName, Double latitude, Double longitude);

    String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit);

}
