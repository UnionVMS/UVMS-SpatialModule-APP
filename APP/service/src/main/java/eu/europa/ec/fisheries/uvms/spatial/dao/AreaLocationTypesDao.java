package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import javax.persistence.EntityManager;
import java.util.List;

public class AreaLocationTypesDao extends AbstractDAO<AreaLocationTypesEntity> {

    private EntityManager em;

    public AreaLocationTypesDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public AreaLocationTypesEntity findBy(final String typeName) throws ServiceException {

        List<AreaLocationTypesEntity> result =
                findEntityByNamedQuery(AreaLocationTypesEntity.class,
                        AreaLocationTypesEntity.FIND_TYPE_BY_NAME,
                        QueryParameter.with("typeName", typeName).parameters(), 1);

        AreaLocationTypesEntity entity = null;
        if (result != null){
            entity = result.get(0);
        }
        return entity;
    }

    public List<AreaLocationTypesEntity> findAllIsLocationIsSystemWide(Boolean isLocation, Boolean isSystemWide ) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE,
                QueryParameter.with("isLocation", isLocation).and("isSystemWide", isSystemWide).parameters());

    }

    public List<AreaLocationTypesEntity> findAllIsLocation(Boolean isLocation) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_ALL_IS_LOCATION,
                QueryParameter.with("isLocation", isLocation).parameters());

    }
}
