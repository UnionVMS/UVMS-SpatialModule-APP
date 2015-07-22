package eu.europa.ec.fisheries.uvms.spatial;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.Integer.valueOf;

public class ModuleInitializerBeanTest {

    public static final int PORT = 8089;
    public static final String LOCALHOST = "localhost:" + valueOf(PORT);
    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final String APPLICATION_XML = "application/xml";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final int NOT_PRESENT = HttpServletResponse.SC_NO_CONTENT;
    public static final int PRESENT = HttpServletResponse.SC_OK;
    public static final int SUCCESS = HttpServletResponse.SC_OK;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);

    private ModuleInitializerBean initializerBean;

    @Before
    public void setUp() throws Exception {
        initializerBean = new ModuleInitializerBean();
    }

    @Test
    public void shouldDeployDescriptor() throws Exception {
        // given
        stubFor(get(urlEqualTo("/usm-administration/rest/deployments/spatialModule"))
                .willReturn(aResponse()
                        .withStatus(NOT_PRESENT)));

        stubFor(post(urlEqualTo("/usm-administration/rest/deployments/"))
                .willReturn(aResponse()
                        .withStatus(SUCCESS)
                        .withHeader(CONTENT_TYPE, APPLICATION_XML)
                        .withBody("<response>OK</response>")));

        // when
        initializerBean.startup();

        // then
        verify(1, getRequestedFor(urlEqualTo("/usm-administration/rest/deployments/spatialModule"))
                        .withHeader("Accept", matching(APPLICATION_XML))
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

    @Test
    public void shouldUpdateDescriptor() throws Exception {
        // given
        stubFor(get(urlEqualTo("/usm-administration/rest/deployments/spatialModule"))
                .willReturn(aResponse()
                        .withStatus(PRESENT)
                        .withHeader(CONTENT_TYPE, APPLICATION_XML)
                        .withBody("<response>OK</response>")));

        stubFor(put(urlEqualTo("/usm-administration/rest/deployments/"))
                .willReturn(aResponse()
                        .withStatus(SUCCESS)
                        .withHeader(CONTENT_TYPE, APPLICATION_XML)
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

        verify(1, putRequestedFor(urlEqualTo("/usm-administration/rest/deployments/"))
                        .withHeader("Accept", matching(APPLICATION_XML))
                        .withHeader("Content-Type", matching(APPLICATION_XML))
                        .withHeader("Host", matching(LOCALHOST))
                        .withHeader("Connection", matching(KEEP_ALIVE))
                        .withHeader("Content-Length", matching("866"))
                        .withRequestBody(matching(".*<name>Spatial Module</name>.*"))
        );

    }

}