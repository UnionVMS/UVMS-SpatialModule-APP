package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

public abstract class GeoJsonDto {
	
	private static final String GEOMETRY = "geometry";
	
	private static final String EXTENT = "extent";
	
	protected Map<String, String> properties = new HashMap<String, String>();
	
	protected String type;
	
	public void add(String key, String value) {
		properties.put(key, value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
    public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	protected SimpleFeatureType build(Class geometryType, Map<String, String> properties) {
        SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
        sb.setCRS(DefaultGeographicCRS.WGS84);
        sb.setName(type.toUpperCase());
        for (String key : properties.keySet()) {
        	if (key.equalsIgnoreCase(GEOMETRY)) {
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
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(build(geometryType, properties));
        for (Entry<String, String> entrySet : properties.entrySet()) {
        	featureBuilder.set(entrySet.getKey(), entrySet.getValue());
        }
        return featureBuilder.buildFeature(null);
    }
    
    public Map<String, String> removeGeometry() {
    	if(properties.containsKey(GEOMETRY)) {
    		properties.put(EXTENT, getExtend(properties.get(GEOMETRY)));
    		properties.remove(GEOMETRY);
    	}
    	return properties;   	
    }

    protected String getExtend(String geometry) {
    	try {    	
    		Geometry geom = new WKTReader().read(geometry);
    		return new WKTWriter().write(geom.getEnvelope());
    	} catch (ParseException e) {
    		return geometry;
    	}
    }
}
