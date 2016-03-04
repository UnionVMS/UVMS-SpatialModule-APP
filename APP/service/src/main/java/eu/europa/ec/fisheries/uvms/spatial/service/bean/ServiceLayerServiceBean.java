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
import javax.transaction.Transactional;

@Stateless
@Local(ServiceLayerService.class)
@Slf4j
public class ServiceLayerServiceBean implements ServiceLayerService {

    @EJB
    private LayerRepository repository;

    @Override
    public ServiceLayer findBy(final String locationType) throws ServiceException {

        if (locationType == null){
            throw new ServiceException("locationType null not allowed");
        }

        ServiceLayerEntity entity = repository.getServiceLayerBy(locationType);

        return ServiceLayerMapper.INSTANCE.serviceLayerEntityToServiceLayer(entity);

    }

    @Override
    @Transactional
    public void update(final ServiceLayer serviceLayer) throws ServiceException {

        if (serviceLayer == null){
            throw new ServiceException("serviceLayer null not allowed");
        }

        ServiceLayerEntity serviceLayerBy = repository.getServiceLayerBy(serviceLayer.getId());
        merge(serviceLayer, serviceLayerBy);

    }

    private void merge(ServiceLayer source, ServiceLayerEntity target) {

        if(source.getName() != null)
            target.setName( source.getName() );

        if( source.getLayerDesc() != null)
            target.setLayerDesc( source.getLayerDesc() );

        if( source.getGeoName() != null)
            target.setGeoName( source.getGeoName() );

        if( source.getSrsCode() != null)
            target.setSrsCode( source.getSrsCode() );

        if( source.getShortCopyright() != null)
            target.setShortCopyright( source.getShortCopyright() );

        if( source.getLongCopyright() != null)
            target.setLongCopyright( source.getLongCopyright() );

        if(  source.getStyleGeom() != null)
            target.setStyleGeom( source.getStyleGeom() );
    }
}
