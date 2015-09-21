package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.io.ParseException;

public abstract class GeoJsonDto {
	
	private static String GEOMETRY = "geometry";
	
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

	protected SimpleFeatureType build(Class geometryType) {
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
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(build(geometryType));
        for (Entry<String, String> entrySet : properties.entrySet()) {
        	featureBuilder.set(entrySet.getKey(), entrySet.getValue());
        }
        return featureBuilder.buildFeature(null);
    }
    
    public Map<String, String> removeGeometry() {
    	if(properties.containsKey(GEOMETRY)) {
    		properties.remove(GEOMETRY);
    	}
    	return properties;
    }

}
