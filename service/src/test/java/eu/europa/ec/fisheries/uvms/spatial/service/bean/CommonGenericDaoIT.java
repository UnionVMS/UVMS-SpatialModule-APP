package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
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
public class CommonGenericDaoIT extends AbstractArquillianIT {

    private static final String EEZ = "eez";
    private static final String REMARKS = "remarks";
    private static final String AUSTRALIA = "Australia";

    @EJB
    private CommonGenericDAO genericDao;

    @Before
    public void beforeEach() {
        assertNotNull("genericDao not injected", genericDao);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void shouldCreateEntity() throws Exception {
        // given
        EezEntity eez = createEezEntity();

        // when
        EezEntity createdEez = (EezEntity) genericDao.createEntity(eez);

        // then
        assertNotNull(createdEez);
        assertEquals(createdEez.getRemarks(), REMARKS);
        assertEquals(createdEez.getEez(), EEZ);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindEntity() throws Exception {
        // when
        EezEntity eezEntity = (EezEntity) genericDao.findEntityById(EezEntity.class, 1);

        // then
        assertEquals(eezEntity.getSovereign(), AUSTRALIA);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetEntity() throws Exception {
        // when
        List<String> areasType = genericDao.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS);

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