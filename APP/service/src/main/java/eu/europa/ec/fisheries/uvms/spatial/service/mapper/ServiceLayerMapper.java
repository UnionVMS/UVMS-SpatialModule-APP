package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.layer.ServiceLayer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceLayerMapper {

    ServiceLayerMapper INSTANCE = Mappers.getMapper(ServiceLayerMapper.class);

    ServiceLayer serviceLayerEntityToServiceLayer(ServiceLayerEntity serviceLayer);

    void merge(ServiceLayerEntity source, @MappingTarget ServiceLayerEntity target);

    void merge(ServiceLayerEntity source, @MappingTarget ServiceLayer target);

    void merge(ServiceLayer source, @MappingTarget ServiceLayerEntity target);

}
