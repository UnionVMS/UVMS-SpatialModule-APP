package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertNull;

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
        PortAreasEntity one = dao.findEntityById(PortAreasEntity.class, 1L);
        assertNull(one);
    }
}
