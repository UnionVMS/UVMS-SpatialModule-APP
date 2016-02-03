package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup.AreaGroupDto;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by padhyad on 1/26/2016.
 */
@RunWith(Arquillian.class)
public class AreaGroupServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private AreaGroupService areaGroupService;

    @Test
    public void getAreaGroupsTest() {
        List<AreaGroupDto> areaGroupDtos = areaGroupService.getAreaGroups("rep_power");
        assertNotNull(areaGroupDtos);
        assertFalse(areaGroupDtos.isEmpty());
    }

    @Test
    public void deleteAreaGroupTest() {
        try {
            areaGroupService.deleteAreaGroup(1L);
        } catch (Exception e) {
            assertNull(e);
        }
    }
}
