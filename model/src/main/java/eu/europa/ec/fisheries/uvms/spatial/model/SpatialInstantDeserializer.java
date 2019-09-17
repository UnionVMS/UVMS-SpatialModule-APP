package eu.europa.ec.fisheries.uvms.spatial.model;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.time.format.DateTimeFormatter;

public class SpatialInstantDeserializer extends InstantDeserializer {
    public SpatialInstantDeserializer(){
        super(InstantDeserializer.INSTANT ,DateTimeFormatter.ISO_INSTANT);
    }
}
