package eu.europa.ec.fisheries.uvms.spatial.dao.util;


public class H2gis extends PostGres {

    @Override
    public String closestAreaToPoint(int index,String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
        return super.closestAreaToPoint(index, typeName, tableName, latitude, longitude, limit).replace(",true)", ")");
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
        return super.closestPointToPoint(typeName, tableName, latitude, longitude, limit).replace(",true)", ")");
    }
}
