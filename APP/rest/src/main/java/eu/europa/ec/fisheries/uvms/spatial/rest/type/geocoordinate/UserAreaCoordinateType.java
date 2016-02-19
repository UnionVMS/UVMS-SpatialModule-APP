package eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate;

import eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum;

import java.io.Serializable;

public class UserAreaCoordinateType extends AreaCoordinateType implements Serializable {


    public UserAreaCoordinateType() {
        super.setAreaType(SpatialTypeEnum.USERAREA.getType());
    }

}
