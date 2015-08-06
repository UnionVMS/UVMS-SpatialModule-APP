package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
public class GeometryMapper {
    public String asString(Geometry geometry) {
        // TODO Fix it - please return GeoJSON
        // Guys this is wrong JSON belongs in rest layer not service layer
        // look as serializer deserializer example in MockResource please thanks guyz :-)
        return "test";
    }
}
