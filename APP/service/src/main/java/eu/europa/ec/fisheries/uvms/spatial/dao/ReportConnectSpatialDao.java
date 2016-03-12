package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
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
                ReportConnectSpatialEntity.class, QueryNameConstants.FIND_BY_REPORT_ID,
                with("reportId", reportId).parameters(), 1
        );
    }

    public List<ReportConnectSpatialEntity> findReportConnectSpatialById(Long reportId, Long id) throws ServiceException {
        return findEntityByNamedQuery(
                ReportConnectSpatialEntity.class, QueryNameConstants.FIND_BY_ID,
                with("reportId", reportId).and("id", id).parameters(), 1
        );
    }

    public void deleteById(List<Long> reportIds) throws ServiceException {

        deleteEntityByNamedQuery(ReportConnectSpatialEntity.class, ReportConnectSpatialEntity.DELETE_BY_ID_LIST,
                with("idList", reportIds).parameters()
        );
    }
}
