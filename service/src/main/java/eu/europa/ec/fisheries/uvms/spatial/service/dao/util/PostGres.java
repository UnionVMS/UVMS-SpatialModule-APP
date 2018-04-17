/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao.util;

public class PostGres extends AbstractGisFunction {

    public static final int DEFAULT_WILDFLY_SRID = 4326;

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
    public Integer defaultSRID() {
        return DEFAULT_WILDFLY_SRID;
    }

    public String closestAreaToPoint(int index, String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("(WITH candidates AS (SELECT cast('").append(typeName).append("' as varchar) as type, gid, code, name, geom FROM spatial.").append(tableName);
        sb.append(" WHERE enabled = 'Y' ORDER BY geom <-> ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude);
        sb.append(")' AS TEXT), 4326) LIMIT 50)");
        sb.append(" SELECT type, gid, code, name, geom as closest, ST_Distance(geom,");
        sb.append(" ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326), true) as dist");
        sb.append(" FROM candidates ORDER BY dist LIMIT ").append(limit).append(")");

        return sb.toString();
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT '").append(typeName).append("' as type, gid, code, name, geom,");
        sb.append(" ST_Distance(geom, ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326),true) AS distance");
        sb.append(" FROM spatial.").append(tableName).append(" WHERE enabled = 'Y' AND");
        sb.append(" ST_DWithin(ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326), geom, 22224)");
        sb.append(" ORDER BY ST_GeomFromText(CAST ('POINT(").append(longitude).append(" ").append(latitude).append(")' AS TEXT), 4326) <-> geom");
        sb.append(" LIMIT ").append(limit).append(" )");

        return sb.toString();
    }

}