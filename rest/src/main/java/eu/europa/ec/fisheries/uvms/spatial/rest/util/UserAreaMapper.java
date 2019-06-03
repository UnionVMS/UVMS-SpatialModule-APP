package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.movement.model.util.DateUtil;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.GeometryUtils;

import java.time.Instant;

public class UserAreaMapper {

    public static UserAreasEntity mapToEntity(UserAreaDto dto) throws ParseException {
        UserAreasEntity entity = new UserAreasEntity();
        entity.setAreaDesc(dto.getDescription());
        entity.setCreatedOn(Instant.now());
        entity.setDatasetName(dto.getDatasetName());
        entity.setEndDate(DateUtil.parseToUTCDate(dto.getEndDate()));
        entity.setStartDate(DateUtil.parseToUTCDate(dto.getStartDate()));
        entity.setId( (dto.getId() == null || dto.getId().equals("")) ? null : Long.valueOf(dto.getId()));
        entity.setType(dto.getType());

        String code = dto.getName();
        if(code != null && code.length() > 20){
            code = code.substring(0 , 20);
        }
        entity.setCode(code);
        entity.setName(dto.getName());
        entity.setEnabled( (entity.getStartDate().isBefore(Instant.now()) && entity.getEndDate().isAfter(Instant.now())) );     //if now is between start and end
        entity.setEnabledOn(entity.getEnabled() ? Instant.now() : null);

        entity.setGeom(GeometryUtils.wktToGeometry(dto.getGeometry()));

        return entity;
    }
}
