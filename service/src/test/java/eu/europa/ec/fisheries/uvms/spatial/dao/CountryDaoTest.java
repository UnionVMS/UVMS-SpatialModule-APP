package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;

public class CountryDaoTest extends BaseSpatialDaoTest {

    private CountryDao dao = new CountryDao(em);

    @Before
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_COUNTRY_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void testFindAllCountriesDesc() {
        dbSetupTracker.skipNextLaunch();

        List<CountryEntity> entityByNamedQuery = dao.findEntitiesByNamedQuery(CountryEntity.FIND_ALL);

        assertEquals("Portugal", entityByNamedQuery.get(0).getName());
        assertEquals("PRT", entityByNamedQuery.get(0).getCode());

        assertEquals("Spain", entityByNamedQuery.get(1).getName());
        assertEquals("ESP", entityByNamedQuery.get(1).getCode());

    }
}
