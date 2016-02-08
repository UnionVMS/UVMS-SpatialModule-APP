package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;

import javax.persistence.EntityManager;
import java.util.List;

public class ProjectionDao extends AbstractDAO<ProjectionEntity> {

    private EntityManager em;

    public ProjectionDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ProjectionEntity findBySrsCode(Integer srs) throws ServiceException {

        List<ProjectionEntity> srsCode = findEntityByNamedQuery(ProjectionEntity.class, ProjectionEntity.FIND_BY_SRS_CODE, QueryParameter.with("srsCode", srs).parameters(), 1);
        if (!srsCode.isEmpty()){
            return srsCode.remove(0);
        }
        throw new ServiceException("PROJECTION NOT FOUND");

    }
}
