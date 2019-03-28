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
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProjectionEntity;

import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ProjectionDto;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import static eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter.*;
import static eu.europa.ec.fisheries.uvms.spatial.service.entity.ProjectionEntity.*;

@Stateless
public class ProjectionDao extends AbstractDAO<ProjectionEntity> {

    @PersistenceContext
    private EntityManager em;

    public ProjectionDao() {

    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ProjectionEntity> findBySrsCode(Integer srs) throws ServiceException {
        return findEntityByNamedQuery(ProjectionEntity.class, FIND_BY_SRS_CODE, with("srsCode", srs).parameters(), 1);
    }

    public List<ProjectionDto> findProjectionById(Long id) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put("id", id).build();
        Query query = em.unwrap(Session.class).getNamedQuery(ProjectionEntity.FIND_PROJECTION_BY_ID);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setResultTransformer(Transformers.aliasToBean(ProjectionDto.class));
        return query.list();
    }
}