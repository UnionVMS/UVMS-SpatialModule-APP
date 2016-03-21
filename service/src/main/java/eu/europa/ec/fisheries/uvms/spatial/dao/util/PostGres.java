package eu.europa.ec.fisheries.uvms.spatial.dao.util;

import eu.europa.ec.fisheries.uvms.spatial.dao.AbstractGisFunction;

public class PostGres extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude, Integer crs) {
        return "ST_Intersects(geom, ST_GeomFromText(CAST('POINT(" + latitude + " " + longitude + ")' AS " + castAsUnlimitedLength() + " ), " + crs + "))";
    }

    @Override
    public String stDistance(Double latitude, Double longitude, Integer crs) {
        return "ST_Distance(geom, ST_GeomFromText(CAST('POINT(" + latitude + " " + longitude + ")' AS " + castAsUnlimitedLength() + "), " + crs + "))";
    }

    @Override
    public String stClosestPoint(Double latitude, Double longitude, Integer crs) {
        return "ST_ClosestPoint(geom, ST_GeomFromText(CAST('POINT(" + latitude + " " + longitude + ")' AS " + castAsUnlimitedLength() + "), " + crs + "))";
    }

    @Override
    public String castAsUnlimitedLength() {
        return "TEXT";
    }

    @Override
    public String toWkt(String value) {
        return "ST_AsText(" + value + ")";
    }

    @Override
    public String limit(int i) {
        return "LIMIT " + i;
    }
}