package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableList;
import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAOBean;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreaTypesSpatialRS;
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
public class AreaServiceBeanTest {

    private final static List<String> AREA_TYPES = ImmutableList.of("Portugal", "Belgium", "Poland", "Bulgaria", "India");

    @Mock
    private CommonGenericDAOBean commonGenericDAO;

    @InjectMocks
    private AreaService areaService = new AreaServiceBean();

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnAreaTypes() throws Exception {
        // given
        when(commonGenericDAO.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL)).thenReturn(AREA_TYPES);

        // when
        GetAreaTypesSpatialRS areaTypeRS = areaService.getAreaTypes();

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
        when(commonGenericDAO.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL)).thenReturn(null);

        // when
        GetAreaTypesSpatialRS areaTypeRS = areaService.getAreaTypes();

        // then
        assertNotNull(areaTypeRS);
        assertThat(areaTypeRS.getAreaTypes().getAreaType()).isEmpty();
    }

}
