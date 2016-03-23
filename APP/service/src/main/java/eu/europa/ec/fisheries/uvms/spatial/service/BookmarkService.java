package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import java.util.Set;

public interface BookmarkService {

    Set<Bookmark> listByUsername(String username) throws ServiceException;

    Bookmark create(Bookmark bookmark, String username) throws ServiceException;

    void delete(Long id, String username) throws ServiceException;

    void update(Bookmark bookmark, String username) throws ServiceException;
}
