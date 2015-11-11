package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import org.opengis.feature.simple.SimpleFeature;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;

import java.io.IOException;

public class LocationDetailsDto extends GeoJsonDto {
	
    public SimpleFeature toFeature() throws ParseException {
    	return super.toFeature(Point.class);
    }
    
	public JsonNode convert() throws ParseException, IOException {
		return new ObjectMapper().readTree(new FeatureToGeoJsonMapper().convert(toFeature()));
	}
}
