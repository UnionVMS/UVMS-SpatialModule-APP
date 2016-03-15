package eu.europa.ec.fisheries.uvms.spatial.dao;

public class PostGres extends AbstractGisFunction {

    @Override
    public String stIntersects(String wkt, Integer crs) {
        return "ST_Intersects(geom, ST_GeomFromText(CAST('" + wkt + "' AS " + castAsUnlimitedLength() +" ), " + crs + "))";
    }

    @Override
    public String stIntersects(Double latitude, Double longitude, Integer crs) {
        return "ST_Intersects(geom, ST_GeomFromText(CAST('POINT(" + latitude + " " + longitude + ")' AS " + castAsUnlimitedLength() + " ), " + crs + "))";
    }

    @Override
    public String stDistance(Double latitude, Double longitude, Integer crs) {
        return "ST_Distance(geom, ST_GeomFromText(CAST('POINT(" + latitude + " " + longitude + ")' AS " + castAsUnlimitedLength() + "), " + crs + "))";
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