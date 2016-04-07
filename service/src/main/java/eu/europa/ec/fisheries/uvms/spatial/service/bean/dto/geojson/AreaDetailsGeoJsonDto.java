package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;
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

    public void removeGeometryAllAreas() {
        for (Map<String, Object> props : allAreaProperties) {
            if (props.containsKey(GEOMETRY)) {
                props.put(EXTENT, getExtend(props.get(GEOMETRY)));
                props.remove(GEOMETRY);
            }
        }
    }
}
