package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import eu.europa.ec.fisheries.uvms.commons.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ConfigResourceDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.MapConfigDto;
import eu.europe.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;

@RunAsClient
@RunWith(Arquillian.class)
public class ConfigResourceTest extends BuildSpatialRestDeployment {

    @Test
    public void getReportMapConfigByIdTest() {
        ConfigResourceDto config = new ConfigResourceDto();
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
        assertThat(mapConfig.getMap().getProjectionDto().getEpsgCode(), is(3857));
    }
    
    @Test
    public void getBasicReportMapConfigTest() {
        ResponseDto<MapConfigDto> response = getSecuredWebTarget()
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
        assertThat(mapConfig.getMap().getProjectionDto().getEpsgCode(), is(3857));
    }
}
