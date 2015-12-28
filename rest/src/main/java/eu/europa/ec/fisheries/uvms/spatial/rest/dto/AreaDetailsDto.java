package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.GeoJsonDto;
import org.opengis.feature.simple.SimpleFeature;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;

import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;

public class AreaDetailsDto extends GeoJsonDto {
	
	private static final String GEOMETRY = "geometry";
	
	private static final String EXTENT = "extent";
	
	private List<Map<String, String>> allAreaProperties = new ArrayList<Map<String, String>>();
	
    public SimpleFeature toFeature() throws ParseException {
    	return super.toFeature(MultiPolygon.class);
    }
    
    public SimpleFeature toFeature(Map<String, String> properties) throws ParseException {
    	return super.toFeature(MultiPolygon.class, properties);
    }

	public List<Map<String, String>> getAllAreaProperties() {
		return allAreaProperties;
	}
	
	public JsonNode convert() throws ParseException, IOException {
		return new ObjectMapper().readTree(new FeatureToGeoJsonMapper().convert(toFeature()));
	}
	
	public JsonNode convert(Map<String, String> featureMap) throws ParseException, IOException {
		return new ObjectMapper().readTree(new FeatureToGeoJsonMapper().convert(toFeature(featureMap)));
	}
	
	public List<JsonNode> convertAll() throws IOException, ParseException {
		List<JsonNode> nodeList = new ArrayList<JsonNode>();
		for (Map<String, String> featureMap : allAreaProperties) {
			nodeList.add(convert(featureMap));
		}
		return nodeList;
	}

	public void setAllAreaProperties(List<Map<String, String>> allAreaProperties) {
		this.allAreaProperties = allAreaProperties;
	}
	
	public List<Map<String, String>> removeGeometryAllAreas() {
		List<Map<String, String>> tempAllAreaProperties = new ArrayList<Map<String, String>>();
		for (Map<String, String> props : allAreaProperties) {
			if(props.containsKey(GEOMETRY)) {
        		props.put(EXTENT, getExtend(props.get(GEOMETRY)));
        		props.remove(GEOMETRY);
        	}
			tempAllAreaProperties.add(props);
		}
		return tempAllAreaProperties;
	}
}
