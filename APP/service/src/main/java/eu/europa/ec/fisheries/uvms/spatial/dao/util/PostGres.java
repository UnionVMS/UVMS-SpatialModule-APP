package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public class PostGres extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
        return "ST_Intersects(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326)) ";
    }

    
    @Override
    public String closestAreaToPointPrefix(){
    	return "";    	
    }
    
    @Override
    public  String closestAreaToPointSuffix(){
    	return "";
    }
    
    @Override
    public String makeGeomValid(String tableName) {
        return "update spatial."+ tableName + " set geom = st_makevalid(geom) where enabled = 'Y'";
    }
    
    @Override
    public String closestAreaToPoint(int index,String typeName, String tableName, Double latitude, Double longitude, Integer limit) {

        return "(SELECT '" + typeName + "' AS type, gid, code, name," +
                " ST_ClosestPoint(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326))" +
                " AS closest, "+
                " ST_Distance(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326),true) as dist "+                
                " FROM spatial." + tableName +
                " WHERE NOT ST_IsEmpty(geom) AND enabled = 'Y' " + "ORDER BY dist,gid " +
                " LIMIT " + limit + ")";
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {

        return "(SELECT '" + typeName + "' as type, gid, code, name, geom, "
                + "ST_Distance(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326),true) " +
                "AS distance FROM spatial." + tableName + " WHERE enabled = 'Y'" +
                " ORDER BY distance,gid ASC LIMIT " + limit + " )";
    }

}