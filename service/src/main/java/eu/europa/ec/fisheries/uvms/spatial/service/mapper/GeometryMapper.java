package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", imports = {WKTWriter.class, WKTReader.class})
public interface GeometryMapper {

    GeometryMapper INSTANCE = Mappers.getMapper(GeometryMapper.class);

    @Mapping(target = "geometry", expression = "java(new WKTWriter().write(geometry))")
    GeometryType geometryToWKT(Geometry geometry); // FIXME change name inside GeometryType to wkt

   // @Mapping(target = "geometry", expression = "java(new WKTWriter().write(geometry))")
   // GeometryType wktToGeometry(Geometry geometry);
}
