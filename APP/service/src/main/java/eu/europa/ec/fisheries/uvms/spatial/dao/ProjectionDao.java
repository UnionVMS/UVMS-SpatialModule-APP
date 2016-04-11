package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;

import javax.persistence.EntityManager;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.service.QueryParameter.*;
import static eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity.*;

public class ProjectionDao extends AbstractDAO<ProjectionEntity> {

    private EntityManager em;

    public ProjectionDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ProjectionEntity> findBySrsCode(Integer srs) throws ServiceException {
        return findEntityByNamedQuery(ProjectionEntity.class, FIND_BY_SRS_CODE, with("srsCode", srs).parameters(), 1);
    }
}
