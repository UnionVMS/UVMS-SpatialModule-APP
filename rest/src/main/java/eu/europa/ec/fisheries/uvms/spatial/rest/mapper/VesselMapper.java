package eu.europa.ec.fisheries.uvms.spatial.rest.mapper;

import eu.europa.ec.fisheries.uvms.spatial.service.dto.VesselDto;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * //TODO add test
 */
@Mapper
public interface VesselMapper {

    VesselMapper INSTANCE = Mappers.getMapper(VesselMapper.class);

    VesselDto vesselToVesselDto(Vessel vessel);
}
