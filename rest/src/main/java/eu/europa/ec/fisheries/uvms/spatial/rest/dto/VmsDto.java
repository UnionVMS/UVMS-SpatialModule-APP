package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

/**
 * //TODO create test
 * //TODO find better name
 */
public class VmsDto {

    private VesselDto vessel;
    private MovementDto movement;

    public VmsDto() {
    }

    public VesselDto getVessel() {
        return vessel;
    }

    public void setVessel(VesselDto vessel) {
        this.vessel = vessel;
    }

    public MovementDto getMovement() {
        return movement;
    }

    public void setMovement(MovementDto movement) {
        this.movement = movement;
    }
}
