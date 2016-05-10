package eu.europa.ec.fisheries.uvms.spatial.dao.util;

import eu.europa.ec.fisheries.uvms.spatial.dao.AbstractGisFunction;

public class Oracle extends AbstractGisFunction {

    @Override
    public String stIntersects(Double latitude, Double longitude) {

        return null;
    }

    @Override
    public String stDistance(Double latitude, Double longitude) {

        return "SDO_GEOM.SDO_DISTANCE('geom', SDO_UTIL.FROM_WKTGEOMETRY('POINT(" + longitude + " " + latitude + "), 0.005)'";
    }

    @Override
    public String stClosestPoint(Double latitude, Double longitude) {

        return null;
    }

    @Override
    public String limit(int i) {

        return "ROWNUM <= " + i;
    }
}
