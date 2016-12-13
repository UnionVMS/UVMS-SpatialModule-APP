/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import javax.persistence.EntityManager;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.*;
import static eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity.*;

public class AreaLocationTypesDao extends AbstractDAO<AreaLocationTypesEntity> {

    private EntityManager em;

    public AreaLocationTypesDao(EntityManager em) {
        this.em = em;
    }

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

    public List findUserAreaLayerMapping() {
        Query query = em.unwrap(Session.class).getNamedQuery(AreaLocationTypesEntity.FIND_USER_AREA_LAYER);
        query.setResultTransformer(Transformers.aliasToBean(UserAreaLayerDto.class));
        return query.list();
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