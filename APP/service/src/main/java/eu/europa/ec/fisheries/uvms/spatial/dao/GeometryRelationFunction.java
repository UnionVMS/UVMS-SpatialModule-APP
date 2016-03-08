package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Geometry;

public interface GeometryRelationFunction {

    public String stAsText(String wkt);

    public String stTranslate(Geometry geometry, Double x, Double y);

}
