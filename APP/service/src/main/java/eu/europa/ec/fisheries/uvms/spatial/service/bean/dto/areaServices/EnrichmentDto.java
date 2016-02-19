package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices;

import java.util.List;

public class EnrichmentDto {
    private List<AreaExtendedIdentifierDto> areasByLocation;
    private List<ClosestAreaDto> closestAreas;
    private List<ClosestLocationDto> closestLocations;

    public EnrichmentDto() {
    }

    public EnrichmentDto(List<AreaExtendedIdentifierDto> areasByLocation, List<ClosestAreaDto> closestAreas, List<ClosestLocationDto> closestLocations) {
        this.areasByLocation = areasByLocation;
        this.closestAreas = closestAreas;
        this.closestLocations = closestLocations;
    }

    public List<AreaExtendedIdentifierDto> getAreasByLocation() {
        return areasByLocation;
    }

    public void setAreasByLocation(List<AreaExtendedIdentifierDto> areasByLocation) {
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
