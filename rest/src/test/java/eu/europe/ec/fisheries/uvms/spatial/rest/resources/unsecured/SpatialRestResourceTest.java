package eu.europe.ec.fisheries.uvms.spatial.rest.resources.unsecured;

import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentCategoryType;
import eu.europe.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;
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

import static org.junit.Assert.assertEquals;

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
    public void pong() throws Exception{

        List <MovementType> request = new ArrayList<>();
        Response response =  getWebTarget()
                .path("json")
                .path("pong")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(""), Response.class);

        System.out.println("Now");
        //Thread.sleep(1000 * 60 * 5);

        assertEquals(200 ,response.getStatus());
    }

    @Test
    public void pong2() throws Exception{

        List <MovementType> request = new ArrayList<>();
        Response response =  getWebTarget()
                .path("json")
                .path("pong2")
                .request(MediaType.APPLICATION_JSON)
                .get();

        System.out.println("Now");
        //Thread.sleep(1000 * 60 * 10);

        assertEquals(200 ,response.getStatus());
    }

    @Test
    public void getSegmentCategoryTypeTest(){


        MovementType move1 = createBasicMovementType(2d, 3d);
        MovementType move2 = createBasicMovementType(3d, 2d);
        List<MovementType> input = new ArrayList<>();
        input.add(move1);
        input.add(move2);

        SegmentCategoryType output = getSegmentCategoryType(input);

        System.out.println(output);



    }


    private SegmentCategoryType getSegmentCategoryType( List<MovementType> request){

        Response response =  getWebTarget()
                .path("json")
                .path("getSegmentCategoryType")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(request), Response.class);

        assertEquals(200, response.getStatus());
        return response.readEntity(new GenericType<SegmentCategoryType>() {});
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
