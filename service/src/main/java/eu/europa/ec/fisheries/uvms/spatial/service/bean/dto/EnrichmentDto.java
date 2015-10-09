package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import java.util.List;

/**
 * Created by Michal Kopyczok on 09-Sep-15.
 */
public class EnrichmentDto {
    private List<AreaIdentifierDto> areasByLocation;
    private List<ClosestAreaDto> closestAreas;
    private List<ClosestLocationDto> closestLocations;

    public EnrichmentDto() {
    }

    public EnrichmentDto(List<AreaIdentifierDto> areasByLocation, List<ClosestAreaDto> closestAreas, List<ClosestLocationDto> closestLocations) {
        this.areasByLocation = areasByLocation;
        this.closestAreas = closestAreas;
        this.closestLocations = closestLocations;
    }

    public List<AreaIdentifierDto> getAreasByLocation() {
        return areasByLocation;
    }

    public void setAreasByLocation(List<AreaIdentifierDto> areasByLocation) {
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
