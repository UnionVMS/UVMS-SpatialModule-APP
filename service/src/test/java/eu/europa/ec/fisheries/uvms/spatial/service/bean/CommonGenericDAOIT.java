package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(Arquillian.class)
@Transactional
public class CommonGenericDAOIT extends AbstractArquillianIT {

    private static final String EEZ = "eez";
    private static final String REMARKS = "remarks";
    private static final String AUSTRALIA = "Australia";

    @EJB
    private CommonGenericDAO genericDAO;

    @Before
    public void beforeEach() {
        assertNotNull("CommonGenericDAO not injected", genericDAO);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void shouldCreateEntity() throws Exception {
        // given
        EezEntity eez = createEezEntity();

        // when
        EezEntity createdEez = (EezEntity) genericDAO.createEntity(eez);

        // then
        assertNotNull(createdEez);
        assertEquals(createdEez.getRemarks(), REMARKS);
        assertEquals(createdEez.getEez(), EEZ);
    }

    @Test
    public void shouldFindEntity() throws Exception {
        // when
        EezEntity eezEntity = (EezEntity) genericDAO.findEntityById(EezEntity.class, 1);

        // then
        assertEquals(eezEntity.getSovereign(), AUSTRALIA);
    }

    @Test
    public void shouldGetEntity() throws Exception {
        // when
        List<String> areasType = genericDAO.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL);

        // then
        assertThat(areasType).isNotEmpty();
    }

    private EezEntity createEezEntity() {
        EezEntity eez = new EezEntity();
        eez.setRemarks(REMARKS);
        eez.setEez(EEZ);
        return eez;
    }
}