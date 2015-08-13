package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.schema.spatial.types.GeometryType;
import org.geotools.geojson.geom.GeometryJSON;
import org.mapstruct.*;

/**
 * Created by kopyczmi on 11-Aug-15.
 */
@Mapper(componentModel = "cdi")
public abstract class GeometryMapper {

    @Mappings({
            @Mapping(target = "geometryJson", expression = "java(new org.geotools.geojson.geom.GeometryJSON().toString(geometry))")
    })
    public abstract GeometryType geometryToGeometryType(Geometry geometry);

}