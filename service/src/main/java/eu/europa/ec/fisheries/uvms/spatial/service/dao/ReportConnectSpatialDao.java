/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import static eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter.with;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectSpatialEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

@Stateless
public class ReportConnectSpatialDao extends AbstractDAO<ReportConnectSpatialEntity> {

    private static final String REPORT_ID = "reportId";

    @PersistenceContext
    private EntityManager em;

    public ReportConnectSpatialDao() {

    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public ReportConnectSpatialEntity findByReportId(Long reportId) throws ServiceException {

        ReportConnectSpatialEntity entity = null;
        List<ReportConnectSpatialEntity> list =
                findEntityByNamedQuery(ReportConnectSpatialEntity.class,
                        ReportConnectSpatialEntity.FIND_BY_REPORT_ID, with(REPORT_ID, reportId).parameters(), 1);
        if (isNotEmpty(list)) {
            entity = list.get(0);
        }
        return entity;
    }

    public List<ReportConnectSpatialEntity> findByConnectId(Long id) throws ServiceException {
        return findEntityByNamedQuery(ReportConnectSpatialEntity.class, ReportConnectSpatialEntity.FIND_BY_REPORT_CONNECT_ID, with("id", id).parameters(), 1);
    }

    public List<ReportConnectSpatialEntity> findByReportIdAndConnectId(Long reportId, Long id) throws ServiceException {
        return findEntityByNamedQuery(
                ReportConnectSpatialEntity.class, ReportConnectSpatialEntity.FIND_BY_ID,
                with(REPORT_ID, reportId).and("id", id).parameters(), 1
        );
    }

    public void deleteById(List<Long> reportIds) throws ServiceException {

        if (isNotEmpty(reportIds)) {
            deleteEntityByNamedQuery(ReportConnectSpatialEntity.class, ReportConnectSpatialEntity.DELETE_BY_ID_LIST,
                    with("idList", reportIds).parameters()
            );
        }
    }

    public List<ProjectionDto> findProjectionByMap(long reportId) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put(REPORT_ID, reportId).build();
        Query query = em.unwrap(Session.class).getNamedQuery(ReportConnectSpatialEntity.FIND_MAP_PROJ_BY_ID);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setResultTransformer(Transformers.aliasToBean(ProjectionDto.class));
        return query.list();
    }
}