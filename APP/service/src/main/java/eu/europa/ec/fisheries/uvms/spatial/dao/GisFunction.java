package eu.europa.ec.fisheries.uvms.spatial.dao;

public interface GisFunction {

    public String stIntersects(String wkt, Integer crs);

    public String stIntersects(Double latitude, Double longitude,Integer crs);


}
