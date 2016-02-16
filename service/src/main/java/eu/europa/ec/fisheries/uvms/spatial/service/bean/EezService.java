package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;

public interface EezService {
    EezType getEezById(EezSpatialRQ getEezSpatialRQ);

    EezDto getEezById(int id);

    long createEzz(EezDto eezDto) throws ServiceException;

    int disableAllEezAreas() throws ServiceException;
}
