package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.ServiceLayerDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.LayerRepository;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(value = LayerRepository.class)
public class LayerRepositoryBean implements LayerRepository {

    private @PersistenceContext EntityManager em;

    private ServiceLayerDao serviceLayerDao;

    @PostConstruct
    public void init() {
        serviceLayerDao = new ServiceLayerDao(em);
    }

    @Override
    public ServiceLayerEntity getServiceLayerBy(String locationType) throws ServiceException {
        return serviceLayerDao.getBy(locationType);
    }

    @Override
    public ServiceLayerEntity getServiceLayerBy(Long id) throws ServiceException {
        return serviceLayerDao.findEntityById(ServiceLayerEntity.class, id);
    }

    @Override
    public ServiceLayerEntity getByAreaLocationType(String areaLocationType) throws ServiceException {
        return  serviceLayerDao.getByAreaLocationType(areaLocationType);
    }
}
