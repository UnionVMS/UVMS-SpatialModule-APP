package eu.europa.ec.fisheries.uvms.spatial.mapper;

import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.spatial.entity.BookmarkEntity;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class BookmarkMapperTest extends BaseUnitilsTest {

    @Test
    public void bookmarkToBookmarkEntityWithNull(){
        assertNull(BookmarkMapper.INSTANCE.bookmarkToBookmarkEntity(null));
    }
}
