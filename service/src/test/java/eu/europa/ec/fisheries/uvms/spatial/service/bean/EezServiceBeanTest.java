package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAOBean;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by kopyczmi on 20-Aug-15.
 */
@RunWith(MockitoJUnitRunner.class)
public class EezServiceBeanTest {

    private static final String EEZ_ID = "123";
    @Mock
    EezMapper eezMapper;
    @Mock
    private CommonGenericDAOBean commonGenericDAO;
    @InjectMocks
    private EezService eezService = new EezServiceBean();

    @Test
    public void shouldReturnEmptyResponse() {
        // given
        EezSpatialRQ getEezSpatialRQ = new EezSpatialRQ(EEZ_ID);

        // when
        EezSpatialRS eezSpatialRS = eezService.getEezById(getEezSpatialRQ);

        // then
        assertNotNull(eezSpatialRS);
        ResponseMessageType responseMessage = eezSpatialRS.getResponseMessage();
        assertNotNull(responseMessage);
        assertNotNull(responseMessage.getSuccess());
        assertNull(eezSpatialRS.getEez());
    }
}