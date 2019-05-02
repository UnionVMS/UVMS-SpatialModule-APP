/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.service.dto.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BookmarkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface BookmarkMapper {

    BookmarkMapper INSTANCE = Mappers.getMapper(BookmarkMapper.class);

    BookmarkEntity bookmarkToBookmarkEntity(Bookmark bookmark);

    Bookmark bookmarkEntityToBookmark(BookmarkEntity bookmark);

    Set<Bookmark> bookmarkEntityListToBookmarkSet(List<BookmarkEntity> bookmarkEntitySet);

    @Mappings({
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "srs", expression = "java(bookmark.getSrs() != null ? bookmark.getSrs() : entity.getSrs())"),
            @Mapping(target = "name", expression = "java(bookmark.getName() != null ? bookmark.getName() : entity.getName())"),
            @Mapping(target = "extent", expression = "java(bookmark.getExtent() != null ? bookmark.getExtent() : entity.getExtent())")
    })
    void merge(Bookmark bookmark, @MappingTarget BookmarkEntity entity);
}