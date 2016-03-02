package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.layer.ServiceLayer;
import eu.europa.ec.fisheries.uvms.spatial.repository.LayerRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ServiceLayerMapper;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(ServiceLayerService.class)
@Slf4j
public class ServiceLayerServiceBean implements ServiceLayerService {

    @EJB
    private LayerRepository layerRepository;

    @Override
    public ServiceLayer findBy(final String name) throws ServiceException {

        if (name == null){
            throw new ServiceException("Service layer null not allowed");
        }

        ServiceLayerEntity entity = layerRepository.getServiceLayerBy(name);

        return ServiceLayerMapper.INSTANCE.serviceLayerEntityToServiceLayer(entity);

    }
}
