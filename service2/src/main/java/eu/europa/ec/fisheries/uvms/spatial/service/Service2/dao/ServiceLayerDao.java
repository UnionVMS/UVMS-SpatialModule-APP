/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.ServiceLayerEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ServiceLayerDao {

    private static final String SUB_TYPE = "subTypes";


    @PersistenceContext
    private EntityManager em;


    @SuppressWarnings("unchecked")
    public ServiceLayerEntity getBy(AreaType areaType) {

        TypedQuery<ServiceLayerEntity> query = em.createNamedQuery(ServiceLayerEntity.BY_SYSTEM_AREA_TYPE, ServiceLayerEntity.class);
        query.setParameter("systemAreaType", areaType.value());
        List<ServiceLayerEntity> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    public List<ServiceLayerEntity> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing) {

        if (isWithBing) {
            TypedQuery<ServiceLayerEntity> query = em.createNamedQuery(ServiceLayerEntity.FIND_SERVICE_LAYER_BY_SUBTYPE, ServiceLayerEntity.class);
            query.setParameter("systemAreaType", subAreaTypes);
            return query.getResultList();
        } else {
            TypedQuery<ServiceLayerEntity> query = em.createNamedQuery(ServiceLayerEntity.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING, ServiceLayerEntity.class);
            query.setParameter("systemAreaType", subAreaTypes);
            return query.getResultList();
        }
    }


    public List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids) {

        TypedQuery<ServiceLayerEntity> query = em.createNamedQuery(ServiceLayerEntity.FIND_SERVICE_LAYERS_BY_ID, ServiceLayerEntity.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }
}