package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import javax.persistence.EntityManager;
import java.util.List;

public class BookmarkDao extends AbstractDAO<BookmarkEntity> {

    private EntityManager em;

    public BookmarkDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<BookmarkEntity> findAllByUser(String userName) throws ServiceException {
        return findEntityByNamedQuery(BookmarkEntity.class, BookmarkEntity.LIST_BY_USERNAME, QueryParameter.with("createdBy", userName).parameters());
    }
}
