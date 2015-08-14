package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.schemas.AreaType;
import eu.schemas.GetAreaTypesSpatialRS;
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
    private CommonGenericDAO commonGenericDAO;

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
        List<AreaType> areaTypes = areaTypeRS.getAreaType();
        assertThat(areaTypes).hasSize(AREA_TYPES.size());
        assertThat(retrieveAreaNames(areaTypes)).containsOnly(AREA_TYPES.toArray());
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
        assertThat(areaTypeRS.getAreaType()).isEmpty();
    }

    private List<String> retrieveAreaNames(List<AreaType> areaTypes) {
        return Lists.transform(areaTypes, new Function<AreaType, String>() {
            @Override
            public String apply(AreaType areaType) {
                return areaType.getTypeName();
            }
        });
    }

}
