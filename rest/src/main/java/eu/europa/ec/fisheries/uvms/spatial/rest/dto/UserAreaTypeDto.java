package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.io.Serializable;

public class UserAreaTypeDto extends AreaTypeDto implements Serializable {

    private static final String USER_AREA = "USER_AREA";

    public UserAreaTypeDto() {
        super.setAreaType(USER_AREA);
    }

}
