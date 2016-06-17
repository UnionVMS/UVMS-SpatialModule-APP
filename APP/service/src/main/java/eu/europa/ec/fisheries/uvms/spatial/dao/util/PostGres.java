package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public class PostGres extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
    	StringBuffer sb = new StringBuffer();    	    	
        sb.append("ST_Intersects(geom, ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326)) ");
        return sb.toString();
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
    	StringBuffer sb = new StringBuffer();    	
    	sb.append("update spatial.").append(tableName).append(" set geom = st_makevalid(geom) where enabled = 'Y'");
    	return sb.toString();
    }
    
    @Override
    public String closestAreaToPoint(int index,String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("(SELECT '").append(typeName).append("' AS type, gid, code, name,");
    	sb.append(" ST_ClosestPoint(geom, ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326))" );
    	sb.append(" AS closest, ");
    	sb.append(" ST_Distance(geom, ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326),true) as dist ");                
    	sb.append(" FROM spatial.").append(tableName);
    	sb.append(" WHERE NOT ST_IsEmpty(geom) AND enabled = 'Y' ORDER BY dist,gid ");
    	sb.append(" LIMIT ").append(limit).append(")");
    	return sb.toString();
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
    	StringBuffer sb = new StringBuffer();
        sb.append("(SELECT '").append(typeName).append("' as type, gid, code, name, geom, ");
        sb.append("ST_Distance(geom, ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326),true) ");
        sb.append("AS distance FROM spatial.").append(tableName).append(" WHERE enabled = 'Y'");
        sb.append(" ORDER BY distance,gid ASC LIMIT ").append(limit).append(" )");
        return sb.toString();
    }

}