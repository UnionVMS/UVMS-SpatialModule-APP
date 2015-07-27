package eu.europa.ec.fisheries.uvms.movement;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import org.apache.commons.lang3.StringUtils;
import org.geojson.LngLatAlt;
import org.geojson.Point;

import java.io.IOException;
import java.util.List;

/**
 * //TODO create test
 */
public class MovementDtoListSerializer extends JsonSerializer<List<MovementDto>> {

    @Override
    public void serialize(List<MovementDto> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();
        gen.writeStringField("type", "FeatureCollection");
        gen.writeFieldName("features");
        gen.writeStartArray();

        for (MovementDto movementDto: value){
            MovementPoint position = movementDto.getPosition();

            gen.writeStartObject();
            gen.writeStringField("type", "Feature");
            gen.writeFieldName("geometry");

            if (position != null){
                double latitude = position.getLatitude();
                double longitude = position.getLongitude();
                Point point = new Point();
                LngLatAlt coordinates = new LngLatAlt();
                coordinates.setLatitude(latitude);
                coordinates.setLongitude(longitude);
                point.setCoordinates(coordinates);
                writePoint(gen, point);
            }

            writeProperties(gen, movementDto);
            gen.writeEndObject();
        }

        gen.writeEndArray();
        gen.writeEndObject();
    }

    private void writeProperties(JsonGenerator gen, MovementDto value) throws IOException{
        gen.writeFieldName("properties");
        gen.writeStartObject();
        gen.writeStringField("st", value.getStatus());
        gen.writeNumberField("cs", value.getCalculatedSpeed());
        gen.writeNumberField("co", value.getCourse());
        gen.writeNumberField("ms", value.getMeasuredSpeed());
        gen.writeStringField("mt", value.getMessageType().value());

        if (value.getPositionTime() != null){
            String s = value.getPositionTime().toString();
            if (StringUtils.isNotBlank(s)){
                gen.writeStringField("pt", s);
            }
        }
        gen.writeEndObject();
    }

    private void writePoint(JsonGenerator gen, Point p) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "Point");
        gen.writeFieldName("coordinates");
        writePointCoords(gen, p);
        gen.writeEndObject();
    }

    private void writePointCoords(JsonGenerator gen, Point p) throws IOException {
        gen.writeStartArray();
        gen.writeNumber(p.getCoordinates().getLongitude());
        gen.writeNumber(p.getCoordinates().getLatitude());
        gen.writeEndArray();
    }

}
