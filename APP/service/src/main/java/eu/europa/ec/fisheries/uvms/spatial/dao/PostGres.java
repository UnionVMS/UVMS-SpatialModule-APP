package eu.europa.ec.fisheries.uvms.spatial.dao;

public class PostGres extends AbstractGisFunction {

    @Override
    public String stIntersects(String wkt, Integer crs) {
        return "st_intersects(geom, st_geomfromtext(CAST('" + wkt+ "' as text), " + crs + "))";
    }

    @Override
    public String stIntersects(Double latitude, Double longitude, Integer crs) {
        return "st_intersects(geom, st_geomfromtext(CAST('POINT(" + latitude + " " + longitude + ")' as text), " + crs + "))";
    }

    @Override
    public String stAsText(String wkt) {
        return null;
    }

    @Override
    public String castAsUnlimitedLength() {
        return "TEXT";
    }

    @Override
    public String geomToWkt() {
        return "ST_AsText(geom)";
    }
}