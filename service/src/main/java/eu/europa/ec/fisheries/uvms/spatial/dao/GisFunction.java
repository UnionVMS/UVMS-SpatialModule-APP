package eu.europa.ec.fisheries.uvms.spatial.dao;

public interface GisFunction {

    String stIntersects(String wkt, Integer crs);

    String stIntersects(Double latitude, Double longitude,Integer crs);

    String stAsText(String wkt);

    // ORACLE = CLOB
    String castAsUnlimitedLength();

    String geomToWkt();


}
