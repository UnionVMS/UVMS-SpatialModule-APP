package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class PortAreaDaoTest extends BaseSpatialDaoTest {

    private PortAreaDao dao = new PortAreaDao(em);

    @Test
    @SneakyThrows
    public void shouldReturnIntersectedPortArea(){
        List<PortAreasEntity> intersects = dao.intersects(new GeometryBuilder().point(1, 1));
    }

    @Test
    @SneakyThrows
    public void testFindOne(){
        List<PortAreasEntity> one = dao.findOne(1L);
        assertTrue(one.isEmpty());
    }
}
