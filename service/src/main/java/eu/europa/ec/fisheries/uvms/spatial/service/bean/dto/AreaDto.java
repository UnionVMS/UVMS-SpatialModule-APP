package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import lombok.experimental.Delegate;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
public class AreaDto {

    @Delegate(types = Include.class)
    private AreaTypeEntry areaTypeName;

    public AreaDto(String id, String areaType) {
        this.areaTypeName = new AreaTypeEntry(id, areaType);
    }

    private interface Include {
        String getId();

        String getAreaType();
    }

}
