/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.layer.ServiceLayer;
import eu.europa.ec.fisheries.uvms.spatial.service.ServiceLayerService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
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
    private SpatialRepository repository;

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