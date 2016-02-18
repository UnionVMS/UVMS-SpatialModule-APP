package eu.europe.ec.fisheries.uvms.spatial.rest.service;

import static org.junit.Assert.assertEquals;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaFilterDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.geocoordinate.AreaTypeDto;

@RunWith(Arquillian.class)
@RunAsClient
public class AreaResourceIT extends AbstractArquillianIT {
	
	@ArquillianResource
    private URL deploymentURL;
	
	
	@Test
	public void getAreaPropertiesTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {
		Response response = webTarget.path("/areaproperties").request(MediaType.APPLICATION_JSON).post(Entity.entity(getMockedAreaTypeDto(),MediaType.APPLICATION_JSON));
		testOk(response);		
	}
	
	@Test
	public void getAreaFilterTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {
		AreaFilterDto areaFilterDto = new AreaFilterDto("eez", "islands");
		Response response = webTarget.path("/areasbyfilter").request(MediaType.APPLICATION_JSON).post(Entity.entity(areaFilterDto,MediaType.APPLICATION_JSON));
		testOk(response);
	}
	
	@Test
	public void getAreaLayerMappingTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {
		Response response = webTarget.path("/arealayers" ).request().get();		
		testOk(response);
	}
    
    @Test
    public void getEezAreaByIdTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	AreaTypeDto areaDto = getareaTypeDto("1", "EEZ", null, null, null, true);    	
    	Response response = webTarget.path("/areadetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(areaDto,MediaType.APPLICATION_JSON));
    	testOk(response);
    } 
    
    @Test
    public void getEezAreaByCoordinatesTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	AreaTypeDto areaDto = getareaTypeDto("1", "EEZ", -9.5, 41.0, 4326, true);    	
    	Response response = webTarget.path("/areadetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(areaDto,MediaType.APPLICATION_JSON));    	
    	testOk(response);
    } 
    
    @Test
    public void getRfmoAreaByIdTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	AreaTypeDto areaDto = getareaTypeDto("1", "RFMO", null, null, null, true);    	
    	Response response = webTarget.path("/areadetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(areaDto,MediaType.APPLICATION_JSON));    	
    	testOk(response);
    } 
    
    @Test
    public void serviceExceptionTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	AreaTypeDto areaDto = getareaTypeDto("1", "INVALLID", null, null, null, true);    	
    	Response response = webTarget.path("/areadetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(areaDto,MediaType.APPLICATION_JSON));    	
    	testInternalServerError(response);
    }
    
    @Test
    public void areaTypeInputValidationTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	AreaTypeDto areaDto = getareaTypeDto("1", null, null, null, null, true);    	
    	Response response = webTarget.path("/areadetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(areaDto,MediaType.APPLICATION_JSON));    	
    	testBadRequest(response);
    }
    
    @Test
    public void coordinateInputValidationTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	AreaTypeDto areaDto = getareaTypeDto(null, "PORT", 10.0, 10.0, null, true);    	
    	Response response = webTarget.path("/areadetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(areaDto,MediaType.APPLICATION_JSON));    	
    	testBadRequest(response);
    }
    
    @Test
    public void isGeomInputValidationTest(@ArquillianResteasyResource("rest/") ResteasyWebTarget webTarget) {    	
    	AreaTypeDto areaDto = getareaTypeDto(null, "PORT", 10.0, 10.0, null, null);    	
    	Response response = webTarget.path("/areadetails" ).request(MediaType.APPLICATION_JSON).post(Entity.entity(areaDto,MediaType.APPLICATION_JSON));    	
    	testBadRequest(response);
    }
    
	private List<AreaTypeDto> getMockedAreaTypeDto() {
		AreaTypeDto areaOne = new AreaTypeDto();
		areaOne.setAreaType("eez");
		areaOne.setGid("1");
		
		AreaTypeDto areaTwo = new AreaTypeDto();
		areaTwo.setAreaType("rfmo");
		areaTwo.setGid("2");
		
		return Arrays.asList(areaOne, areaTwo);
	}
    
    private AreaTypeDto getareaTypeDto(String id, String areaType, Double longitude, Double latitude, Integer crs, Boolean isGeom) {
    	AreaTypeDto areaTypeDto = new AreaTypeDto();
    	areaTypeDto.setId(id);
    	areaTypeDto.setAreaType(areaType);
    	areaTypeDto.setLongitude(longitude);
    	areaTypeDto.setLatitude(latitude);
    	areaTypeDto.setCrs(crs);
    	areaTypeDto.setIsGeom(isGeom);
    	return areaTypeDto;
    }
    
    private void testOk(Response response) {
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		ResponseDto responseDto = response.readEntity(ResponseDto.class);
    	assertEquals(HttpServletResponse.SC_OK, responseDto.getCode());
	}
	
    private void testBadRequest(Response response) {
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    	ResponseDto responseDto = response.readEntity(ResponseDto.class);
    	assertEquals(HttpServletResponse.SC_BAD_REQUEST, responseDto.getCode());
    	assertEquals("INPUT_VALIDATION_FAILED", responseDto.getMsg());
	}
	
    private void testInternalServerError(Response response) {
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    	ResponseDto responseDto = response.readEntity(ResponseDto.class);
    	assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseDto.getCode());
	}
}
