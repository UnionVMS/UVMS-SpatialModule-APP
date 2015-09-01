package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableList;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeNamesSpatialRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@RunWith(MockitoJUnitRunner.class)
public class AreaTypeNamesServiceTest {

    private final static List<String> AREA_TYPES = ImmutableList.of("Portugal", "Belgium", "Poland", "Bulgaria", "India");

    @Mock
    private CrudServiceBean commonGenericDAO;

    @InjectMocks
    private AreaTypeNamesService areaTypeNamesService = new AreaTypeNamesServiceBean();

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnAreaTypes() throws Exception {
        // given
        when(commonGenericDAO.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS)).thenReturn(AREA_TYPES);

        // when
        AreaTypeNamesSpatialRS areaTypeRS = areaTypeNamesService.getAreaTypes();

        //then
        assertNotNull(areaTypeRS);
        List<String> areaTypes = areaTypeRS.getAreaTypes().getAreaType();
        assertThat(areaTypes).hasSize(AREA_TYPES.size());
        assertThat(areaTypes).containsOnly(AREA_TYPES.toArray());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotThrowNullPointerException() throws Exception {
        // given
        when(commonGenericDAO.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS)).thenReturn(null);

        // when
        AreaTypeNamesSpatialRS areaTypeRS = areaTypeNamesService.getAreaTypes();

        // then
        assertNotNull(areaTypeRS);
        assertThat(areaTypeRS.getAreaTypes().getAreaType()).isEmpty();
    }

}
