package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableList;
import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialDao;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@RunWith(MockitoJUnitRunner.class)
public class AreaServiceBeanITest {

    private final static List<String> AREA_TYPES = ImmutableList.of("Portugal", "Belgium", "Poland", "Bulgaria");

    @Mock
    private SpatialDao spatialDao;

    @InjectMocks
    private AreaService areaService = new AreaServiceBean();

    @Test
    public void shouldReturnAreaTypes() throws Exception {
        // given
        when(spatialDao.getAreaTypes()).thenReturn(AREA_TYPES);

        // when
        GetAreaTypesSpatialRS areaTypes = areaService.getAreaTypes();

        //then
        assertNotNull(areaTypes);
        List<String> areaTypesList = areaTypes.getAreaTypes();
        assertThat(areaTypesList).hasSize(AREA_TYPES.size());
        assertThat(areaTypes.getAreaTypes()).containsOnly(AREA_TYPES.toArray());
    }

}
