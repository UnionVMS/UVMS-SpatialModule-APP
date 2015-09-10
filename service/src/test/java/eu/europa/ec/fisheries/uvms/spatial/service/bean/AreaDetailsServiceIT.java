package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialResponse;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ErrorsType;

/**
 * @author padhyad
 * Arquillian integration test for GetAreaDetails API
 */
@RunWith(Arquillian.class)
public class AreaDetailsServiceIT {
	
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "spatial-service.war").addPackages(true, "eu.europa")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/orm.xml")
                .addAsResource("config.properties")
                .addAsResource("META-INF/jboss-deployment-structure.xml")
                .addAsResource("nativeSql.properties")
                .addAsResource("logback.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        File[] files = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST, ScopeType.PROVIDED).resolve().withTransitivity().as(File.class);
        webArchive.addAsLibraries(files);

        return webArchive;
    }
	
	@EJB
	private AreaDetailsService areaDetailsService;
	
	/**
	 * Test EEZ entity for valid response
	 */
	@Test
	public void getEezAreaDetailsTest() {
		AreaDetailsSpatialRequest request = new AreaDetailsSpatialRequest(new AreaTypeEntry("1", "eez"));		
		AreaDetailsSpatialResponse response = areaDetailsService.getAreaDetails(request);
		assertNull(response.getResponseMessage().getErrors());
		assertNotNull(response.getResponseMessage().getSuccess());		
		assertNotNull(response.getAreaDetails().getAreaProperties());
		assertEquals(response.getAreaDetails().getAreaProperties().isEmpty(), false);
	}
	
	/**
	 * Test RFMO entity for valid response
	 */
	@Test
	public void getRfmoAreaDetailsTest() {
		AreaDetailsSpatialRequest request = new AreaDetailsSpatialRequest(new AreaTypeEntry("1", "rfmo"));		
		AreaDetailsSpatialResponse response = areaDetailsService.getAreaDetails(request);
		assertNull(response.getResponseMessage().getErrors());
		assertNotNull(response.getResponseMessage().getSuccess());		
		assertNotNull(response.getAreaDetails().getAreaProperties());
		assertEquals(response.getAreaDetails().getAreaProperties().isEmpty(), false);
	}
	

	/**
	 * Test for invalid input
	 */
	@Test
	public void getAreaDetailsIncorrectIdTest() {
		AreaDetailsSpatialRequest request = new AreaDetailsSpatialRequest(new AreaTypeEntry("invalid", "eez"));		
		AreaDetailsSpatialResponse response = areaDetailsService.getAreaDetails(request);
		ErrorsType errorType = response.getResponseMessage().getErrors();
		assertEquals(errorType.getErrorMessages().get(0).getErrorCode(), "5012");
	}
	

	/**
	 * Test for Invalid entity in the DB
	 */
	@Test
	public void getAreaDetailsInvalidEntityTest() {
		AreaDetailsSpatialRequest request = new AreaDetailsSpatialRequest(new AreaTypeEntry("1", "Invalid"));		
		AreaDetailsSpatialResponse response = areaDetailsService.getAreaDetails(request);
		ErrorsType errorType = response.getResponseMessage().getErrors();
		assertEquals(errorType.getErrorMessages().get(0).getErrorCode(), "5009");
	}
	
	/**
	 * Test for non existing row in DB
	 */
	@Test
	public void GetAreaDetailsInvalidRowTest() {
		AreaDetailsSpatialRequest request = new AreaDetailsSpatialRequest(new AreaTypeEntry("10000000", "eez"));		
		AreaDetailsSpatialResponse response = areaDetailsService.getAreaDetails(request);
		ErrorsType errorType = response.getResponseMessage().getErrors();
		assertNotNull(errorType.getErrorMessages().get(0).getErrorCode());
		assertEquals(errorType.getErrorMessages().get(0).getErrorCode(), "5010");
	}
	
}
