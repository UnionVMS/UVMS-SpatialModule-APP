package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableList;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AreaTypeNamesServiceTest {

    private final static List<String> AREA_TYPES = ImmutableList.of("Portugal", "Belgium", "Poland", "Bulgaria", "India");

    @Mock
    private SpatialRepository repository;

    @InjectMocks
    private AreaTypeNamesService areaTypeNamesService = new AreaTypeNamesServiceBean();

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnAreaTypes() throws Exception {
        // given
        when(repository.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREA_TYPE_NAMES)).thenReturn(AREA_TYPES);

        // when
        List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();

        //then
        assertNotNull(areaTypeNames);
        assertThat(areaTypeNames).hasSize(AREA_TYPES.size());
        assertThat(areaTypeNames).containsOnly(AREA_TYPES.toArray());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotThrowNullPointerException() throws Exception {
        // given
        when(repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS)).thenReturn(Collections.emptyList());

        // when
        List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();

        // then
        assertNotNull(areaTypeNames);
        assertThat(areaTypeNames).isEmpty();
    }
}
