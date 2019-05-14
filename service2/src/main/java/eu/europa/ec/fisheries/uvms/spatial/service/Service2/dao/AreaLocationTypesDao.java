/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.AreaLocationTypesEntity;
import org.hibernate.Session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AreaLocationTypesDao {

    @PersistenceContext
    private EntityManager em;


    public AreaLocationTypesEntity findOneByTypeName(final String typeName) {

        TypedQuery<AreaLocationTypesEntity> query = em.createNamedQuery(AreaLocationTypesEntity.FIND_TYPE_BY_NAME, AreaLocationTypesEntity.class);
        query.setParameter("typeName",typeName);
        List<AreaLocationTypesEntity> resultList = query.getResultList();
        if(!resultList.isEmpty()){
            return resultList.get(0);
        }
        return null;
    }

    public List<AreaLocationTypesEntity> findByIsLocationAndIsSystemWide(Boolean isLocation, Boolean isSystemWide ) {

        TypedQuery<AreaLocationTypesEntity> query = em.createNamedQuery(AreaLocationTypesEntity.FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE, AreaLocationTypesEntity.class);
        query.setParameter("isLocation",isLocation);
        query.setParameter("isSystemWide",isSystemWide);

        return  query.getResultList();
    }

    public List<AreaLocationTypesEntity> findByIsLocation(Boolean isLocation) {

        TypedQuery<AreaLocationTypesEntity> query = em.createNamedQuery(AreaLocationTypesEntity.FIND_ALL_IS_LOCATION, AreaLocationTypesEntity.class);
        query.setParameter("isLocation",isLocation);
       return   query.getResultList();
    }

    public AreaLayerDto findUserAreaLayerMapping() {
        TypedQuery<AreaLayerDto> query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity.FIND_USER_AREA_LAYER);
        return query.getSingleResult();
    }

    public List<AreaLayerDto> findSystemAreaLayerMapping() {
        TypedQuery<AreaLayerDto> query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity.FIND_SYSTEM_AREA_LAYER);
        return query.getResultList();
    }

    public List<AreaLocationTypesEntity> findSystemAreaAndLocationLayerMapping() {
        TypedQuery<AreaLocationTypesEntity> query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity.FIND_SYSTEM_AREA_AND_LOCATION_LAYER);
        return query.getResultList();
    }


}