package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.schema.spatial.types.GeometryType;
import org.mapstruct.*;

/**
 * Created by kopyczmi on 11-Aug-15.
 */
@Mapper(componentModel="cdi")
public abstract class GeometryMapper {

    @Mappings({
            @Mapping(target = "coordinates", expression = "java(geometry.toText())")
    })
    public abstract GeometryType geometryToGeometryType(Geometry geometry);

    @AfterMapping
    protected void fillProperties(Geometry geometry, @MappingTarget GeometryType result) {
        Coordinate[] coordinates = geometry.getCoordinates();

    }

}