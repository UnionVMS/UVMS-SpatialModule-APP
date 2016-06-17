package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public interface DatabaseDialect {

    String stIntersects(Double latitude, Double longitude);

    String closestAreaToPoint(int index,String typeName, String tableName, Double latitude, Double longitude, Integer limit);

    String closestAreaToPointPrefix();
    String closestAreaToPointSuffix();
    

    String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit);


    String makeGeomValid(String tableName);
    
}