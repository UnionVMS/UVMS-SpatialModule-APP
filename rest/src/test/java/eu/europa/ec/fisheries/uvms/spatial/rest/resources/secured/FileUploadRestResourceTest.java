package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;
import eu.europa.ec.fisheries.uvms.spatial.rest.TestZipFile;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.FileUploadForm;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMapping;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMetadata;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class FileUploadRestResourceTest extends BuildSpatialRestDeployment {

    @Test
    public void metadataTest() throws IOException {
        String base64EncodedZipfile = TestZipFile.omradeZip;
        byte[] bytes = Base64.getDecoder().decode(base64EncodedZipfile);

        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        mdo.addFormData("areaType", "EEZ", MediaType.TEXT_PLAIN_TYPE );
        mdo.addFormData("uploadedFile", bytes, MediaType.APPLICATION_OCTET_STREAM_TYPE );

        String stringResponse = getSecuredWebTarget()
                .path("files")
                .path("metadata")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);

        ObjectMapper om = new ObjectMapper();
        AreaUploadMetadata response = om.readValue(stringResponse, AreaUploadMetadata.class);       //for some reason the client always uses jsonb despite us saying that it should use jackson, and we use a jackson-specific annotation.

        assertThat(response, is(notNullValue()));
        assertEquals(11, response.getDomain().size());
        assertEquals(15, response.getFile().size());
        assertEquals(1, response.getAdditionalProperties().keySet().size());
        assertTrue(response.getAdditionalProperties().keySet().contains("ref"));
    }

    @Test
    public void uploadZipFileWithAreaDataTest() throws IOException {
        String base64EncodedZipfile = TestZipFile.omradeZip;
        byte[] bytes = Base64.getDecoder().decode(base64EncodedZipfile);

        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        mdo.addFormData("areaType", "userArea", MediaType.TEXT_PLAIN_TYPE );
        mdo.addFormData("uploadedFile", bytes, MediaType.APPLICATION_OCTET_STREAM_TYPE );

        String stringResponse = getSecuredWebTarget()
                .path("files")
                .path("metadata")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);

        ObjectMapper om = new ObjectMapper();
        AreaUploadMetadata response = om.readValue(stringResponse, AreaUploadMetadata.class);       //for some reason the client always uses jsonb despite us saying that it should use jackson, and we use a jackson-specific annotation.

        AreaUploadMapping mapping = new AreaUploadMapping();
        mapping.setAdditionalProperty("ref", response.getAdditionalProperties().get("ref"));

        mapping.getMapping().add(createAreaUploadMappingProperty("name", "OMR_NAMN"));
        mapping.getMapping().add(createAreaUploadMappingProperty("code", "OMR_ID"));
        mapping.getMapping().add(createAreaUploadMappingProperty("userName", "OMR_UPPANV"));
        mapping.getMapping().add(createAreaUploadMappingProperty("createdOn", "OMR_UPPDAT"));
        mapping.getMapping().add(createAreaUploadMappingProperty("areaDesc", "OMR_BESKRI"));

        String json = om.writeValueAsString(mapping);

        Response uploadResponse = getSecuredWebTarget()
                .path("files")
                .path("upload")
                .path("4326")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.json(json) , Response.class);

        assertNotNull(uploadResponse);
        assertEquals(200, uploadResponse.getStatus());

        List<UserAreasEntity> areaResponse = getSecuredWebTarget()
                .path("userarea")
                .path("list")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .header("scopeName", null)
                .get( new GenericType<List<UserAreasEntity>>() {});

        assertFalse(areaResponse.isEmpty());
        assertTrue(areaResponse.size() >= 20);
    }


    private AreaUploadMappingProperty createAreaUploadMappingProperty(String source, String target){
        AreaUploadMappingProperty property = new AreaUploadMappingProperty(source, target);
        return property;
    }

}
