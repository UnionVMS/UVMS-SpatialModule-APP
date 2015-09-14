package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;

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
        List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();

        //then
        assertNotNull(areaTypeNames);
        assertFalse(areaTypeNames.isEmpty());
    }

}