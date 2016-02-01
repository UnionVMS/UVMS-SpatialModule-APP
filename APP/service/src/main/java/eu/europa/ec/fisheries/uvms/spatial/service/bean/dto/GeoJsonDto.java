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
    private static final String EXTENT = "extent";
    private static final String PORTAREA = "PORTAREA";
    protected String type;

    @JsonDeserialize(using = GeometryDeserializer.class)
    protected Geometry geometry;

    protected Map<String, String> properties = new HashMap<String, String>();

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

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void add(String key, String value) {
        properties.put(key, value);
    }

    protected SimpleFeatureType build(Class geometryType, Map<String, String> properties, String geometryFieldName) {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName(type.toUpperCase());
        for (String key : properties.keySet()) {
            if (key.equalsIgnoreCase(geometryFieldName)) {
                sb.add(key, geometryType);
            } else {
                sb.add(key, String.class);
            }
        }
        return sb.buildFeatureType();
    }

    public SimpleFeature toFeature(Class geometryType) throws ParseException {
        return toFeature(geometryType, properties);
    }

    public SimpleFeature toFeature(Class geometryType, Map<String, String> properties) throws ParseException {
        SimpleFeatureBuilder featureBuilder;
        if (PORTAREA.equalsIgnoreCase(type)) {
            featureBuilder = new SimpleFeatureBuilder(build(geometryType, properties, AREA_GEOMETRY));
        } else {
            featureBuilder = new SimpleFeatureBuilder(build(geometryType, properties, GEOMETRY));
        }

        for (Entry<String, String> entrySet : properties.entrySet()) {
            featureBuilder.set(entrySet.getKey(), entrySet.getValue());
        }
        return featureBuilder.buildFeature(null);
    }

    public void removeGeometry() {
        if (PORTAREA.equalsIgnoreCase(type)) {
            if (properties.containsKey(AREA_GEOMETRY)) {
                properties.put(EXTENT, getExtend(properties.get(AREA_GEOMETRY)));
                properties.remove(AREA_GEOMETRY);
            }
        }
        if (properties.containsKey(GEOMETRY)) {
            properties.put(EXTENT, getExtend(properties.get(GEOMETRY)));
            properties.remove(GEOMETRY);
        }
    }

    protected String getExtend(String geometry) {
        String extent = null;
        try {
            if (geometry != null) {
                Geometry geom = new WKTReader().read(geometry);
                extent = new WKTWriter().write(geom.getEnvelope());
            }
            return extent;
        } catch (ParseException e) {
            return geometry;
        }
    }
}
