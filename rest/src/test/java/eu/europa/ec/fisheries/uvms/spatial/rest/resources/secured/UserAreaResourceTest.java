package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.movement.model.util.DateUtil;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

//@RunAsClient              //Need to set up a mocked user module to get this to work. See assets rest tests for references.
@RunWith(Arquillian.class)
public class UserAreaResourceTest extends BuildSpatialRestDeployment {

    @Test
    public void getUserAreaLayerMappingTest() {
        AreaLayerDto response = getSecuredWebTarget()
                .path("userarea")
                .path("layers")
                .request(MediaType.APPLICATION_JSON)
                .header("scopeName", "testScope")
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .get(AreaLayerDto.class);
        
        assertThat(response, is(notNullValue()));
        assertThat(response.getTypeName(), is("USERAREA"));
        assertThat(response.getServiceType(), is("WMS"));
    }


    @Test
    public void createUserAreaTest() {
        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        UserAreasEntity createdArea = restUserArea(area);

        assertTrue(createdArea.getEnabled());
        assertEquals(area.getName(), createdArea.getCode());
        assertEquals(area.getName(), createdArea.getName());
    }

    @Test
    public void updateUserArea(){
        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        UserAreasEntity createdArea = restUserArea(area);

        area.setId(createdArea.getId().toString());
        area.setDescription("Updated description");
        area.setEndDate(DateUtil.parseUTCDateToString(Instant.now().minusSeconds(30)));
        UserAreasEntity updatedArea = restUserArea(area);

        assertEquals(area.getDescription(), updatedArea.getAreaDesc());
        assertFalse(updatedArea.getEnabled());
    }

    @Test
    public void getUserAreasTest(){
        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        UserAreasEntity createdArea = restUserArea(area);

        List<UserAreasEntity> idResponse = getSecuredWebTarget()
                .path("userarea")
                .path("get")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.json(Arrays.asList(createdArea.getCode())), new GenericType<List<UserAreasEntity>>() {});

        assertEquals(1, idResponse.size());
        assertNotNull(idResponse.get(0).getId());
        assertEquals(area.getName(), idResponse.get(0).getCode());
        assertEquals(area.getName(), idResponse.get(0).getName());
    }

    @Test
    public void deleteUserArea(){
        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        UserAreasEntity createdArea = restUserArea(area);

        Response response = getSecuredWebTarget()
                .path("userarea")
                .path(createdArea.getId().toString())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .delete( Response.class);

        assertEquals(200, response.getStatus());

        List<UserAreasEntity> idResponse = getSecuredWebTarget()
                .path("userarea")
                .path("get")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.json(Arrays.asList(createdArea.getCode())), new GenericType<List<UserAreasEntity>>() {});

        assertTrue(idResponse.isEmpty());
    }

    @Test
    public void getAreasByScopeTest(){
        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        UserAreasEntity createdArea = restUserArea(area);

        List<UserAreasEntity> response = getSecuredWebTarget()
                .path("userarea")
                .path("list")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .header("scopeName", area.getScopeSelection())
                .get( new GenericType<List<UserAreasEntity>>() {});

        assertFalse(response.isEmpty());
        response.stream().anyMatch(a -> createdArea.getId() == a.getId());
        response.stream().anyMatch(a -> createdArea.getCode().equals(a.getCode()));
    }




    private UserAreaDto createUserArea(String code){
        UserAreaDto area = new UserAreaDto();
        area.setDatasetName("Test Dataset");
        area.setDescription("User Area Test Description");
        area.setEndDate(DateUtil.parseUTCDateToString(Instant.now().plusSeconds(60)));
        area.setStartDate(DateUtil.parseUTCDateToString(Instant.now().minusSeconds(60)));
        area.setExtent("Test Extent");
        area.setName(code);
        area.setScopeSelection("Test Scope Selection");
        area.setSubType("Test Subtype");
        area.setType(AreaType.USERAREA.value());
        area.setGeometry("MULTIPOLYGON(((-349.5904541015625 57.43903710833487,-349.45861816406244 56.42605447604976,-346.6790771484375 56.51404838330879,-347.618408203125 57.468589192089354,-348.3819580078125 56.74971110497151,-349.5904541015625 57.43903710833487)))");       //This is monacos eez, no I do not know why it is defined as -350

        return area;
    }

    private UserAreasEntity restUserArea(UserAreaDto area){
        UserAreasEntity response = getSecuredWebTarget()
                .path("userarea")
                .request(MediaType.APPLICATION_JSON)
                .header("scopeName", "testScope")
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.json(area), UserAreasEntity.class);

        assertThat(response, is(notNullValue()));
        assertNotNull(response.getId());
        return response;
    }
    
}
