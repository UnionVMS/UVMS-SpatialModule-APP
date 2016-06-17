package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public class Oracle extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
    	StringBuffer sb = new StringBuffer(); 
        sb.append("SDO_RELATE(GEOM, SDO_GEOMETRY('POINT(").append(longitude).append(" ").append(latitude).append(")', 8307), 'mask=contains') = 'TRUE' AND enabled = 'Y'");
        return sb.toString();
    }

    
    @Override
    public String closestAreaToPointPrefix(){
    	return "select * from  (";    	
    }
    
    @Override
    public  String closestAreaToPointSuffix(){
    	return ")  ORDER BY indexRS,dist,gid ASC ";
    }

    @Override
    public String makeGeomValid(String tableName) {
    	StringBuffer sb = new StringBuffer();     	
    	sb.append("update spatial.").append(tableName).append(" set geom = SDO_UTIL.RECTIFY_GEOMETRY(geom, 0.005) where enabled = 'Y'");
        return sb.toString();
    }
    
    	
    @Override
    public String closestAreaToPoint(int index,String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
    	StringBuffer sb = new StringBuffer();     	
    	sb.append("(select rownum, a.* from (SELECT ").append(index).append(" as indexRS, '").append(typeName).append("' AS type, gid, code, name, SDO_NN_DISTANCE(1) AS dist, ");
    	sb.append("geom AS closest FROM spatial.").append(tableName).append(" WHERE enabled = 'Y' " );
    	sb.append("AND SDO_NN(geom, SDO_GEOMETRY('POINT(").append(longitude).append(" ").append(latitude).append(")', 8307), 1) = 'TRUE' ");
    	sb.append(" ORDER BY dist,gid ASC) a where rownum <= ").append(limit).append(") ");
    	return sb.toString();
    						
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
    	StringBuffer sb = new StringBuffer();     	
    	sb.append("select ROWNUM,s.* from (SELECT '").append(typeName).append("' as type, gid, code,name, geom, ");
    	sb.append( stDistance(latitude, longitude)).append(" AS distance " );
    	sb.append("FROM spatial.").append(tableName).append(" WHERE enabled = 'Y' ");
    	sb.append("ORDER BY distance,gid ASC) s where rownum<= ").append(limit) ;
    	return sb.toString();
        
    }

    private String stDistance(Double latitude, Double longitude) {
    	StringBuffer sb = new StringBuffer();     	
    	sb.append("SDO_GEOM.SDO_DISTANCE(geom, SDO_GEOMETRY('POINT(").append(longitude).append(" ").append(latitude).append(")', 8307), 0.05, 'unit=M')");
    	return sb.toString();
    }
}
