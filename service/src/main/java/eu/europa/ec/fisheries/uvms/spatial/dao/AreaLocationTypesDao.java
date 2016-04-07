package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import javax.persistence.EntityManager;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.*;
import static eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity.*;

public class AreaLocationTypesDao extends AbstractDAO<AreaLocationTypesEntity> {

    private EntityManager em;

    public AreaLocationTypesDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<AreaLocationTypesEntity> findByTypeName(final String typeName) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class,FIND_TYPE_BY_NAME,with("typeName", typeName).parameters());
    }

    public List<AreaLocationTypesEntity> findByIsLocationAndIsSystemWide(Boolean isLocation, Boolean isSystemWide ) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE,
                with("isLocation", isLocation).and("isSystemWide", isSystemWide).parameters());
    }

    public List<AreaLocationTypesEntity> findByIsLocation(Boolean isLocation) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, FIND_ALL_IS_LOCATION,
                with("isLocation", isLocation).parameters());
    }
}
