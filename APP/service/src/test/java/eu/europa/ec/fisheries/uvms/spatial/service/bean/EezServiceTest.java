package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNull;

/**
 * Created by kopyczmi on 20-Aug-15.
 */
@RunWith(MockitoJUnitRunner.class)
public class EezServiceTest {

    private static final String EEZ_ID = "123";
    @Mock
    EezMapper eezMapper;
    @Mock
    private SpatialRepository repository;
    @InjectMocks
    private EezService service = new EezServiceBean();

    @Test
    public void testGetEezByIdNull() {
        EezSpatialRQ eezSpatialRQ = new EezSpatialRQ();
        eezSpatialRQ.setEezId(EEZ_ID);
        EezType eezById = service.getEezById(eezSpatialRQ);
        assertNull(eezById);
    }
}