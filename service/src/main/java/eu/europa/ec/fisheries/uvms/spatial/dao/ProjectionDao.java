package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;

import javax.persistence.EntityManager;

/**
 * //TODO create test
 */
public class ProjectionDao extends AbstractDAO<ProjectionEntity> {

    private EntityManager em;

    public ProjectionDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
