package service.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;


public class GeometryMapper  {

    public static String geometryToWkt(Geometry geometry) {
        if ( geometry == null ) {
            return null;
        }


        return new WKTWriter().write(geometry) ;
    }

    public static  Geometry wktToGeometry(String wkt) throws ParseException {
        if ( wkt == null ) {
            return null;
        }

       return  new WKTReader().read(wkt) ;

    }



}
