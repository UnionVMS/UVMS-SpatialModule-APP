/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.dao.util;

public class PostGres extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
    	sb.append("update spatial.").append(tableName).append(" set geom = st_makevalid(geom) where enabled = 'Y'");
    	return sb.toString();
    }

    @Override
    public String closestAreaToPoint(int index,String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT '").append(typeName).append("' as type, gid, code, name, geom, ");
        sb.append("ST_Distance(geom, ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326),true) ");
        sb.append("AS distance FROM spatial.").append(tableName).append(" WHERE enabled = 'Y'");
        sb.append(" ORDER BY distance,gid ASC LIMIT ").append(limit).append(" )");
        return sb.toString();
    }

}