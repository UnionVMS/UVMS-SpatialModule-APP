package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public interface DatabaseDialect {

    String stIntersects(Double latitude, Double longitude);

    String closestAreaToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit);

    String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit);

    String makeGeomValid(String tableName);

}
