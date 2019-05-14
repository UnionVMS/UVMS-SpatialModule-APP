/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.ReportConnectSpatialEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ReportConnectSpatialDao {

    private static final String REPORT_ID = "reportId";

    @PersistenceContext
    private EntityManager em;


    public ReportConnectSpatialEntity find(Long reportId)  {

        return em.find(ReportConnectSpatialEntity.class,reportId);
    }

    public List<ReportConnectSpatialEntity> findByConnectId(Long id)  {

            TypedQuery<ReportConnectSpatialEntity> query = em.createNamedQuery(ReportConnectSpatialEntity.FIND_BY_REPORT_CONNECT_ID, ReportConnectSpatialEntity.class);
            query.setParameter("id", id);
            return query.getResultList();
    }

    public List<ReportConnectSpatialEntity> findByReportIdAndConnectId(Long reportId, Long id)  {

        TypedQuery<ReportConnectSpatialEntity> query = em.createNamedQuery(ReportConnectSpatialEntity.FIND_BY_ID, ReportConnectSpatialEntity.class);
        query.setParameter("reportId", reportId);
        query.setParameter("id", id);
        return query.getResultList();
    }

    public void deleteById(List<Long> reportIds) throws ServiceException {

        if(reportIds == null || reportIds.size() < 1) return;

        TypedQuery<ReportConnectSpatialEntity> query = em.createNamedQuery(ReportConnectSpatialEntity.DELETE_BY_ID_LIST, ReportConnectSpatialEntity.class);
        query.setParameter("idList",reportIds);
        query.executeUpdate();
    }

    public List<ReportConnectSpatialEntity> findProjectionByMap(long reportId) {

        TypedQuery<ReportConnectSpatialEntity> query = em.createNamedQuery(ReportConnectSpatialEntity.FIND_MAP_PROJ_BY_ID, ReportConnectSpatialEntity.class);
        query.setParameter("reportId",reportId);
        return query.getResultList();
    }
}