package eu.europa.ec.fisheries.uvms.spatial.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.europa.ec.fisheries.uvms.movement.MovementDto;

import java.util.List;

/**
 * //TODO create test
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpatialDto {

    private List<VesselDto> vessels;
    private List<MovementDto> movements;

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
