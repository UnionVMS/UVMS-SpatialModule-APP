package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * //TODO create test
 * //TODO find better name
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VmsDto {

    private List<VesselDto> vessels;
    private List<MovementDto> movements;

    public List<MovementDto> getMovements() {
        return movements;
    }

    public void setMovements(List<MovementDto> movements) {
        this.movements = movements;
    }

    public List<VesselDto> getVessels() {
        return vessels;
    }

    public void setVessels(List<VesselDto> vessels) {
        this.vessels = vessels;
    }
}
