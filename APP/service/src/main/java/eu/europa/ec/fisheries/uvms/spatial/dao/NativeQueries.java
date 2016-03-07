package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;

public interface NativeQueries {

    public String stAsText(String wkt);

    public String stTransate(Geometry geometry, Double x, Double y);

}
