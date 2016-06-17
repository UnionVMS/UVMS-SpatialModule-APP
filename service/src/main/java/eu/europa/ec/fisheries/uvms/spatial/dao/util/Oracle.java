package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public class Oracle extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
        return "SDO_RELATE(GEOM, SDO_GEOMETRY('POINT(" + longitude + " " + latitude + ")', 8307), 'mask=contains') = 'TRUE' AND enabled = 'Y'";
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
        return "update spatial." + tableName + " set geom = SDO_UTIL.RECTIFY_GEOMETRY(geom, 0.005) where enabled = 'Y'";
    }
    
    	
    @Override
    public String closestAreaToPoint(int index,String typeName, String tableName, Double latitude, Double longitude, Integer limit) {

     return "(select rownum, a.* from (SELECT "+index+" as indexRS, '" + typeName + "' AS type, gid, code, name, SDO_NN_DISTANCE(1) AS dist, " +
             "geom AS closest FROM spatial." + tableName + " WHERE enabled = 'Y' " +
             "AND SDO_NN(geom, SDO_GEOMETRY('POINT(" + longitude + " " + latitude + ")', 8307), 1) = 'TRUE' "
             		+ " ORDER BY dist,gid ASC) a where rownum <= "+limit+") ";
     //
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {

        return "select ROWNUM,s.* from (SELECT '" + typeName + "' as type, gid, code,name, geom, "
                + stDistance(latitude, longitude) + " AS distance " +
                "FROM spatial." + tableName + " WHERE enabled = 'Y' " +
                "ORDER BY distance,gid ASC) s where rownum<= "+ limit ;
    }

    private String stDistance(Double latitude, Double longitude) {
        return "SDO_GEOM.SDO_DISTANCE(geom, SDO_GEOMETRY('POINT(" + longitude + " " + latitude + ")', 8307), 0.05, 'unit=M')";
    }
}
