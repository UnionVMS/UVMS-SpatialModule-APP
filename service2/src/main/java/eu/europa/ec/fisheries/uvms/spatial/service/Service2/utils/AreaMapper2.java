package eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.AreaLayerDto2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.BaseAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.AreaLocationTypesEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.BaseAreaEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.UserAreasEntity2;

import java.util.ArrayList;
import java.util.List;

public class AreaMapper2 {

    public static List<AreaSimpleType> mapToAreaSimpleType(List<? extends BaseAreaEntity2> baseList, AreaType areaType){
        List<AreaSimpleType> responseList = new ArrayList<>();
        for (BaseAreaEntity2 area: baseList) {
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

    public static List<BaseAreaDto> mapToBaseAreaDtoList(List<UserAreasEntity2> entityList){
        List<BaseAreaDto> dtoList = new ArrayList<>();
        for (UserAreasEntity2 entity: entityList) {
            dtoList.add(mapToBaseAreaDto(entity));
        }
        return dtoList;
    }

    public static BaseAreaDto mapToBaseAreaDto(UserAreasEntity2 entity){
        BaseAreaDto dto = new BaseAreaDto(entity.getType(), entity.getId(), entity.getCode(), entity.getName());
        return dto;
    }

}
