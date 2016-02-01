package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

/**
 * Created by Georgi on 23-Nov-15.
 */

import eu.europa.ec.fisheries.uvms.spatial.entity.config.SysConfigEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.SysConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface SysConfigMapper {
    SysConfigMapper INSTANCE = Mappers.getMapper(SysConfigMapper.class);

    SysConfig fromEntityToDTO(SysConfigEntity entity);

    SysConfigEntity fromDTOToEntity(SysConfig dto);

    List<SysConfig> fromEntityToDTO(List<SysConfigEntity> entity);

    List<SysConfigEntity> fromDTOToEntity(List<SysConfig> dto);
}
