package eu.europa.ec.fisheries.uvms.spatial.rest.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.EezDto;

import java.io.IOException;

/**
 * Created by kopyczmi on 11-Aug-15.
 */
public class EezDtoSerializer extends JsonSerializer<EezDto> {

    private static final String GEOMETRY = "geometry";
    private static final String PROPERTIES = "properties";
    private static final String FEATURE = "Feature";
    protected static final String TYPE = "type";
    protected static final String COORDINATES = "coordinates";


    @Override
    public void serialize(EezDto eezDto, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(TYPE, FEATURE);
        gen.writeFieldName(GEOMETRY);
        writeGeometry(gen , eezDto.getGeometryJson());
        writeProperties(gen, eezDto);

        gen.writeEndObject();
    }

    private void writeGeometry(JsonGenerator gen, String geometryJson) throws IOException {
        gen.writeStartObject();

        JsonNode rootNode = new ObjectMapper().readTree(geometryJson);

        gen.writeStringField(TYPE, rootNode.get(TYPE).textValue());
        gen.writeObjectField(COORDINATES, rootNode.get(COORDINATES));

        gen.writeEndObject();
    }

    private void writeProperties(JsonGenerator gen, EezDto eezDto) throws IOException {
        gen.writeFieldName(PROPERTIES);
        gen.writeStartObject();
        gen.writeStringField("gid", String.valueOf(eezDto.getGid()));
        gen.writeStringField("eez", eezDto.getEez());
        gen.writeStringField("country", eezDto.getCountry());
        gen.writeStringField("sovereign", eezDto.getSovereign());
        gen.writeStringField("remarks", eezDto.getRemarks());
        gen.writeNumberField("sovId", eezDto.getSovId());
        gen.writeNumberField("eezId", eezDto.getEezId());
        gen.writeStringField("iso3Digit", eezDto.getIso3Digit());
        gen.writeStringField("mrgid", String.valueOf(eezDto.getMrgid()));
        gen.writeStringField("dateChang", eezDto.getDateChang());
        gen.writeNumberField("areaM2", eezDto.getAreaM2());
        gen.writeNumberField("longitude", eezDto.getLongitude());
        gen.writeNumberField("latitude", eezDto.getLatitude());
        gen.writeNumberField("mrgidEez", eezDto.getMrgidEez());
        gen.writeEndObject();
    }

}
