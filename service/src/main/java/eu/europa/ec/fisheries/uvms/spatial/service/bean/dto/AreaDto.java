package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import lombok.experimental.Delegate;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
public class AreaDto {

    @Delegate(types = Include.class)
    private AreaType areaType;

    public AreaDto(String id, String areaTypeName) {
        this.areaType = new AreaType(id, areaTypeName);
    }

    private interface Include {
        String getId();

        void setId(String id);

        String getAreaTypeName();

        void setAreaTypeName(String areaType);
    }

}
