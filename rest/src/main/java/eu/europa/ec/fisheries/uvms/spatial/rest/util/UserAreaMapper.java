package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.GeometryUtils;
import org.locationtech.jts.io.ParseException;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class UserAreaMapper {

    public static UserAreasEntity mapToEntity(UserAreaDto dto) throws ParseException {
        UserAreasEntity entity = new UserAreasEntity();
        entity.setAreaDesc(dto.getDescription());
        entity.setCreatedOn(Instant.now());
        entity.setDatasetName(dto.getDatasetName());
        entity.setEndDate(DateUtils.stringToDate(dto.getEndDate()));
        entity.setStartDate(DateUtils.stringToDate(dto.getStartDate()));
        entity.setId( (dto.getId() == null || dto.getId().equals("")) ? null : Long.valueOf(dto.getId()));

        entity.setType(AreaType.USERAREA);
        entity.setAreaGroup(dto.getAreaGroup());

        String code = dto.getName();
        if(code != null && code.length() > 20){
            code = code.substring(0 , 20);
        }
        entity.setCode(code);
        entity.setName(dto.getName());
        entity.setEnabled( (entity.getStartDate().isBefore(Instant.now()) && entity.getEndDate().isAfter(Instant.now())) );     //if now is between start and end
        entity.setEnabledOn(entity.getEnabled() ? Instant.now() : null);

        Set<UserScopeEntity> scopes = new HashSet<>(dto.getScopeSelection().size());
        for (String s : dto.getScopeSelection()) {
            UserScopeEntity scope = new UserScopeEntity();
            scope.setName(s);
            scope.setUserArea(entity);
            scopes.add(scope);
        }
        entity.setScopeSelection(scopes);

        entity.setGeom(GeometryUtils.wktToGeometry(dto.getGeometry()));

        return entity;
    }
}
