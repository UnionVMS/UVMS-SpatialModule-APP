package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.model.bookmark.Bookmark;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
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
