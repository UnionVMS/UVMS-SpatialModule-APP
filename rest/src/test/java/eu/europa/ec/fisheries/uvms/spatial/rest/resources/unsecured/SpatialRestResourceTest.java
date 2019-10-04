package eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryByPortCodeRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryByPortCodeResponse;
import eu.europa.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaTransitionsDTO;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.GeometryUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunAsClient
@RunWith(Arquillian.class)
public class SpatialRestResourceTest extends BuildSpatialRestDeployment {

    @Test
    public void getSegmentCategoryTypeEmptyListAsInputTest(){

        List <MovementType> request = new ArrayList<>();
        Response response =  getWebTarget()
                .path("json")
                .path("getSegmentCategoryType")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(request), Response.class);

        assertEquals(400 ,response.getStatus());
    }

    @Test
    public void getSegmentCategoryTypeListWithOneAsInputTest(){

        MovementType move1 = createBasicMovementType(2d, 3d);
        List <MovementType> request = new ArrayList<>();
        request.add(move1);
        Response response =  getWebTarget()
                .path("json")
                .path("getSegmentCategoryType")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(request), Response.class);

        assertEquals(400 ,response.getStatus());
    }


    @Test
    public void getSegmentCategoryTypeSameTimeTest(){


        MovementType move1 = createBasicMovementType(2d, 3d);
        MovementType move2 = createBasicMovementType(3d, 2d);
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);

        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.NULL_DUR, output);
    }


    @Test
    public void getSegmentCategoryTypeGapTest(){


        MovementType move1 = createBasicMovementType(2d, 3d);
        MovementType move2 = createBasicMovementType(3d, 2d);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);

        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.GAP, output);
    }

    @Test
    public void getSegmentCategoryTypeAnchoredTest(){


        MovementType move1 = createBasicMovementType(2d, 3d);
        MovementType move2 = createBasicMovementType(2d, 3d);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);

        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.ANCHORED, output);
    }


    @Test
    public void getSegmentCategoryTypeJumpTest() throws Exception{


        MovementType move1 = createBasicMovementType(-15.301291d, 47.006736d);
        MovementType move2 = createBasicMovementType( -61.214025d, 36.745943d);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);


        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.JUMP, output);
    }

    @Test
    public void getSegmentCategoryTypeInPortTest() throws Exception{


        MovementType move1 = createBasicMovementType(11.922098, 57.700490);
        MovementType move2 = createBasicMovementType( 11.928836, 57.693246);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);


        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.IN_PORT, output);
    }

    @Test
    public void getSegmentCategoryTypeExitPortTest() throws Exception{


        MovementType move1 = createBasicMovementType(11.922098, 57.700490);
        MovementType move2 = createBasicMovementType( 3.786473, 56.565762);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);


        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.EXIT_PORT, output);
    }

    @Test
    public void getSegmentCategoryTypeExitPortReverseMovementPositionsTest() throws Exception{


        MovementType move1 = createBasicMovementType(11.922098, 57.700490);
        MovementType move2 = createBasicMovementType( 3.786473, 56.565762);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move2);
        input.add(move1);


        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.EXIT_PORT, output);
    }

    @Test
    public void getSegmentCategoryTypeEnterPortTest() throws Exception{


        MovementType move2 = createBasicMovementType(11.922098, 57.700490);
        MovementType move1 = createBasicMovementType( 3.786473, 56.565762);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);


        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.ENTER_PORT, output);
    }

    @Test
    public void getSegmentCategoryTypeEnterPortReverseMovementPositionsTest() throws Exception{


        MovementType move2 = createBasicMovementType(11.922098, 57.700490);
        MovementType move1 = createBasicMovementType( 3.786473, 56.565762);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move2);
        input.add(move1);


        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.ENTER_PORT, output);
    }

    @Test
    public void getSegmentCategoryTypeTwoDifferentPortsTest() throws Exception{   //Exactly what this is supposed to return is rather unclear


        MovementType move1 = createBasicMovementType(11.922098, 57.700490);
        MovementType move2 = createBasicMovementType( 12.993741, 55.615228);
        move2.setPositionTime(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);


        SegmentCategoryType output = getSegmentCategoryType(input);

        assertEquals(SegmentCategoryType.OTHER, output);
    }


    @Test
    public void getEnrichmentAndTransitionsWOSecondInput(){


        Response response =  getWebTarget()
                .path("json")
                .path("getEnrichmentAndTransitions")
                .queryParam("firstLongitude", new Double(0))
                .queryParam("firstLatitude", new Double(0))
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        assertEquals(400 ,response.getStatus());

        response =  getWebTarget()
                .path("json")
                .path("getEnrichmentAndTransitions")
                .queryParam("firstLongitude", new Double(0))
                .queryParam("firstLatitude", new Double(0))
                .queryParam("secondLatitude", new Double(0))
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        assertEquals(400 ,response.getStatus());
    }

    @Test
    public void getEnrichmentAndTransitionsTest(){
        AreaTransitionsDTO response = getEnrichmentAndTransitions(11.940240, 57.702816, -9.080324, 38.713954);

        assertFalse(response.getExitedAreas().isEmpty());
        assertFalse(response.getEnteredAreas().isEmpty());

        assertFalse(response.getSpatialEnrichmentRS().getAreasByLocation().getAreas().isEmpty());

    }

    @Test
    public void getEnrichmentAndTransitionsWOFirstMoveTest(){
        AreaTransitionsDTO response = getEnrichmentAndTransitions(null, null, -9.080324, 38.713954);

        assertTrue(response.getExitedAreas().isEmpty());
        assertFalse(response.getEnteredAreas().isEmpty());

        assertFalse(response.getSpatialEnrichmentRS().getAreasByLocation().getAreas().isEmpty());
    }

    @Test
    public void getGeometryByPortCode() throws ParseException {

        GeometryByPortCodeRequest request = new GeometryByPortCodeRequest();
        request.setPortCode("ESVGO");

        Response response =  getWebTarget()
                .path("json")
                .path("getGeometryByPortCode")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(request), Response.class);

        assertEquals(200, response.getStatus());

        GeometryByPortCodeResponse geometryByPortCodeResponse = response.readEntity(GeometryByPortCodeResponse.class);
        String portGeometry = geometryByPortCodeResponse.getPortGeometry();

        Coordinate coordinate = new Coordinate(-8.718, 42.242);
        CoordinateArraySequence sequence = new CoordinateArraySequence(new Coordinate[]{coordinate});
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = new Point(sequence, geometryFactory);
        MultiPoint multiPoint = new MultiPoint(new Point[]{point}, geometryFactory);

        MultiPoint responseMultipoint = (MultiPoint) GeometryUtils.wktToGeometry(portGeometry);
        assertEquals(multiPoint, responseMultipoint);
    }


    /*      HELPERS     */

    private SegmentCategoryType getSegmentCategoryType( List<MovementType> request){

        Response response =  getWebTarget()
                .path("json")
                .path("getSegmentCategoryType")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(request), Response.class);

        assertEquals(200, response.getStatus());
        return response.readEntity(new GenericType<SegmentCategoryType>() {});
    }


    private AreaTransitionsDTO getEnrichmentAndTransitions(Double firstLongitude, Double firstLatitude, Double secondLongitude, Double secondLatitude){

        Response response =  getWebTarget()
                .path("json")
                .path("getEnrichmentAndTransitions")
                .queryParam("firstLongitude", firstLongitude)
                .queryParam("firstLatitude", firstLatitude)
                .queryParam("secondLongitude", secondLongitude)
                .queryParam("secondLatitude", secondLatitude)
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        assertEquals(200 ,response.getStatus());
        return response.readEntity(new GenericType<AreaTransitionsDTO>() {});
    }

    private MovementType createBasicMovementType(double lon, double lat){
        MovementType move = new MovementType();
        MovementPoint point = new MovementPoint();
        point.setLongitude(lon);
        point.setLatitude(lat);
        move.setPosition(point);
        move.setPositionTime(new Date());

        return move;
    }
}
