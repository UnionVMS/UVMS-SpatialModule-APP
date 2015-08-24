package eu.europa.ec.fisheries.uvms.spatial.service.rest.dto;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import lombok.experimental.Delegate;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
public class AreaDto {

    @Delegate(types = Include.class)
    private AreaType areaType;

    public AreaDto(String id, String areaType) {
        setId(id);
        setAreaType(areaType);
    }

    private interface Include {
        public String getId();

        public void setId(String id);

        public String getAreaType();

        public void setAreaType(String areaType);
    }

}
