package eu.europa.ec.fisheries.uvms.spatial.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;

/**
 * //TODO create test
 */
public class GeometryTypeSerializer extends JsonSerializer<GeometryType> {

    private static final String FIELD_SEPARATOR = ":";
    private static final String GEOMETRY = "geometry";

    @Override
    public void serialize(GeometryType geometryType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        try {
            WKTReader wktReader = new WKTReader();
            GeometryJSON geometryJSON = new GeometryJSON();
            jsonGenerator.writeFieldName(GEOMETRY);
            Geometry geometry = wktReader.read(geometryType.getGeometry());
            String geometryJson = geometryJSON.toString(geometry);
            jsonGenerator.writeRaw(FIELD_SEPARATOR + geometryJson);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
