package eu.europa.ec.fisheries.uvms.spatial.dao.util;

import eu.europa.ec.fisheries.uvms.spatial.dao.AbstractGisFunction;

public class PostGres extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {
        return "ST_Intersects(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326))";
    }

    @Override
    public String stDistance(Double latitude, Double longitude) {
        return "ST_Distance(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326))";
    }

    @Override
    public String stClosestPoint(Double latitude, Double longitude) {
        return "ST_ClosestPoint(geom, ST_GeomFromText(CAST ('POINT(" + longitude + " " + latitude + ")' AS TEXT), 4326))";
    }

    @Override
    public String limit(int i) {
        return "LIMIT " + i;
    }
}