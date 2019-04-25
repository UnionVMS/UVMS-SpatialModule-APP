/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Stateless
public class ServiceLayerDao extends AbstractDAO<ServiceLayerEntity> {

    private static final String SUB_TYPE = "subTypes";

    @PersistenceContext
    private EntityManager em;


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @SuppressWarnings("unchecked")
    public ServiceLayerEntity getBy(AreaType areaType) throws ServiceException {

        ServiceLayerEntity serviceLayerEntity = null;
        List<ServiceLayerEntity> layers = findEntityByNamedQuery(ServiceLayerEntity.class, ServiceLayerEntity.BY_SYSTEM_AREA_TYPE,
                QueryParameter.with("systemAreaType", areaType.value()).parameters(), 1);

        if (layers != null && layers.size() == 1) {
            serviceLayerEntity = layers.get(0);
        }

        return serviceLayerEntity;

    }

    public List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing) {
        if (isWithBing) {
            return createNamedQueryWithParameterList(ServiceLayerEntity.FIND_SERVICE_LAYER_BY_SUBTYPE, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        } else {
            return createNamedQueryWithParameterList(ServiceLayerEntity.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        }
    }

    private <T> Query createNamedQueryWithParameterList(String nativeQuery, String parameterName, List<?> parameters, Class<T> dtoClass) {
        Query query = em.unwrap(Session.class).getNamedQuery(nativeQuery);
        query.setParameterList(parameterName, parameters);
        query.setResultTransformer(Transformers.aliasToBean(dtoClass));
        return query;
    }


    public List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids) {
        Map<String, List<Long>> parameters = ImmutableMap.<String, List<Long>>builder().put("ids", ids).build();

        Query query = em.unwrap(Session.class).getNamedQuery(ServiceLayerEntity.FIND_SERVICE_LAYERS_BY_ID);
        for (Map.Entry<String, List<Long>> entry : parameters.entrySet()) {
            query.setParameterList(entry.getKey(), entry.getValue());
        }
        return query.list();
    }
}