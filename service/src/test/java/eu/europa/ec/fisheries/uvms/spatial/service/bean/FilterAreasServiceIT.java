package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.ArrayList;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class FilterAreasServiceIT extends AbstractArquillianIT {

    private static final String EEZ = "eez";
    private static final String RFMO = "rfmo";
    private static final String NOT_EXISTING_TYPE = "NOT_EXISTING_TYPE";

    @EJB
    private FilterAreasService filterAreasService;

    @Test
    public void shouldReturnMergedAreaAndSkipNotExisting() throws Exception {
        // given
        FilterAreasSpatialRQ request = createRequest(RFMO);

        // when
        FilterAreasSpatialRS filterAreasSpatialRS = filterAreasService.filterAreas(request);

        // then
        assertNotNull(filterAreasSpatialRS);
        assertNotNull(filterAreasSpatialRS.getGeometry());
    }

    @Test
    public void shouldThrowExceptionWhenWrongAreaType() throws Exception {
        // given
        FilterAreasSpatialRQ request = createRequest(NOT_EXISTING_TYPE);

        // when
        try {
            filterAreasService.filterAreas(request);
            fail("Should throw an excpetion when one or more of given area types are invalid.");
        } catch (Exception e) {
            SpatialServiceException ex = new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, NOT_EXISTING_TYPE);
            assertThat(e.getCause()).isInstanceOf(SpatialServiceException.class).hasMessage(ex.getMessage());
        }

    }

    private FilterAreasSpatialRQ createRequest(String areaType) {
        ArrayList<AreaIdentifierType> userAreaIdentifiers = Lists.newArrayList();
        userAreaIdentifiers.add(new AreaIdentifierType("1", EEZ));
        userAreaIdentifiers.add(new AreaIdentifierType("12", EEZ));
        userAreaIdentifiers.add(new AreaIdentifierType("10", RFMO));
        userAreaIdentifiers.add(new AreaIdentifierType("18", RFMO));
        userAreaIdentifiers.add(new AreaIdentifierType("20", areaType));

        ArrayList<AreaIdentifierType> scopeAreaIdentifiers = Lists.newArrayList();
        scopeAreaIdentifiers.add(new AreaIdentifierType("1", EEZ));
        scopeAreaIdentifiers.add(new AreaIdentifierType("44", EEZ));

        UserAreasType userAreasType = new UserAreasType(userAreaIdentifiers);
        ScopeAreasType scopeAreasType = new ScopeAreasType(scopeAreaIdentifiers);

        return new FilterAreasSpatialRQ(SpatialModuleMethod.GET_FILTER_AREA, userAreasType, scopeAreasType);
    }

}
