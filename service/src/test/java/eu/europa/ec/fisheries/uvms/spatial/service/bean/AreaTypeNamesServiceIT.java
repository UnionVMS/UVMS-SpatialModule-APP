package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeNamesSpatialRS;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kopyczmi on 04-Aug-15.
 */
@RunWith(Arquillian.class)
public class AreaTypeNamesServiceIT extends AbstractArquillianIT {

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @Test
    public void shouldReturnAreaTypes() throws Exception {
        // when
        AreaTypeNamesSpatialRS areaTypes = areaTypeNamesService.getAreaTypesQueue();

        //then
        assertNotNull(areaTypes);
        assertFalse(areaTypes.getAreaTypes().getAreaType().isEmpty());
    }

}