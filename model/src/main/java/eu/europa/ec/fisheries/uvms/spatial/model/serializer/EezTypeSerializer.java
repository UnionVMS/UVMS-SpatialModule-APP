package eu.europa.ec.fisheries.uvms.spatial.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;

import java.io.IOException;

/**
 * TODO create test
 */
public class EezTypeSerializer extends JsonSerializer<EezType> {

    private static final String PROPERTIES = "properties";
    private static final String FEATURE = "Feature";
    private static final String TYPE = "type";

    @Override
    public void serialize(EezType eezType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(TYPE, FEATURE);
        jsonGenerator.writeObject(eezType.getGeometry());
        writeProperties(jsonGenerator, eezType);
        jsonGenerator.writeEndObject();
    }

    private void writeProperties(JsonGenerator gen, EezType eezType) throws IOException {
        gen.writeFieldName(PROPERTIES);
        gen.writeStartObject();
        gen.writeStringField("gid", String.valueOf(eezType.getGid()));
        gen.writeStringField("eez", eezType.getEez());
        gen.writeStringField("country", eezType.getCountry());
        gen.writeStringField("sovereign", eezType.getSovereign());
        gen.writeStringField("remarks", eezType.getRemarks());
        gen.writeNumberField("sovId", eezType.getSovId());
        gen.writeNumberField("eezId", eezType.getEezId());
        gen.writeStringField("iso3Digit", eezType.getIso3Digit());
        gen.writeStringField("mrgid", String.valueOf(eezType.getMrgid()));
        gen.writeStringField("dateChang", eezType.getDateChang());
        gen.writeNumberField("areaM2", eezType.getAreaM2());
        gen.writeNumberField("longitude", eezType.getLongitude());
        gen.writeNumberField("latitude", eezType.getLatitude());
        gen.writeNumberField("mrgidEez", eezType.getMrgidEez());
        gen.writeEndObject();
    }
}

