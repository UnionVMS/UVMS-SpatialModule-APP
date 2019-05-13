package eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.AreaServiceBean2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.TransactionalTests;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.BaseAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.PortDistanceInfoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortAreaEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils.MeasurementUnit;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AreaServiceBean2Test extends TransactionalTests {


    @Inject
    AreaServiceBean2 areaServiceBean;

    @Test
    public void getPortAreasByPointTest(){
        double lon = 17.969;
        double lat = 58.916666666666664;

        List<PortAreaEntity2> result = areaServiceBean.getPortAreasByPoint(lat,lon);

        assertEquals(1, result.size());
        assertEquals("SENYN", result.get(0).getCode());
        assertEquals("Nynäshamn", result.get(0).getName());
    }

    @Test
    public void getPortsByCodeTest(){
        String inputArray[] = {"SENYN", "SETOV", "SENOB", "SEAND", "SEKVK", "SELEI", "SESRT", "SERLD", "SEVEJ", "SESVL", "SEREK"};
        String outputCheckerArray[] = {"Nynäshamn", "Torekov", "Norrebro", "Ängelsbäcksstrand", "Kattvik", "Lervik", "Skäret", "Arild", "Vejbystrand", "Svanshall", "Rekekroken"};
        List<String> inputList = Arrays.asList(inputArray);

        List<PortEntity2> result = areaServiceBean.getPortsByAreaCodes(inputList);
        assertEquals(11, result.size());


        List<String> outputCheckerList = Arrays.asList(outputCheckerArray);
        for (String s: outputCheckerList) {
            assertTrue(s ,result.stream().anyMatch(port -> s.equals((port.getName()))));
        }
    }

    @Test
    public void getPortsByEmptyListTest(){
        List<String> inputList = new ArrayList<>();

        List<PortEntity2> result = areaServiceBean.getPortsByAreaCodes(inputList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findClosestPortByPositionTest(){
        double lon = 17.969;
        double lat = 58.916666666666664;

        PortDistanceInfoDto result = areaServiceBean.findClosestPortByPosition(lat,lon);
        assertNotNull(result);

        assertEquals("SENYN", result.getPort().getCode());
        assertEquals("Nynäshamn", result.getPort().getName());
        assertEquals(1981d, result.getDistance(), 10d);
    }

    @Test
    public void getAreasByPointTest(){
        double lon = 17.536166666666666;
        double lat = 59.391;
        String outputCheckArray[] = {"Swedish Exclusive Economic Zone", "International Commission for the Conservation of Atlantic Tuna", "ICES", "ATLANTIC, NORTHEAST"};

        List<BaseAreaDto> result = areaServiceBean.getAreasByPoint(lat, lon);

        assertEquals(4, result.size());

        List<String> outputCheckerList = Arrays.asList(outputCheckArray);
        for (String s: outputCheckerList) {
            assertTrue(s ,result.stream().anyMatch(base -> s.equals((base.getName()))));
        }
    }

    @Test
    public void getClosestAreasByPointTest(){
        double lon = 17.536166666666666;
        double lat = 59.391;
        String outputCheckArray[] = {"Swedish Exclusive Economic Zone", "International Commission for the Conservation of Atlantic Tuna", "Stallarholmen", "ICES", "Northern Adriatic", "ATLANTIC, NORTHEAST"};

        List<BaseAreaDto> result = areaServiceBean.getClosestAreasByPoint(lat, lon);

        assertEquals(6, result.size());

        List<String> outputCheckerList = Arrays.asList(outputCheckArray);
        for (String s: outputCheckerList) {
            assertTrue(s ,result.stream().anyMatch(base -> s.equals((base.getName()))));
        }
    }

    @Test
    public void getEnrichment(){
        double lon = 18.735362;
        double lat = 63.270487;
        String outputCheckArray[] = {"Swedish Exclusive Economic Zone", "International Commission for the Conservation of Atlantic Tuna", "Örnsköldsvik", "ICES", "Black Sea", "ATLANTIC, NORTHEAST"};


        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ();
        PointType point = new PointType();
        point.setCrs(4326);
        point.setLatitude(lat);
        point.setLongitude(lon);
        request.setPoint(point);

        SpatialEnrichmentRS response = areaServiceBean.getSpatialEnrichment(request);

        assertEquals(3, response.getAreasByLocation().getAreas().size());
        assertEquals("SEOER", response.getAreasByLocation().getAreas().get(0).getCode());
        assertEquals("Örnsköldsvik", response.getAreasByLocation().getAreas().get(0).getName());
        assertEquals(AreaType.PORTAREA, response.getAreasByLocation().getAreas().get(0).getAreaType());

        assertEquals(6, response.getClosestAreas().getClosestAreas().size());
        List<String> outputCheckerList = Arrays.asList(outputCheckArray);
        for (String s: outputCheckerList) {
            assertTrue(s ,response.getClosestAreas().getClosestAreas().stream().anyMatch(base -> s.equals((base.getName()))));
        }

        assertEquals(1, response.getClosestLocations().getClosestLocations().size());
        assertEquals("SEOER", response.getClosestLocations().getClosestLocations().get(0).getCode());
        assertEquals("Örnsköldsvik", response.getClosestLocations().getClosestLocations().get(0).getName());
        assertEquals(2158d / MeasurementUnit.NAUTICAL_MILES.getRatio() , response.getClosestLocations().getClosestLocations().get(0).getDistance(), 0.01d);
    }


    @Test
    public void getAreasByCodeAllDifferentAreaTypesTest(){
        double lon = 17.536166666666666;
        double lat = 59.391;

        List<BaseAreaDto> areasByPoint = areaServiceBean.getAreasByPoint(lat, lon);

        assertEquals(4, areasByPoint.size());

        AreaByCodeRequest areaByCodeRequest = new AreaByCodeRequest();
        List<AreaSimpleType> areaList = new ArrayList<>();
        for (BaseAreaDto base: areasByPoint) {
            AreaSimpleType simpleType = new AreaSimpleType(base.getType().value(), base.getCode(), null);
            areaList.add(simpleType);
        }

        areaByCodeRequest.setAreaSimples(areaList);
        List<AreaSimpleType> result = areaServiceBean.getAreasByCode(areaByCodeRequest);

        assertEquals(4, result.size());
        for (AreaSimpleType simpleType: result) {
            assertNotNull(simpleType.getWkt());
            assertTrue(simpleType.getAreaType() + " : " + simpleType.getAreaCode() ,areasByPoint.stream().anyMatch(base -> simpleType.getAreaCode().equals((base.getCode()))));
        }
    }

    @Test
    public void getAreasByCodeAllSameAreaTypesTest(){
        String inputArray[] = {"SGP", "ATA", "IDN", "TLS", "PRT", "GBR",  "SJM", "USA", "SAU", "NZL", "EGY", "AUS", "COL", "IND", "VNM", "CHN", "MEX"};


        AreaByCodeRequest areaByCodeRequest = new AreaByCodeRequest();
        List<AreaSimpleType> areaList = new ArrayList<>();
        for (String s: inputArray) {
            AreaSimpleType simpleType = new AreaSimpleType(AreaType.EEZ.value(), s, null);
            areaList.add(simpleType);
        }

        areaByCodeRequest.setAreaSimples(areaList);
        List<AreaSimpleType> result = areaServiceBean.getAreasByCode(areaByCodeRequest);

        assertEquals(28, result.size());            //17 codes, some are used several times so the result is 28
        for (AreaSimpleType simpleType: result) {
            assertNotNull(simpleType.getWkt());
            assertTrue(simpleType.getAreaType() + " : " + simpleType.getAreaCode() ,Arrays.asList(inputArray).stream().anyMatch(base -> simpleType.getAreaCode().equals((base))));
        }
    }


}
