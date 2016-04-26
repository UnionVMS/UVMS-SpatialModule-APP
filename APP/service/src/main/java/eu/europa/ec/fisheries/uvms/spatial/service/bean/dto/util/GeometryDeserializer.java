package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vividsolutions.jts.geom.Geometry;
import org.wololo.geojson.GeoJSON;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import java.io.IOException;

public class GeometryDeserializer extends JsonDeserializer<Geometry> { // TODO @Greg try to get rid of dependency on wololo

    private static final int DEFAULT_SRID = 4326;

    @Override
    public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        GeoJSON geoJSON = GeoJSONFactory.create(jsonParser.readValueAsTree().toString());
        Geometry geom = new GeoJSONReader().read(geoJSON);
        if (geom.getSRID() == 0) {
            geom.setSRID(DEFAULT_SRID);
        }
        return geom;
    }
}
