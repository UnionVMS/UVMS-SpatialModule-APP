package eu.europa.ec.fisheries.uvms.spatial.dao;

public interface GisFunction {

    String stIntersects(String wkt, Integer crs);

    String stIntersects(Double latitude, Double longitude,Integer crs);

    String stDistance(Double latitude, Double longitude, Integer crs);

    // ORACLE = CLOB
    String castAsUnlimitedLength();

    String toWkt(String value);

    String limit(int i);
}
