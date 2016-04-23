package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.BaseArquillianTest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.ArrayList;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class FilterAreasServiceIT extends BaseArquillianTest {

    private static final String NOT_EXISTING_TYPE = "NOT_EXISTING_TYPE";

    @EJB
    private SpatialService filterAreasService;

    @Test
    public void shouldReturnMergedAreaAndSkipNotExisting() throws Exception {
        // given
        FilterAreasSpatialRQ request = createRequest(AreaType.RFMO);

        // when
        FilterAreasSpatialRS filterAreasSpatialRS = filterAreasService.computeAreaFilter(request);

        // then
        assertNotNull(filterAreasSpatialRS);
        assertNotNull(filterAreasSpatialRS.getGeometry());
        assertEquals(3, filterAreasSpatialRS.getCode());
    }

    @Test
    public void shouldThrowExceptionWhenWrongAreaType() throws Exception {
        // given
        FilterAreasSpatialRQ request = createRequest(null);

        // when
        try {
            filterAreasService.computeAreaFilter(request);
            fail("Should throw an excpetion when one or more of given area types are invalid.");
        } catch (Exception e) {
            SpatialServiceException ex = new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, NOT_EXISTING_TYPE);
            assertThat(e.getCause()).isInstanceOf(SpatialServiceException.class).hasMessage(ex.getMessage());
        }

    }

    private FilterAreasSpatialRQ createRequest(AreaType areaType) throws JAXBException {
        ArrayList<AreaIdentifierType> userAreaIdentifiers = Lists.newArrayList();
        userAreaIdentifiers.add(new AreaIdentifierType("1",  AreaType.EEZ));
        userAreaIdentifiers.add(new AreaIdentifierType("12",  AreaType.EEZ));
        userAreaIdentifiers.add(new AreaIdentifierType("10", AreaType.RFMO));
        userAreaIdentifiers.add(new AreaIdentifierType("18", AreaType.RFMO));
        userAreaIdentifiers.add(new AreaIdentifierType("20", areaType));

        ArrayList<AreaIdentifierType> scopeAreaIdentifiers = Lists.newArrayList();
        scopeAreaIdentifiers.add(new AreaIdentifierType("1", AreaType.EEZ));
        scopeAreaIdentifiers.add(new AreaIdentifierType("44",  AreaType.EEZ));

        UserAreasType userAreasType = new UserAreasType(userAreaIdentifiers);
        ScopeAreasType scopeAreasType = new ScopeAreasType(scopeAreaIdentifiers);


        FilterAreasSpatialRQ filterAreasSpatialRQ = new FilterAreasSpatialRQ(SpatialModuleMethod.GET_FILTER_AREA, userAreasType, scopeAreasType);

        JAXBContext jaxbContext = JAXBContext.newInstance(FilterAreasSpatialRQ.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(filterAreasSpatialRQ, System.out);

        return filterAreasSpatialRQ;
    }

}
