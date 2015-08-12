package eu.europa.ec.fisheries.uvms.spatial.rest.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.EezDto;

import java.io.IOException;

/**
 * Created by kopyczmi on 12-Aug-15.
 */
public abstract class AbstractDtoSerializer extends JsonSerializer<EezDto> {

    protected static final String TYPE = "type";
    protected static final String COORDINATES = "coordinates";

    protected void writeGeometry(JsonGenerator gen, String geometryJson) throws IOException {
        gen.writeStartObject();

        JsonNode rootNode = new ObjectMapper().readTree(geometryJson);

        gen.writeStringField(TYPE, rootNode.get(TYPE).textValue());
        gen.writeObjectField(COORDINATES, rootNode.get(COORDINATES));

        gen.writeEndObject();
    }
}
