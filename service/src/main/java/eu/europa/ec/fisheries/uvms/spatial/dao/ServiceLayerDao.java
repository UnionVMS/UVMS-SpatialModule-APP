package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
public class ServiceLayerDao extends AbstractDAO<ServiceLayerEntity> {

    private EntityManager em;

    public ServiceLayerDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @SuppressWarnings("unchecked")
    public ServiceLayerEntity getBy(String locationType) throws ServiceException {

        ServiceLayerEntity serviceLayerEntity = null;

        try {
            List<ServiceLayerEntity> layers = findEntityByNamedQuery(ServiceLayerEntity.class, ServiceLayerEntity.BY_LOCATION_TYPE,
                    QueryParameter.with("locationType", locationType).parameters(), 1);

            if (layers != null && layers.size() == 1) {
                serviceLayerEntity = layers.get(0);
            }

        }
        catch (Exception e){
            String error = "Error when trying to fetch service layer from db";
            log.error(error);
            throw new ServiceException(error);
        }

        return serviceLayerEntity;

    }

    public ServiceLayerEntity getByAreaLocationType(String areaLocationType) throws ServiceException {
        List<ServiceLayerEntity> serviceLayers = findEntityByNamedQuery(ServiceLayerEntity.class, ServiceLayerEntity.BY_AREA_LOCATION_TYPE,
                QueryParameter.with("typeName", areaLocationType).parameters(), 1);
        if (serviceLayers != null && !serviceLayers.isEmpty()) {
            return serviceLayers.get(0);
        }
        return null;
    }
}