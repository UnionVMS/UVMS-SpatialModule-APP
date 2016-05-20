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
