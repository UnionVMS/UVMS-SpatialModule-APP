package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import org.junit.After;
import org.junit.Before;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

public class TransactionalTests extends BuildServiceDeployment {

    @Inject
    private UserTransaction userTransaction;

    @PersistenceContext
    protected EntityManager em;

    @Before
    public void before() throws SystemException, NotSupportedException {
        userTransaction.begin();
    }

    @After
    public void after() throws SystemException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        userTransaction.rollback();
        //userTransaction.commit();
    }

}
