/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dao.util;

public class Oracle extends AbstractGisFunction {

    public static final int DEFAULT_ORACLE_SRID = 8307;

    @Override
    public String stIntersects(Double latitude, Double longitude) {
        StringBuilder sb = new StringBuilder();
        sb.append("SDO_RELATE(GEOM, SDO_GEOMETRY('POINT(").append(longitude).append(" ").append(latitude).append(")', 8307), 'mask=contains') = 'TRUE' AND enabled = true");
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
        StringBuilder sb = new StringBuilder();
    	sb.append("update spatial.").append(tableName).append(" set geom = SDO_UTIL.RECTIFY_GEOMETRY(geom, 0.005) where enabled = true");
        return sb.toString();
    }

    @Override
    public Integer defaultSRID() {
        return DEFAULT_ORACLE_SRID;
    }

    @Override
    public String closestAreaToPoint(int index,String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
        StringBuilder sb = new StringBuilder();
    	sb.append("(select rownum, a.* from (SELECT ").append(index).append(" as indexRS, '").append(typeName).append("' AS type, gid, code, name, SDO_NN_DISTANCE(1) AS dist, ");
    	sb.append("geom AS closest FROM spatial.").append(tableName).append(" WHERE enabled = true " );
    	sb.append("AND SDO_NN(geom, SDO_GEOMETRY('POINT(").append(longitude).append(" ").append(latitude).append(")', 8307), 1) = 'TRUE' ");
    	sb.append(" ORDER BY dist,gid ASC) a where rownum <= ").append(limit).append(") ");
    	return sb.toString();
    						
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
        StringBuilder sb = new StringBuilder();
    	sb.append("select ROWNUM,s.* from (SELECT '").append(typeName).append("' as type, gid, code,name, geom, ");
    	sb.append( stDistance(latitude, longitude)).append(" AS distance " );
    	sb.append("FROM spatial.").append(tableName).append(" WHERE enabled = true ");
    	sb.append("ORDER BY distance,gid ASC) s where rownum<= ").append(limit) ;
    	return sb.toString();
        
    }

    private String stDistance(Double latitude, Double longitude) {
        StringBuilder sb = new StringBuilder();
    	sb.append("SDO_GEOM.SDO_DISTANCE(geom, SDO_GEOMETRY('POINT(").append(longitude).append(" ").append(latitude).append(")', 8307), 0.05, 'unit=M')");
    	return sb.toString();
    }
}