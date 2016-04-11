package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.BookmarkService;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.BookmarkMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Stateless
@Local(BookmarkService.class)
@Slf4j
public class BookmarkServiceBean implements BookmarkService {

    private @EJB SpatialRepository repository;

    @Override
    public Set<Bookmark> listByUsername(String userName) throws ServiceException {

        log.info("{} is requesting bookmarks(...), with a scopeName={}", userName);

        try {
            return repository.listBookmarksBy(userName);

        } catch (ServiceException e) {
            throw new ServiceException("User doesn't have the right to list bookmarks");
        }
    }

    @Override
    @Transactional
    public Bookmark create(Bookmark bookmark, String userName) throws ServiceException {

        log.info("{} is creating bookmark(...), with a scopeName={}", userName);

        try {
            List<ProjectionEntity> projection = repository.findProjection(bookmark.getSrs());

            if (CollectionUtils.isEmpty(projection)){
                throw new ServiceException("PROJECTION NOT FOUND");
            }

            bookmark.setCreatedBy(userName);
            BookmarkEntity entity = repository.create(BookmarkMapper.INSTANCE.bookmarkToBookmarkEntity(bookmark));
            return BookmarkMapper.INSTANCE.bookmarkEntityToBookmark(entity);


        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long id, String userName) throws ServiceException {

        log.info("{} is deleting bookmark(...), with a scopeName={}", userName);

        try {
            repository.deleteBookmark(id);

        } catch (ServiceException e) {
            throw new ServiceException("User doesn't have the right to delete bookmarks");
        }
    }

    @Override
    @Transactional
    public void update(Bookmark bookmark, String userName) throws ServiceException {

        log.info("{} is updating bookmark(...), with a scopeName={}", userName);

        try {
            BookmarkEntity entity = repository.getBookmarkBy(bookmark.getId());
            BookmarkMapper.INSTANCE.merge(bookmark, entity);
        } catch (ServiceException e) {
            throw new ServiceException("User doesn't have the right to update bookmarks");
        }

    }
}
