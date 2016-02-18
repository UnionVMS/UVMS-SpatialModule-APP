package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.PortAreaGeoJsonDto;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class PortAreaServiceBeanIT extends AbstractArquillianIT {

    @EJB
    private PortAreaService portAreaService;

    @Test
    public void shouldUpdatePortArea() throws ServiceException, ParseException {
        // given
        PortAreaGeoJsonDto portAreaGeoJsonDto = new PortAreaGeoJsonDto();
        portAreaGeoJsonDto.setId(new Long(1));
        portAreaGeoJsonDto.setGeometry(createGeometry());

        // when
        long portAreaId = portAreaService.updatePortArea(portAreaGeoJsonDto);

        // then
        assertNotNull(portAreaId);
    }

    @Test
    public void shouldDeletePortArea() throws ServiceException {
        // given
        Long portAreaId = new Long(1);

        // when
        try {
            portAreaService.deletePortArea(portAreaId);
        }
        catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    private Geometry createGeometry() throws ParseException {
        WKTReader wkt = new WKTReader();
        Geometry geometry = wkt.read("MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)),\n" +
                "((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),\n" +
                "(30 20, 20 15, 20 25, 30 20)))");
        geometry.setSRID(4326);
        return geometry;
    }

}