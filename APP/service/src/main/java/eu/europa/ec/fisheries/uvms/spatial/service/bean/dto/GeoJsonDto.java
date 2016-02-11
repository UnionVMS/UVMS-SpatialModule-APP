package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class GeoJsonDto {

    public static final String GEOMETRY = "geometry";
    public static final String AREA_GEOMETRY = "areageometry";
    protected static final String ID = "id";
    protected static final String PORTAREA = "PORTAREA";
    private static final String EXTENT = "extent";
    protected String type;
    @JsonDeserialize(using = GeometryDeserializer.class)
    protected Geometry geometry;
    protected Map<String, Object> properties = new HashMap<String, Object>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void add(String key, String value) {
        properties.put(key, value);
    }

    protected SimpleFeatureType build(Class geometryType, Map<String, Object> properties, String geometryFieldName) {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName(type.toUpperCase());
        for (String key : properties.keySet()) {
            if (key.equalsIgnoreCase(geometryFieldName)) {
                sb.add(key, geometryType);
            } else {
                Class propClass = String.class;
                Object propValue = properties.get(key);

                if (propValue != null) {
                    propClass = propValue.getClass();
                }
                sb.add(key, propClass);
            }
        }
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature(Class geometryType) throws ParseException {
        return toFeature(geometryType, properties);
    }

    public SimpleFeature toFeature(Class geometryType, Map<String, Object> properties) throws ParseException {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(build(geometryType, properties, GEOMETRY));

        for (Entry<String, Object> entrySet : properties.entrySet()) {
            featureBuilder.set(entrySet.getKey(), entrySet.getValue());
        }
        return featureBuilder.buildFeature(null);
    }

    public void removeGeometry() {
        if (PORTAREA.equalsIgnoreCase(type) && properties.containsKey(AREA_GEOMETRY)) {
            properties.put(EXTENT, getExtend(properties.get(AREA_GEOMETRY)));
            properties.remove(AREA_GEOMETRY);
            if (properties.containsKey(GEOMETRY)) {
                properties.remove(GEOMETRY);
            }
        } else if (properties.containsKey(GEOMETRY)) {
            properties.put(EXTENT, getExtend(properties.get(GEOMETRY)));
            properties.remove(GEOMETRY);
        }
    }

    protected String getExtend(Object geometry) {
        String extent = null;
        try {
            if (geometry != null) {
                Geometry geom = new WKTReader().read(geometry.toString());
                extent = new WKTWriter().write(geom.getEnvelope());
            }
            return extent;
        } catch (ParseException e) {
            return geometry==null?null:geometry.toString();
        }
    }


}
