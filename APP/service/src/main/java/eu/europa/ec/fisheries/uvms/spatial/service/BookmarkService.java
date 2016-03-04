package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.bookmark.Bookmark;
import java.util.Set;

public interface BookmarkService {

    Set<Bookmark> listByUsername(String username, String scopeName, String roleName, String applicationName) throws ServiceException;

    Bookmark create(Bookmark bookmark, String username, String scopeName, String roleName, String applicationName) throws ServiceException;

    void delete(Long id, String username, String scopeName, String roleName, String applicationName) throws ServiceException;

    void update(Bookmark bookmark, String username, String scopeName, String roleName, String applicationName) throws ServiceException;
}
