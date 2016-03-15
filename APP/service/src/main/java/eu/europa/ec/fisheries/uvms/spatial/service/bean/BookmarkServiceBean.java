package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.security.bean.USMService;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.BookmarkService;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.BookmarkMapper;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Set;

@Stateless
@Local(BookmarkService.class)
@Slf4j
public class BookmarkServiceBean implements BookmarkService {

    private @EJB SpatialRepository repository;
    private @EJB USMService usmService;

    @Override
    public Set<Bookmark> listByUsername(String userName, String scopeName, String roleName, String applicationName) throws ServiceException {

        log.info("{} is requesting bookmarks(...), with a scopeName={}", userName, scopeName);

        Set<String> features = usmService.getUserFeatures(userName, applicationName, roleName, scopeName);

        if (features.contains("MANAGE_BOOKMARKS")){
            return repository.listBookmarksBy(userName);
        }
        else {
            throw new ServiceException("User doesn't have the right to list bookmarks");
        }
    }

    @Override
    @Transactional
    public Bookmark create(Bookmark bookmark, String userName, String scopeName, String roleName, String applicationName) throws ServiceException {

        log.info("{} is creating bookmark(...), with a scopeName={}", userName, scopeName);

        try {

            Set<String> features  = usmService.getUserFeatures(userName, applicationName, roleName, scopeName);

            if (features.contains("MANAGE_BOOKMARKS")){

                repository.findProjection(bookmark.getSrs());
                bookmark.setCreatedBy(userName);
                BookmarkEntity entity = repository.create(BookmarkMapper.INSTANCE.bookmarkToBookmarkEntity(bookmark));
                return BookmarkMapper.INSTANCE.bookmarkEntityToBookmark(entity);
            }
            else {
                throw new ServiceException("ERROR WHILE CREATING BOOKMARK");
            }

        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long id, String userName, String scopeName, String roleName, String applicationName) throws ServiceException {

        log.info("{} is deleting bookmark(...), with a scopeName={}", userName, scopeName);

        try {

            Set<String> features = usmService.getUserFeatures(userName, applicationName, roleName, scopeName);

            if (features.contains("MANAGE_BOOKMARKS")){
                repository.delete(id);
            }

        } catch (ServiceException e) {
            throw new ServiceException("User doesn't have the right to delete bookmarks");
        }

    }

    @Override
    @Transactional
    public void update(Bookmark bookmark, String userName, String scopeName, String roleName, String applicationName) throws ServiceException {

        log.info("{} is updating bookmark(...), with a scopeName={}", userName, scopeName);

        try {

            Set<String> features = usmService.getUserFeatures(userName, applicationName, roleName, scopeName);

            if (features.contains("MANAGE_BOOKMARKS")){
                repository.update(bookmark);
            }

        } catch (ServiceException e) {
            throw new ServiceException("User doesn't have the right to update bookmarks");
        }

    }
}
