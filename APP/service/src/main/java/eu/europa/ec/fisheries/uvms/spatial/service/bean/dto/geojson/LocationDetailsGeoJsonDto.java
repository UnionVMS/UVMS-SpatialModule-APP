package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;

public class LocationDetailsGeoJsonDto extends GeoJsonDto {

    public SimpleFeature toFeature() throws ParseException {
        return super.toFeature(Point.class);
    }

}
