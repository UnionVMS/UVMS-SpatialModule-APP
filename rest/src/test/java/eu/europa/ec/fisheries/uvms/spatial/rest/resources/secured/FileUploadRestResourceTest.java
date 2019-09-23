package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured;

import eu.europa.ec.fisheries.uvms.spatial.rest.BuildSpatialRestDeployment;
import eu.europa.ec.fisheries.uvms.spatial.rest.TestZipFile;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.FileUploadForm;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMetadata;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Base64;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class FileUploadRestResourceTest extends BuildSpatialRestDeployment {

    @Test
    public void metadataTest(){
        String base64EncodedZipfile = TestZipFile.omradeZip;
        byte[] bytes = Base64.getDecoder().decode(base64EncodedZipfile);

        FileUploadForm uploadForm = new FileUploadForm();
        uploadForm.setAreaType("EEZ");
        uploadForm.setData(bytes);

        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        mdo.addFormData("areaType", "EEZ", MediaType.TEXT_PLAIN );
        mdo.addFormData("uploadedFile", bytes, MediaType.APPLICATION_OCTET_STREAM );

        AreaUploadMetadata response = getSecuredWebTarget()
                .path("files")
                .path("metadata")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.entity(uploadForm, MediaType.MULTIPART_FORM_DATA), AreaUploadMetadata.class);

        assertThat(response, is(notNullValue()));
        assertEquals(12, response.getDomain().size());
        assertEquals(15, response.getFile().size());
        assertEquals(1, response.getAdditionalProperties().keySet().size());
        assertTrue(response.getAdditionalProperties().keySet().contains("ref"));
    }

}
