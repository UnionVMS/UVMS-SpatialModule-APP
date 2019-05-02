package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import eu.europa.ec.fisheries.uvms.commons.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.layer.UserAreaLayerDto;
import eu.europe.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;

@RunAsClient
@RunWith(Arquillian.class)
public class UserAreaResourceTest extends BuildSpatialRestDeployment {

    @Test
    public void getUserAreaLayerMappingTest() {
        ResponseDto<UserAreaLayerDto> response = getSecuredWebTarget()
                .path("userarea")
                .path("layers")
                .request(MediaType.APPLICATION_JSON)
                .header("scopeName", "testScope")
                .get(new GenericType<ResponseDto<UserAreaLayerDto>>() {});
        
        assertThat(response, is(notNullValue()));
        UserAreaLayerDto userAreaLayer = response.getData();
        assertThat(userAreaLayer, is(notNullValue()));
        assertThat(userAreaLayer.getTypeName(), is("USERAREA"));
        assertThat(userAreaLayer.getServiceType(), is("WMS"));
    }
    
}
