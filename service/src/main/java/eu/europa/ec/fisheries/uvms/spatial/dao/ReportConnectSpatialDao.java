package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;

import javax.persistence.EntityManager;

import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.*;

public class ReportConnectSpatialDao extends AbstractDAO<ReportConnectSpatialEntity> {

    private EntityManager em;

    public ReportConnectSpatialDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public List<ReportConnectSpatialEntity> findReportConnectSpatialBy(Long reportId) throws ServiceException {
        return findEntityByNamedQuery(
                ReportConnectSpatialEntity.class, ReportConnectSpatialEntity.FIND_BY_REPORT_ID,
                with("reportId", reportId).parameters(), 1
        );
    }
}
