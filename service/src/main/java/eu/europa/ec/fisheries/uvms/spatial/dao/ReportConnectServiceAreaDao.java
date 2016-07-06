/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.hibernate.Query;
import org.hibernate.Session;

public class ReportConnectServiceAreaDao extends AbstractDAO<ReportConnectServiceAreasEntity> {

    private EntityManager em;

    public ReportConnectServiceAreaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void delete(ReportConnectServiceAreasEntity entity) {
        deleteEntity(ReportConnectServiceAreasEntity.class, entity.getId());
    }

    public List<ReportConnectServiceAreasEntity> findReportConnectServiceAreas(long reportId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put("reportId", reportId).build();

        Query query = em.unwrap(Session.class).getNamedQuery(ReportConnectServiceAreasEntity.FIND_REPORT_SERVICE_AREAS);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.list();
    }

    public void deleteReportConnectServiceAreas(Long id) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put("id", id).build();
        Query query = em.unwrap(Session.class).getNamedQuery(ReportConnectServiceAreasEntity.DELETE_BY_REPORT_CONNECT_SPATIAL_ID);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.executeUpdate();
    }

}