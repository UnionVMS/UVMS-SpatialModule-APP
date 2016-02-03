package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.rest.FeatureToGeoJsonMapper;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AreaDetailsDto extends GeoJsonDto {

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

    public void setAllAreaProperties(List<Map<String, String>> allAreaProperties) {
        this.allAreaProperties = allAreaProperties;
    }

    public JsonNode convert() throws ParseException, IOException {
        replaceGeometryToAreaGeometryForPortArea(properties);
        String convert = new FeatureToGeoJsonMapper().convert(toFeature());
        return new ObjectMapper().readTree(convert);
    }

    public JsonNode convert(Map<String, String> properties) throws ParseException, IOException {
        replaceGeometryToAreaGeometryForPortArea(properties);
        return new ObjectMapper().readTree(new FeatureToGeoJsonMapper().convert(toFeature(properties)));
    }

    private void replaceGeometryToAreaGeometryForPortArea(Map<String, String> properties) {
        if (PORTAREA.equalsIgnoreCase(type)) {
            String areaGeom = properties.get(AREA_GEOMETRY);
            properties.put(GEOMETRY, areaGeom);
            properties.remove(AREA_GEOMETRY);
        }
    }

    public List<JsonNode> convertAll() throws IOException, ParseException {
        List<JsonNode> nodeList = new ArrayList<JsonNode>();
        for (Map<String, String> featureMap : allAreaProperties) {
            nodeList.add(convert(featureMap));
        }
        return nodeList;
    }

    public void removeGeometryAllAreas() {
        for (Map<String, String> props : allAreaProperties) {
            if (props.containsKey(GEOMETRY)) {
                props.put(EXTENT, getExtend(props.get(GEOMETRY)));
                props.remove(GEOMETRY);
            }
        }
    }
}
