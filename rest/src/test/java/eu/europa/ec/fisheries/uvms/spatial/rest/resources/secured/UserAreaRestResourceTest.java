package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
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

@RunWith(Arquillian.class)
public class UserAreaRestResourceTest extends BuildSpatialRestDeployment {

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
    public void getUserAreaLayerMappingWithUserAreaTest() {
        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        UserAreasEntity createdArea = restUserArea(area);

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

        assertTrue(response.getIdList().stream().anyMatch(a -> a.getGid() == createdArea.getId()));
    }

    @Test
    public void getUserAreaGroupTest() {
        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        area.setAreaGroup("TestType");
        UserAreasEntity createdArea = restUserArea(area);

        AreaLayerDto response = getSecuredWebTarget()
                .path("userarea")
                .path("layers")
                .path("areaGroups")
                .request(MediaType.APPLICATION_JSON)
                .header("scopeName", "testScope")
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .get(AreaLayerDto.class);

        assertThat(response, is(notNullValue()));
        assertThat(response.getTypeName(), is("USERAREA"));
        assertThat(response.getServiceType(), is("WMS"));

        assertFalse(response.getDistinctAreaGroups().isEmpty());
        assertTrue(response.getDistinctAreaGroups().contains(area.getAreaGroup()));
    }

    @Test
    public void getUserAreaGroupCheckDistinctTest() {
        String areaGroup1 = "TestAreaGroup" + (int)(Math.random()*10000.0);
        String areaGroup2 = "TestAreaGroup" + (int)(Math.random()*10000.0);

        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        area.setAreaGroup(areaGroup1);
        restUserArea(area);

        area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        area.setAreaGroup(areaGroup1);
        restUserArea(area);

        area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        area.setAreaGroup(areaGroup2);
        restUserArea(area);

        AreaLayerDto response = getSecuredWebTarget()
                .path("userarea")
                .path("layers")
                .path("areaGroups")
                .request(MediaType.APPLICATION_JSON)
                .header("scopeName", "testScope")
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .get(AreaLayerDto.class);

        assertThat(response, is(notNullValue()));
        assertThat(response.getTypeName(), is("USERAREA"));
        assertThat(response.getServiceType(), is("WMS"));

        assertFalse(response.getDistinctAreaGroups().isEmpty());
        assertTrue(response.getDistinctAreaGroups().size() > 1);
        assertTrue(response.getDistinctAreaGroups().contains(areaGroup1));
        assertTrue(response.getDistinctAreaGroups().contains(areaGroup2));

        assertTrue(response.getDistinctAreaGroups().remove(areaGroup1));
        assertFalse(response.getDistinctAreaGroups().remove(areaGroup1));
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
        area.setEndDate(DateUtils.dateToHumanReadableString(Instant.now().minusSeconds(30)));
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
        assertTrue(response.stream().anyMatch(a -> createdArea.getId() == a.getId()));
        assertTrue(response.stream().anyMatch(a -> createdArea.getCode().equals(a.getCode())));
    }

    @Test
    @Ignore("This will not work bc the list endpoint checks for username or scope")
    public void getAreasByScopeTwoScopesOnlyGetOneTest(){
        UserAreaDto area = createUserArea(UUID.randomUUID().toString().substring(0,20));
        UserAreasEntity createdArea = restUserArea(area);

        UserAreaDto areaWithOtherScope = createUserArea(UUID.randomUUID().toString().substring(0,20));
        areaWithOtherScope.setScopeSelection(Arrays.asList("Dont select this scope"));
        UserAreasEntity createdAreaWithAnotherScope = restUserArea(areaWithOtherScope);

        List<UserAreasEntity> response = getSecuredWebTarget()
                .path("userarea")
                .path("list")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .header("scopeName", area.getScopeSelection())
                .get( new GenericType<List<UserAreasEntity>>() {});

        assertFalse(response.isEmpty());
        assertTrue(response.stream().anyMatch(a -> createdArea.getId() == a.getId()));
        assertTrue(response.stream().anyMatch(a -> createdArea.getCode().equals(a.getCode())));

        assertFalse(response.stream().anyMatch(a -> createdAreaWithAnotherScope.getId() == a.getId()));
    }


    private UserAreaDto createUserArea(String code){
        UserAreaDto area = new UserAreaDto();
        area.setDatasetName("Test Dataset");
        area.setDescription("User Area Test Description");
        area.setEndDate(DateUtils.dateToHumanReadableString(Instant.now().plusSeconds(60)));
        area.setStartDate(DateUtils.dateToHumanReadableString(Instant.now().minusSeconds(60)));
        area.setExtent("Test Extent");
        area.setName(code);
        area.setScopeSelection(Arrays.asList("Test Scope Selection"));
        area.setAreaGroup("Test Subtype");
        area.setType(AreaType.USERAREA);
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
