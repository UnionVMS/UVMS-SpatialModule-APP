package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.spatial.dto.SpatialDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.entity.Eez;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SpatialService {

    List<SpatialDto> getListAsRestDto(String spatialQuery) throws SpatialServiceException;

    public Eez getEezById(int eezId);

}
