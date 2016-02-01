package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum;

import java.io.Serializable;

public class UserAreaTypeDto extends AreaTypeDto implements Serializable {


    public UserAreaTypeDto() {
        super.setAreaType(SpatialTypeEnum.USERAREA.getType());
    }

}