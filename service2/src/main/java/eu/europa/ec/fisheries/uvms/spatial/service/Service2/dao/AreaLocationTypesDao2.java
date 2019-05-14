/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.AreaLayerDto2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.AreaLocationTypesEntity2;
import org.hibernate.Session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AreaLocationTypesDao2 {

    @PersistenceContext
    private EntityManager em;


    public AreaLocationTypesEntity2 findOneByTypeName(final String typeName) {

        TypedQuery<AreaLocationTypesEntity2> query = em.createNamedQuery(AreaLocationTypesEntity2.FIND_TYPE_BY_NAME, AreaLocationTypesEntity2.class);
        query.setParameter("typeName",typeName);
        List<AreaLocationTypesEntity2> resultList = query.getResultList();
        if(!resultList.isEmpty()){
            return resultList.get(0);
        }
        return null;
    }

    public List<AreaLocationTypesEntity2> findByIsLocationAndIsSystemWide(Boolean isLocation, Boolean isSystemWide ) {

        TypedQuery<AreaLocationTypesEntity2> query = em.createNamedQuery(AreaLocationTypesEntity2.FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE, AreaLocationTypesEntity2.class);
        query.setParameter("isLocation",isLocation);
        query.setParameter("isSystemWide",isSystemWide);

        return  query.getResultList();
    }

    public List<AreaLocationTypesEntity2> findByIsLocation(Boolean isLocation) {

        TypedQuery<AreaLocationTypesEntity2> query = em.createNamedQuery(AreaLocationTypesEntity2.FIND_ALL_IS_LOCATION, AreaLocationTypesEntity2.class);
        query.setParameter("isLocation",isLocation);
       return   query.getResultList();
    }

    public AreaLayerDto2 findUserAreaLayerMapping() {
        TypedQuery<AreaLayerDto2> query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity2.FIND_USER_AREA_LAYER);
        return query.getSingleResult();
    }

    public List<AreaLayerDto2> findSystemAreaLayerMapping() {
        TypedQuery<AreaLayerDto2> query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity2.FIND_SYSTEM_AREA_LAYER);
        return query.getResultList();
    }

    public List<AreaLocationTypesEntity2> findSystemAreaAndLocationLayerMapping() {
        TypedQuery<AreaLocationTypesEntity2> query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity2.FIND_SYSTEM_AREA_AND_LOCATION_LAYER);
        return query.getResultList();
    }


}