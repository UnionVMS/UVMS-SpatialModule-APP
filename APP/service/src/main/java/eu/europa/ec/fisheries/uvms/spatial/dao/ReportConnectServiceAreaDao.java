package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import javax.persistence.EntityManager;

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
}
