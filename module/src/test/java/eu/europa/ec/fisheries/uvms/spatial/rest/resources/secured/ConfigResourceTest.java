package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunAsClient
@RunWith(Arquillian.class)
public class ConfigResourceTest extends BuildSpatialRestDeployment {

    @Test
    public void getReportMapConfigByIdTest() {
     /*   ConfigResourceDto config = new ConfigResourceDto();
        ResponseDto<MapConfigDto> response = getSecuredWebTarget()
                .path("config")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("roleName", "testRole")
                .header("scopeName", "testScope")
                .post(Entity.json(config), new GenericType<ResponseDto<MapConfigDto>>() {});
        
        assertThat(response, is(notNullValue()));
        MapConfigDto mapConfig = response.getData();
        assertThat(mapConfig, is(notNullValue()));
        assertThat(mapConfig.getMap(), is(notNullValue()));
        assertThat(mapConfig.getMap().getProjectionDto().getEpsgCode(), is(3857)); */
    }
    
    @Test
    public void getBasicReportMapConfigTest() {
       /* ResponseDto<MapConfigDto> response = getSecuredWebTarget()
                .path("config")
                .path("basic")
                .request(MediaType.APPLICATION_JSON)
                .header("roleName", "testRole")
                .header("scopeName", "testScope")
                .get(new GenericType<ResponseDto<MapConfigDto>>() {});
        
        // MapProjectionId = 1, returned by UserModuleMock
        assertThat(response, is(notNullValue()));
        MapConfigDto mapConfig = response.getData();
        assertThat(mapConfig, is(notNullValue()));
        assertThat(mapConfig.getMap(), is(notNullValue()));
        assertThat(mapConfig.getMap().getProjectionDto().getEpsgCode(), is(3857));*/
    }
}
