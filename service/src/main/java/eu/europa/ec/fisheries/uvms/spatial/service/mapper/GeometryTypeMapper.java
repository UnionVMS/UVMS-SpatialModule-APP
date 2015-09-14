package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface GeometryTypeMapper {

    GeometryTypeMapper INSTANCE = Mappers.getMapper(GeometryTypeMapper.class);

    @Mapping(target = "geometry", expression = "java(new com.vividsolutions.jts.io.WKTWriter().write(geometry))")
    GeometryType geometryToGeometryType(Geometry geometry);

}
