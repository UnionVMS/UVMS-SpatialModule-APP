package eu.europa.ec.fisheries.uvms.spatial;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by mkopyczok on 07-07-15.
 */
public class ModuleInitializerBeanTest {

    public static final String LOCALHOST = "localhost:8080";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String APPLICATION_XML = "application/xml";
    public static final String CONTENT_TYPE = "Content-Type";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private ModuleInitializerBean initializerBean;

    @Before
    public void setUp() throws Exception {
        initializerBean = new ModuleInitializerBean();
    }

    @Test
    public void shouldDeployDescriptorOnStartup() throws Exception {
        // given
        stubFor(post(urlEqualTo("/usm-administration/rest/deployments/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, "text/xml")
                        .withBody("<response>OK</response>")));

        // when
        initializerBean.startup();

        // then
        verify(1, getRequestedFor(urlEqualTo("/usm-administration/rest/deployments/spatialModule"))
                        .withHeader("Accept", matching("application/xml"))
                        .withHeader("Host", matching(LOCALHOST))
                        .withHeader("Connection", matching(KEEP_ALIVE))
                        .withoutHeader(CONTENT_TYPE)
        );

        verify(1, postRequestedFor(urlEqualTo("/usm-administration/rest/deployments/"))
                        .withHeader("Accept", matching(APPLICATION_XML))
                        .withHeader("Host", matching(LOCALHOST))
                        .withHeader("Connection", matching(KEEP_ALIVE))
                        .withRequestBody(matching(".*<name>Spatial Module</name>.*"))
        );

    }
}