package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import lombok.experimental.Delegate;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
public class AreaDto {

    @Delegate(types = Include.class)
    private AreaTypeEntry areaType;

    public AreaDto(String id, String areaTypeName) {
        this.areaType = new AreaTypeEntry(id, areaTypeName);
    }

    private interface Include {
        String getId();

        String getAreaTypeName();
    }

}
