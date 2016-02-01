package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.util.CountryFactory;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityTransaction;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getNativeQueryByType;
import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class AreaDaoTest extends BaseSpatialDaoTest {

    private AreaDao areaDao = new AreaDao(em, new SqlPropertyHolder());

    @Before
    @SneakyThrows
    public void prepare(){

        EntityTransaction t = em.getTransaction();
        t.begin();
        em.persist(CountryFactory.getCountry("Austria"));
        em.flush();
        t.commit();

    }

    @Test
    @SneakyThrows
    @Parameters(method = "coordinateValues")
    public void shouldReturnAustria(Coordinate coordinate){

        AreaLocationTypesEntity areaLocationTypesEntity = new AreaLocationTypesEntity();
        areaLocationTypesEntity.setTypeName("COUNTRY");
        String nativeQueryByType = getNativeQueryByType(areaLocationTypesEntity.getTypeName());

        List list = areaDao.findAreaOrLocationByCoordinates(geometryFactory.createPoint(coordinate), nativeQueryByType);

        assertEquals(CountryFactory.getCountry("Austria"), list.get(0));

    }

    protected Object[] coordinateValues(){

        return $(
                $(new Coordinate(16.569613472222223, 48.11034788888889),
                  new Coordinate(16.659393, 48.074179),
                  new Coordinate(14.501953, 46.468133)
                )
        );
    }

}
