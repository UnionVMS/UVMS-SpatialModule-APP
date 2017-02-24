package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class BookmarkMapperTest extends BaseUnitilsTest {

    @Test
    public void bookmarkToBookmarkEntityWithNull(){
        assertNull(BookmarkMapper.INSTANCE.bookmarkToBookmarkEntity(null));
    }
}
