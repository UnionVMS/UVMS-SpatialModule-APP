package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
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
    private CrudServiceBean crudService;

    @InjectMocks
    private AreaTypeNamesService areaTypeNamesService = new AreaTypeNamesServiceBean();

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnAreaTypes() throws Exception {
        // given
        when(crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS)).thenReturn(createAreaLocationTypesList(AREA_TYPES));

        // when
        List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();

        //then
        assertNotNull(areaTypeNames);
        List<String> areaTypes = areaTypeNames;
        assertThat(areaTypes).hasSize(AREA_TYPES.size());
        assertThat(areaTypes).containsOnly(AREA_TYPES.toArray());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotThrowNullPointerException() throws Exception {
        // given
        when(crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS)).thenReturn(Collections.emptyList());

        // when
        List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();

        // then
        assertNotNull(areaTypeNames);
        assertThat(areaTypeNames).isEmpty();
    }

    private List<AreaLocationTypesEntity> createAreaLocationTypesList(List<String> areaTypes) {
        return Lists.transform(areaTypes, new Function<String, AreaLocationTypesEntity>() {
            @Override
            public AreaLocationTypesEntity apply(String areaType) {
                AreaLocationTypesEntity entity = new AreaLocationTypesEntity();
                entity.setTypeName(areaType);
                return entity;
            }
        });
    }

}
