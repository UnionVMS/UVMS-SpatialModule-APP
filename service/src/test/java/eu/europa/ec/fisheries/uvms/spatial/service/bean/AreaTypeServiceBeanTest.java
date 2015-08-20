package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableList;
import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAOBean;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialConstants;
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
public class AreaTypeServiceBeanTest {

    private final static List<String> AREA_TYPES = ImmutableList.of("Portugal", "Belgium", "Poland", "Bulgaria", "India");

    @Mock
    private CommonGenericDAOBean commonGenericDAO;

    @InjectMocks
    private AreaTypeService areaTypeService = new AreaTypeServiceBean();

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnAreaTypes() throws Exception {
        // given
        when(commonGenericDAO.findEntityByNamedQuery(String.class, SpatialConstants.FIND_ALL)).thenReturn(AREA_TYPES);

        // when
        AreaTypeSpatialRS areaTypeRS = areaTypeService.getAreaTypes();

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
        when(commonGenericDAO.findEntityByNamedQuery(String.class, SpatialConstants.FIND_ALL)).thenReturn(null);

        // when
        AreaTypeSpatialRS areaTypeRS = areaTypeService.getAreaTypes();

        // then
        assertNotNull(areaTypeRS);
        assertThat(areaTypeRS.getAreaTypes().getAreaType()).isEmpty();
    }

}
