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
}