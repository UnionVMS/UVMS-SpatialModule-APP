package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson;

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

public class AreaDetailsGeoJsonDto extends GeoJsonDto {

    private static final String EXTENT = "extent";

    private List<Map<String, Object>> allAreaProperties = new ArrayList<>();

    public SimpleFeature toFeature() throws ParseException {
        return super.toFeature(MultiPolygon.class);
    }

    public SimpleFeature toFeature(Map<String, Object> properties) throws ParseException {
        return super.toFeature(MultiPolygon.class, properties);
    }

    public List<Map<String, Object>> getAllAreaProperties() {
        return allAreaProperties;
    }

    public void setAllAreaProperties(List<Map<String, Object>> allAreaProperties) {
        this.allAreaProperties = allAreaProperties;
    }

    public JsonNode convert() throws ParseException, IOException {
        String convert = new FeatureToGeoJsonMapper().convert(toFeature());
        return new ObjectMapper().readTree(convert);
    }

    public JsonNode convert(Map<String, Object> properties) throws ParseException, IOException {
        return new ObjectMapper().readTree(new FeatureToGeoJsonMapper().convert(toFeature(properties)));
    }

    public List<JsonNode> convertAll() throws IOException, ParseException {
        List<JsonNode> nodeList = new ArrayList<JsonNode>();
        for (Map<String, Object> featureMap : allAreaProperties) {
            nodeList.add(convert(featureMap));
        }
        return nodeList;
    }

    public void removeGeometryAllAreas() {
        for (Map<String, Object> props : allAreaProperties) {
            if (props.containsKey(GEOMETRY)) {
                props.put(EXTENT, getExtend(props.get(GEOMETRY)));
                props.remove(GEOMETRY);
            }
        }
    }
}
