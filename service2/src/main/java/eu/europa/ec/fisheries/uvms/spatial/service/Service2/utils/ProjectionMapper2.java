package eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.ProjectionDto2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.ProjectionEntity2;

import java.util.ArrayList;
import java.util.List;

public class ProjectionMapper2 {

    public static List<ProjectionDto2> mapToProjectionDto(List<ProjectionEntity2> entityList){
        List<ProjectionDto2> dtoList = new ArrayList<>();
        for (ProjectionEntity2 entity:entityList) {
            dtoList.add(new ProjectionDto2(entity.getId(), entity.getName(), entity.getSrsCode(), entity.getUnits(), entity.getFormats(), entity. getWorld(), entity.getExtent()));
        }

        return dtoList;
    }
}
