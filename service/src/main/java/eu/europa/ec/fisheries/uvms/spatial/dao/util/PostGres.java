package eu.europa.ec.fisheries.uvms.spatial.dao.util;

import eu.europa.ec.fisheries.uvms.spatial.dao.AbstractGisFunction;

public class PostGres extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
        return "ST_Intersects(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326))";
    }

    private String stDistance(Double latitude, Double longitude) {
        return "ST_Distance(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326))";
    }

    @Override
    public String closestAreaToPoint(String typeName, String tableName, Double latitude, Double longitude) {

        return "(SELECT '" + typeName + "' AS type, gid, code, name," +
                " ST_ClosestPoint(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326))" +
                " AS closest FROM spatial." + tableName +
                " WHERE NOT ST_IsEmpty(geom) AND enabled = 'Y' " + "ORDER BY " +
                stDistance(latitude, longitude) + " LIMIT  10)";
    }

    @Override
    public String closestPointToPoint(String typeName, String tableName, Double latitude, Double longitude, Integer limit) {

        return "(SELECT '" + typeName + "' as type, gid, code, name, geom, "
                + stDistance(latitude, longitude) + " AS distance" +
                " FROM spatial." + tableName + " WHERE enabled = 'Y'" +
                " ORDER BY distance ASC LIMIT " + limit + " )";
    }

}