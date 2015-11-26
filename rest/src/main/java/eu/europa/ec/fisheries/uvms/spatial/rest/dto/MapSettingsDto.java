package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import lombok.experimental.Delegate;

public class MapSettingsDto {

    @Delegate(excludes = Exclude.class)
    private MapConfigurationType mapConfigurationType;

    public MapSettingsDto(MapConfigurationType mapConfigurationType) {
        this.mapConfigurationType = mapConfigurationType;
    }

    public interface Exclude {
        Long getReportId();
    }
}
