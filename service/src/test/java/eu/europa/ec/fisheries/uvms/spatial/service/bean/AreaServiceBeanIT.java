package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeSpatialRS;
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
public class AreaServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private AreaTypeService areaTypeService;

    @Test
    public void shouldReturnAreaTypes() throws Exception {
        // when
        AreaTypeSpatialRS areaTypes = areaTypeService.getAreaTypes();

        //then
        assertNotNull(areaTypes);
        assertFalse(areaTypes.getAreaTypes().getAreaType().isEmpty());
    }

}