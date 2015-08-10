package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;


import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(Arquillian.class)
@Transactional
public class CommonGenericDAOIT extends AbstractArquillianIT {

    @EJB
    private CommonGenericDAO eezDao;

    @Before
    public void beforeEach() {
        assertNotNull("CommonGenericDAO not injected", eezDao);
    }

    @After
    public void afterEach() {
        // perform any cleanup task
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testCRUDOperations() throws Exception {

        EezEntity eez = new EezEntity();
        eez.setEez("eez");
        eez.setRemarks("remarks");

        // create
        EezEntity createdEez = (EezEntity) eezDao.createEntity(eez);
        assertNotNull(createdEez);
        assertEquals(createdEez.getRemarks(), "remarks");

        // find by id
        EezEntity eezEntity = (EezEntity) eezDao.findEntityById(EezEntity.class, 1);
        assertEquals(eezEntity.getSovereign(), "Australia");

    }
}