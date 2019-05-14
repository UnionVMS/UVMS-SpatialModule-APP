/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.ProjectionEntity;
import org.hibernate.Session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ProjectionDao {

   @PersistenceContext
    private EntityManager em;


    public List<ProjectionEntity> findBySrsCode(Integer srsCode)  {

            TypedQuery<ProjectionEntity> query = em.createNamedQuery(ProjectionEntity.FIND_BY_SRS_CODE, ProjectionEntity.class);
            query.setParameter("srsCode",srsCode);
            return   query.getResultList();
    }

    public List<ProjectionEntity> findProjectionById(Long id) {

        TypedQuery<ProjectionEntity> query = em.unwrap(Session.class).getNamedQuery(ProjectionEntity.FIND_PROJECTION_BY_ID);
        query.setParameter("id",id);
        return query.getResultList();

    }

    public List<ProjectionEntity> findAll(){
        TypedQuery<ProjectionEntity> query = em.createNamedQuery(ProjectionEntity.FIND_ALL, ProjectionEntity.class);
        return query.getResultList();
    }
}