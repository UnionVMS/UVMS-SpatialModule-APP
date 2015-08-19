package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeSpatialRS;

/**
 * Created by kopyczmi on 03-Aug-15.
 */
public interface AreaTypeService {
    AreaTypeSpatialRS getAreaTypes() throws CommonGenericDAOException;
}
