/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.impl;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.BookmarkEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.BookmarkService;
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
        return repository.listBookmarksBy(userName);
    }

    @Override
    @Transactional
    public Bookmark create(Bookmark bookmark, String userName) throws ServiceException {

        log.info("{} is creating bookmark(...), with a scopeName={}", userName);
        List<ProjectionEntity> projection = repository.findProjection(bookmark.getSrs());
        if (CollectionUtils.isEmpty(projection)){
            throw new ServiceException("PROJECTION NOT FOUND");
        }
        bookmark.setCreatedBy(userName);
        BookmarkEntity entity = repository.create(BookmarkMapper.INSTANCE.bookmarkToBookmarkEntity(bookmark));
        return BookmarkMapper.INSTANCE.bookmarkEntityToBookmark(entity);
    }

    @Override
    @Transactional
    public void delete(Long id, String userName) throws ServiceException {

        log.info("{} is deleting bookmark(...), with a scopeName={}", userName);
        repository.deleteBookmark(id);
    }

    @Override
    @Transactional
    public void update(Bookmark bookmark, String userName) throws ServiceException {

        log.info("{} is updating bookmark(...), with a scopeName={}", userName);
        BookmarkEntity entity = repository.getBookmarkBy(bookmark.getId());
        BookmarkMapper.INSTANCE.merge(bookmark, entity);
    }
}