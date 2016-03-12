package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices;

import eu.europa.ec.fisheries.uvms.spatial.model.area.SystemAreaDto;

import java.util.List;

public class EnrichmentDto {
    private List<SystemAreaDto> areasByLocation;
    private List<ClosestAreaDto> closestAreas;
    private List<ClosestLocationDto> closestLocations;

    public EnrichmentDto() {
    }

    public EnrichmentDto(List<SystemAreaDto> areasByLocation, List<ClosestAreaDto> closestAreas, List<ClosestLocationDto> closestLocations) {
        this.areasByLocation = areasByLocation;
        this.closestAreas = closestAreas;
        this.closestLocations = closestLocations;
    }

    public List<SystemAreaDto> getAreasByLocation() {
        return areasByLocation;
    }

    public void setAreasByLocation(List<SystemAreaDto> areasByLocation) {
        this.areasByLocation = areasByLocation;
    }

    public List<ClosestAreaDto> getClosestAreas() {
        return closestAreas;
    }

    public void setClosestAreas(List<ClosestAreaDto> closestAreas) {
        this.closestAreas = closestAreas;
    }

    public List<ClosestLocationDto> getClosestLocations() {
        return closestLocations;
    }

    public void setClosestLocations(List<ClosestLocationDto> closestLocations) {
        this.closestLocations = closestLocations;
    }
}
