package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import java.util.Map;
import javax.persistence.EntityManager;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

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

    public List<ProjectionDto> findProjectionById(Long id) {
        Map<String, Object> parameters = ImmutableMap.<String, Object>builder().put("id", id).build();
        Query query = em.unwrap(Session.class).getNamedQuery(ProjectionEntity.FIND_PROJECTION_BY_ID);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setResultTransformer(Transformers.aliasToBean(ProjectionDto.class));
        return query.list();
    }
}
