package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class FilterAreasServiceIT extends AbstractArquillianIT {

    private static final String EEZ = "eez";
    private static final String RFMO = "rfmo";

    @EJB
    private FilterAreasService filterAreasService;

    @Test
    public void shouldReturnArea() throws Exception {
        // given
        FilterAreasSpatialRQ request = createRequest();

        // when
        FilterAreasSpatialRS filterAreasSpatialRS = filterAreasService.filterAreas(request);

        // then
        assertNotNull(filterAreasSpatialRS);
        assertNotNull(filterAreasSpatialRS.getGeometry());
    }

    private FilterAreasSpatialRQ createRequest() {
        ArrayList<AreaIdentifierType> userAreaIdentifiers = Lists.newArrayList();
        userAreaIdentifiers.add(new AreaIdentifierType("1", EEZ));
        userAreaIdentifiers.add(new AreaIdentifierType("12", EEZ));
        userAreaIdentifiers.add(new AreaIdentifierType("10", RFMO));
        userAreaIdentifiers.add(new AreaIdentifierType("18", RFMO));
        userAreaIdentifiers.add(new AreaIdentifierType("20", RFMO));
        UserAreasType userAreasType = new UserAreasType(userAreaIdentifiers);

        return new FilterAreasSpatialRQ(SpatialModuleMethod.GET_FILTER_AREA, userAreasType, new ScopeAreasType());
    }
}
