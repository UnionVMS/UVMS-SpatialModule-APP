package eu.europa.ec.fisheries.uvms.spatial.rest.type;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import lombok.experimental.Delegate;

public class MapSettingsType {

    @Delegate(excludes = Exclude.class)
    private MapConfigurationType mapConfigurationType;

    public MapSettingsType(MapConfigurationType mapConfigurationType) {
        this.mapConfigurationType = mapConfigurationType;
    }


    public interface Exclude {
        CoordinatesFormat getCoordinatesFormat();
        ScaleBarUnits getScaleBarUnits();
    }

    public String getCoordinatesFormat(){

        return mapConfigurationType.getCoordinatesFormat().value().toLowerCase();

    }

    public String getScaleBarUnits(){

        return mapConfigurationType.getScaleBarUnits().value().toLowerCase();

    }
}
