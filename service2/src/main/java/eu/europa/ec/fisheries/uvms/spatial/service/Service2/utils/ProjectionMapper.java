package eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.ProjectionEntity;

import java.util.ArrayList;
import java.util.List;

public class ProjectionMapper {

    public static List<ProjectionDto> mapToProjectionDto(List<ProjectionEntity> entityList){
        List<ProjectionDto> dtoList = new ArrayList<>();
        for (ProjectionEntity entity:entityList) {
            dtoList.add(new ProjectionDto(entity.getId(), entity.getName(), entity.getSrsCode(), entity.getUnits(), entity.getFormats(), entity. getWorld(), entity.getExtent()));
        }

        return dtoList;
    }
}
