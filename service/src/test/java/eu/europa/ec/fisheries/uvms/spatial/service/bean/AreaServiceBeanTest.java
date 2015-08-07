package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.schema.spatial.types.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
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
public class AreaServiceBeanTest {

    private final static List<String> AREA_TYPES = ImmutableList.of("Portugal", "Belgium", "Poland", "Bulgaria");

    @Mock
    private CrudDao crudDao;

    @InjectMocks
    private AreaService areaService = new AreaServiceBean();

    @Test
    public void shouldReturnAreaTypes() throws Exception {
        // given
        when(crudDao.findByHQLQuery("SELECT a.typeName FROM AreaTypeEntity a", String.class)).thenReturn(AREA_TYPES);

        // when
        GetAreaTypesSpatialRS areaTypeRS = areaService.getAreaTypes();

        //then
        assertNotNull(areaTypeRS);
        List<AreaType> areaTypes = areaTypeRS.getAreaTypes();
        assertThat(areaTypes).hasSize(AREA_TYPES.size());
        assertThat(retrieveAreaNames(areaTypes)).containsOnly(AREA_TYPES.toArray());
    }

    @Test
    // TODO Great, thanks!
    public void shouldNotThrowNullPointerException() throws Exception {
        // TODO That comments make the test more readable. You see, and you immediately know what class are you testing and which you are mocking. It is even more noticeable with larger more complicated tests.
        // TODO see: http://stackoverflow.com/questions/7665412/writing-first-junit-test
        // TODO see: http://martinfowler.com/bliki/GivenWhenThen.html
        // given
        when(crudDao.findByHQLQuery("SELECT a.typeName FROM AreaTypeEntity a", String.class)).thenReturn(null);

        // when
        GetAreaTypesSpatialRS areaTypeRS = areaService.getAreaTypes();

        // then
        assertNotNull(areaTypeRS);
        assertThat(areaTypeRS.getAreaTypes()).isEmpty();
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
