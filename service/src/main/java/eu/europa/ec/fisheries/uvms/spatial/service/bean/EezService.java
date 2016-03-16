package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.EezDto;

public interface EezService extends AreaDisableService { // FIXME illegal Ejb should only have max 2 interfaces LOCAL AND/OR REMOTE

    EezType getEezById(EezSpatialRQ getEezSpatialRQ);

    long createEzz(EezDto eezDto) throws ServiceException;
}
