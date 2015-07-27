package eu.europa.ec.fisheries.uvms.spatial.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europa.ec.fisheries.uvms.movement.MovementDto;
import eu.europa.ec.fisheries.uvms.movement.MovementDtoListSerializer;
import eu.europa.ec.fisheries.uvms.vessel.dto.VesselDto;

import java.util.List;

/**
 * //TODO create test
 */
public class SpatialDto {

    private List<VesselDto> vessels;

    @JsonSerialize(using = MovementDtoListSerializer.class)
    private List<MovementDto> movements;//todo change this to FeatureCollection

    public List<VesselDto> getVessels() {
        return vessels;
    }

    public void setVessels(List<VesselDto> vessels) {
        this.vessels = vessels;
    }

    public List<MovementDto> getMovements() {
        return movements;
    }

    public void setMovements(List<MovementDto> movements) {
        this.movements = movements;
    }
}
