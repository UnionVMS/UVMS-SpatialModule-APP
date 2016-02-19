package eu.europe.ec.fisheries.uvms.spatial.rest.service;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.LocationCoordinateType;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;

@RunWith(Arquillian.class)
@RunAsClient
public class LocationResourceIT extends AbstractArquillianIT {

	@ArquillianResource
    private URL deploymentURL;
    
    @Test
    public void getLocationByIdTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	LocationCoordinateType locationDto = getlocationTypeDto("1", "PORT", null, null, null);
    	
    	Response response = webTarget.path("/locationdetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(locationDto,MediaType.APPLICATION_JSON));
    	
    	assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    	ResponseDto responseDto = response.readEntity(ResponseDto.class);
    	assertEquals(HttpServletResponse.SC_OK, responseDto.getCode());
    }
    
    @Test
    public void getLocationByCoordinatesTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	LocationCoordinateType locationDto = getlocationTypeDto(null, "PORT", -9.5, 41.0, 4326);
    	
    	Response response = webTarget.path("/locationdetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(locationDto,MediaType.APPLICATION_JSON));
    	
    	assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    	ResponseDto responseDto = response.readEntity(ResponseDto.class);
    	assertEquals(HttpServletResponse.SC_OK, responseDto.getCode());
    } 
    
    @Test
    public void serviceExceptionTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	LocationCoordinateType locationDto = getlocationTypeDto("1", "INVALID", null, null, null);
    	
    	Response response = webTarget.path("/locationdetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(locationDto,MediaType.APPLICATION_JSON));
    	
    	assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    	ResponseDto responseDto = response.readEntity(ResponseDto.class);
    	assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseDto.getCode());
    	assertEquals("INVALID_AREA_LOCATION_TYPE", responseDto.getMsg());
    }
    
    @Test
    public void locationTypeInputValidationTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	LocationCoordinateType locationDto = getlocationTypeDto("1", null, null, null, null);
    	
    	Response response = webTarget.path("/locationdetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(locationDto,MediaType.APPLICATION_JSON));
    	
    	assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    	ResponseDto responseDto = response.readEntity(ResponseDto.class);
    	assertEquals(HttpServletResponse.SC_BAD_REQUEST, responseDto.getCode());
    	assertEquals("INPUT_VALIDATION_FAILED", responseDto.getMsg());
    }
    
    @Test
    public void coordinateInputValidationTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	LocationCoordinateType locationDto = getlocationTypeDto(null, "PORT", 10.0, 10.0, null);
    	
    	Response response = webTarget.path("/locationdetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(locationDto,MediaType.APPLICATION_JSON));
    	
    	assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    	ResponseDto responseDto = response.readEntity(ResponseDto.class);
    	assertEquals(HttpServletResponse.SC_BAD_REQUEST, responseDto.getCode());
    	assertEquals("INPUT_VALIDATION_FAILED", responseDto.getMsg());
    }
    
    private LocationCoordinateType getlocationTypeDto(String id, String locationType, Double longitude, Double latitude, Integer crs) {
    	LocationCoordinateType locationCoordinateTypeDto = new LocationCoordinateType();
    	locationCoordinateTypeDto.setId(id);
    	locationCoordinateTypeDto.setLocationType(locationType);
    	locationCoordinateTypeDto.setLongitude(longitude);
    	locationCoordinateTypeDto.setLatitude(latitude);
    	locationCoordinateTypeDto.setCrs(crs);
    	return locationCoordinateTypeDto;
    }
}
