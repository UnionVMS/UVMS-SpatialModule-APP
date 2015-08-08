package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.schema.spatial.types.GeometryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel="cdi")
public interface GeometryMapper {

    @Mappings({
            @Mapping(target = "coordinates", expression = "java(geometry.toText())"),
            @Mapping(target = "type", source = "geometryType")
    })
    GeometryType geometryToGeometryType(Geometry geometry);
}
