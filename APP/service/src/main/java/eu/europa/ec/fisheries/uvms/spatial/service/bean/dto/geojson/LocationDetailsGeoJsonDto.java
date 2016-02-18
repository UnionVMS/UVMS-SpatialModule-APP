package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.GeoJsonDto;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;

public class LocationDetailsGeoJsonDto extends GeoJsonDto {

    public SimpleFeature toFeature() throws ParseException {
        return super.toFeature(Point.class);
    }

    public JsonNode convert() throws ParseException, IOException {
        return new ObjectMapper().readTree(new FeatureToGeoJsonMapper().convert(toFeature()));
    }
}
