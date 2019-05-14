package eu.europa.ec.fisheries.uvms.spatial.service.utils;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.BaseAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;

import java.util.ArrayList;
import java.util.List;

public class AreaMapper {

    public static List<AreaSimpleType> mapToAreaSimpleType(List<? extends BaseAreaEntity> baseList, AreaType areaType){
        List<AreaSimpleType> responseList = new ArrayList<>();
        for (BaseAreaEntity area: baseList) {
            AreaSimpleType areaSimpleType = new AreaSimpleType(areaType.value(), area.getCode(), area.getGeometry());
            responseList.add(areaSimpleType);
        }
        return responseList;
    }


    public static List<AreaExtendedIdentifierType> mapToAreaExtendedIdentifierType(List<BaseAreaDto> areaList){
        List<AreaExtendedIdentifierType> response = new ArrayList<>();

        for (BaseAreaDto area: areaList) {
            AreaExtendedIdentifierType areaExtendedIdentifierType = new AreaExtendedIdentifierType(String.valueOf(area.getGid()), area.getType(), area.getCode(), area.getName());
            response.add(areaExtendedIdentifierType);
        }
        return response;
    }

    public static List<BaseAreaDto> mapToBaseAreaDtoList(List<UserAreasEntity> entityList){
        List<BaseAreaDto> dtoList = new ArrayList<>();
        for (UserAreasEntity entity: entityList) {
            dtoList.add(mapToBaseAreaDto(entity));
        }
        return dtoList;
    }

    public static BaseAreaDto mapToBaseAreaDto(UserAreasEntity entity){
        BaseAreaDto dto = new BaseAreaDto(entity.getType(), entity.getId(), entity.getCode(), entity.getName());
        return dto;
    }

}
