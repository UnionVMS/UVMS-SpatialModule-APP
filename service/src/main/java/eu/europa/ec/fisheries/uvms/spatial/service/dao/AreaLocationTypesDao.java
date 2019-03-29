/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProviderFormatEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter.with;
import static eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity.*;

@Stateless
public class AreaLocationTypesDao extends AbstractDAO<AreaLocationTypesEntity> {

    @PersistenceContext
    private EntityManager em;

    public AreaLocationTypesDao(){};

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public AreaLocationTypesEntity findOneByTypeName(final String typeName) throws ServiceException {
        AreaLocationTypesEntity result = null;
        List<AreaLocationTypesEntity> resultList =
                findEntityByNamedQuery(AreaLocationTypesEntity.class, FIND_TYPE_BY_NAME,
                        with("typeName", typeName).parameters(), 1);
        if(CollectionUtils.isNotEmpty(resultList)){
            result = resultList.get(0);
        }
        return result;
    }

    public List<AreaLocationTypesEntity> findByIsLocationAndIsSystemWide(Boolean isLocation, Boolean isSystemWide ) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE,
                with("isLocation", isLocation).and("isSystemWide", isSystemWide).parameters());
    }

    public List<AreaLocationTypesEntity> findByIsLocation(Boolean isLocation) throws ServiceException {
        return findEntityByNamedQuery(AreaLocationTypesEntity.class, FIND_ALL_IS_LOCATION,
                with("isLocation", isLocation).parameters());
    }

/*
    SELECT area.typeName as typeName,
           layer.geoName as geoName,
           layer.isInternal as isInternal,
           layer.serviceUrl as serviceUrl,
           layer.styleLabelGeom as style,
            provider.serviceType as serviceType

             FROM AreaLocationTypesEntity as area
             INNER JOIN area.serviceLayer as layer
             INNER JOIN layer.providerFormat as provider
*/

    public List<UserAreaLayerDto> findUserAreaLayerMapping() {

            List<UserAreaLayerDto> returnList = new ArrayList<>();
            javax.persistence.Query qry = em.createNamedQuery(AreaLocationTypesEntity.FIND_USER_AREA_LAYER);
            List<AreaLocationTypesEntity> rs = qry.getResultList();
            for(AreaLocationTypesEntity rec : rs){
                UserAreaLayerDto mapped = new UserAreaLayerDto();

                mapped.setTypeName(rec.getTypeName());

                ServiceLayerEntity serviceLayer = rec.getServiceLayer();
                if(serviceLayer != null) {
                    mapped.setIsInternal(serviceLayer.getIsInternal());
                    mapped.setGeoName(serviceLayer.getGeoName());
                    mapped.setAreaTypeDesc(serviceLayer.getLayerDesc());
                    mapped.setServiceUrl(serviceLayer.getServiceUrl());
                    mapped.setStyle(serviceLayer.getStyleLabelGeom());

                    AreaLocationTypesEntity areaLocationTypes = serviceLayer.getAreaType();
                    if(areaLocationTypes != null){
                        mapped.setIsLocation(areaLocationTypes.getIsLocation());
                        mapped.setServiceType(areaLocationTypes.getTypeName());
                    }

                    ProviderFormatEntity providerFormatEntity = serviceLayer.getProviderFormat();
                    if(providerFormatEntity != null){
                        List<Long> idList = new ArrayList<>();
                        Set<ServiceLayerEntity> set=  providerFormatEntity.getServiceLayers();
                        if(set != null){
                            for(ServiceLayerEntity sl : set){
                                idList.add(sl.getId());
                            }
                        }
                        mapped.setIdList(idList);
                    }
                }
                returnList.add(mapped);
            }
            return returnList;
    }


    public List<AreaLayerDto> findSystemAreaLayerMapping() {
        Query query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity.FIND_SYSTEM_AREA_LAYER);
        return query.setResultTransformer(Transformers.aliasToBean(AreaLayerDto.class)).list();
    }

    public List<AreaLayerDto> findSystemAreaAndLocationLayerMapping() {
        Query query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity.FIND_SYSTEM_AREA_AND_LOCATION_LAYER);
        return query.setResultTransformer(Transformers.aliasToBean(AreaLayerDto.class)).list();
    }

}