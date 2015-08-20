package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAOBean;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.util.QueryNameConstants;
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
    private CommonGenericDAOBean genericDAO;

    @Before
    public void beforeEach() {
        assertNotNull("genericDAO not injected", genericDAO);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public void shouldFindEntity() throws Exception {
        // when
        EezEntity eezEntity = (EezEntity) genericDAO.findEntityById(EezEntity.class, 1);

        // then
        assertEquals(eezEntity.getSovereign(), AUSTRALIA);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetEntity() throws Exception {
        // when
        List<String> areasType = genericDAO.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS);

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