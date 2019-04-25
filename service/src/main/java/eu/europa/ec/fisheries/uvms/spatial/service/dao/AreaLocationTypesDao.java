/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProviderFormatEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Stateless
public class AreaLocationTypesDao {

    @PersistenceContext
    private EntityManager em;

    public AreaLocationTypesEntity findOneByTypeName(final String typeName) throws ServiceException {
        AreaLocationTypesEntity result = null;
        TypedQuery<AreaLocationTypesEntity> qry = em.createNamedQuery(AreaLocationTypesEntity.FIND_TYPE_BY_NAME,AreaLocationTypesEntity.class);
        qry.setParameter("typeName", typeName);
        qry.setMaxResults(1);
        List<AreaLocationTypesEntity> rs = qry.getResultList();
        if (rs.size() > 0) {
            result = rs.get(0);
        }
        return result;
    }


    public List<AreaLocationTypesEntity> findByIsLocationAndIsSystemWide(Boolean isLocation, Boolean isSystemWide) throws ServiceException {
        TypedQuery<AreaLocationTypesEntity> qry = em.createNamedQuery(AreaLocationTypesEntity.FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE,AreaLocationTypesEntity.class);
        qry.setParameter("isLocation", isLocation);
        qry.setParameter("isSystemWide", isSystemWide);
        return qry.getResultList();
    }

    public List<AreaLocationTypesEntity> findByIsLocation(Boolean isLocation) throws ServiceException {
        TypedQuery<AreaLocationTypesEntity> qry = em.createNamedQuery(AreaLocationTypesEntity.FIND_ALL_IS_LOCATION,AreaLocationTypesEntity.class);
        qry.setParameter("isLocation", isLocation);
        return qry.getResultList();
    }

    public List<UserAreaLayerDto> findUserAreaLayerMapping() {

        List<UserAreaLayerDto> returnList = new ArrayList<>();
        Query qry = em.createNamedQuery(AreaLocationTypesEntity.FIND_USER_AREA_LAYER);
        List<AreaLocationTypesEntity> rs = qry.getResultList();
        for (AreaLocationTypesEntity rec : rs) {
            UserAreaLayerDto mapped = new UserAreaLayerDto();
            mapped.setTypeName(rec.getTypeName());

            ServiceLayerEntity serviceLayer = rec.getServiceLayer();
            if (serviceLayer != null) {
                mapped.setIsInternal(serviceLayer.getIsInternal());
                mapped.setGeoName(serviceLayer.getGeoName());
                mapped.setAreaTypeDesc(serviceLayer.getLayerDesc());
                mapped.setServiceUrl(serviceLayer.getServiceUrl());
                mapped.setStyle(serviceLayer.getStyleLabelGeom());

                AreaLocationTypesEntity areaLocationTypes = serviceLayer.getAreaType();
                if (areaLocationTypes != null) {
                    mapped.setIsLocation(areaLocationTypes.getIsLocation());
                    mapped.setServiceType(areaLocationTypes.getTypeName());
                }

                ProviderFormatEntity providerFormatEntity = serviceLayer.getProviderFormat();
                if (providerFormatEntity != null) {
                    List<Long> idList = new ArrayList<>();
                    Set<ServiceLayerEntity> set = providerFormatEntity.getServiceLayers();
                    if (set != null) {
                        for (ServiceLayerEntity sl : set) {
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

        List<AreaLayerDto> returnList = new ArrayList<>();
        Query qry = em.createNamedQuery(AreaLocationTypesEntity.FIND_SYSTEM_AREA_LAYER);
        List<AreaLocationTypesEntity> rs = qry.getResultList();
        for (AreaLocationTypesEntity rec : rs) {
            AreaLayerDto mapped = new AreaLayerDto();

            mapped.setTypeName(rec.getTypeName());

            ServiceLayerEntity serviceLayer = rec.getServiceLayer();
            if (serviceLayer != null) {
                mapped.setIsInternal(serviceLayer.getIsInternal());
                mapped.setGeoName(serviceLayer.getGeoName());
                mapped.setAreaTypeDesc(serviceLayer.getLayerDesc());
                mapped.setServiceUrl(serviceLayer.getServiceUrl());
                mapped.setStyle(serviceLayer.getStyleLabelGeom());
            }
            AreaLocationTypesEntity areaLocationTypes = serviceLayer.getAreaType();
            if (areaLocationTypes != null) {
                mapped.setIsLocation(areaLocationTypes.getIsLocation());
                mapped.setServiceType(areaLocationTypes.getTypeName());
            }
            returnList.add(mapped);
        }
        return returnList;
    }


    public List<AreaLayerDto> findSystemAreaAndLocationLayerMapping() {
        List<AreaLayerDto> returnList = new ArrayList<>();
        Query qry = em.createNamedQuery(AreaLocationTypesEntity.FIND_SYSTEM_AREA_AND_LOCATION_LAYER);
        List<AreaLocationTypesEntity> rs = qry.getResultList();
        for (AreaLocationTypesEntity rec : rs) {
            AreaLayerDto mapped = new AreaLayerDto();

            mapped.setTypeName(rec.getTypeName());

            ServiceLayerEntity serviceLayer = rec.getServiceLayer();
            if (serviceLayer != null) {
                mapped.setIsInternal(serviceLayer.getIsInternal());
                mapped.setGeoName(serviceLayer.getGeoName());
                mapped.setAreaTypeDesc(serviceLayer.getLayerDesc());
                mapped.setServiceUrl(serviceLayer.getServiceUrl());
                mapped.setStyle(serviceLayer.getStyleLabelGeom());
            }
            AreaLocationTypesEntity areaLocationTypes = serviceLayer.getAreaType();
            if (areaLocationTypes != null) {
                mapped.setIsLocation(areaLocationTypes.getIsLocation());
                mapped.setServiceType(areaLocationTypes.getTypeName());
            }
            returnList.add(mapped);
        }
        return returnList;
    }

}