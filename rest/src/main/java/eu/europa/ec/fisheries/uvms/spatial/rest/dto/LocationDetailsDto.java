package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

public class LocationDetailsDto extends GeoJsonDto {
	
    public SimpleFeature toFeature() throws ParseException {
    	return super.toFeature(Point.class);
    }
}
