package eu.europa.ec.fisheries.uvms.spatial.service.bean.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.FileUploadBean;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.TestZipFile;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.TransactionalTests;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMapping;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMetadata;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaUpdateEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.Property;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class FileUploadBeanTest extends TransactionalTests {

    @Inject
    FileUploadBean fileUploadBean;

    @Test
    public void saveFileToDbTest(){

        AreaUpdateEntity createdEntity = createAreaUpdateEntity();

        assertEquals("EEZ", createdEntity.getAreaType());
        assertEquals("joemat", createdEntity.getUploader());
        assertNotNull(createdEntity.getUploadDate());
    }

    @Test
    public void getAreaEntityEEZMetadataTest(){
        List<AreaUploadProperty> areaMetadata = fileUploadBean.getAreaEntityMetadata("EEZ");
        assertEquals(12, areaMetadata.size());
        assertTrue(areaMetadata.stream().anyMatch(property -> "country".equals(property.getName())));
        assertTrue(areaMetadata.stream().anyMatch(property -> "mrGid".equals(property.getName())));
        assertTrue(areaMetadata.stream().anyMatch(property -> "eezId".equals(property.getName())));
    }

    @Test
    public void getAreaEntityFAOMetadataTest(){
        List<AreaUploadProperty> areaMetadata = fileUploadBean.getAreaEntityMetadata("FAO");
        assertEquals(21, areaMetadata.size());
        assertTrue(areaMetadata.stream().anyMatch(property -> "ocean".equals(property.getName())));
        assertTrue(areaMetadata.stream().anyMatch(property -> "fLabel".equals(property.getName())));
        assertTrue(areaMetadata.stream().anyMatch(property -> "fArea".equals(property.getName())));
    }

    @Test
    public void testGetShapeFileMetadata() throws IOException {
        AreaUpdateEntity createdEntity = createAreaUpdateEntity();

        List<AreaUploadProperty> properties = fileUploadBean.getShapeFileMetadata(createdEntity);
        assertEquals(15, properties.size());
        assertTrue(properties.stream().anyMatch(property -> "OMR_ID".equals(property.getName())));
        assertTrue(properties.stream().anyMatch(property -> "OMR_NAMN".equals(property.getName())));
        assertTrue(properties.stream().anyMatch(property -> "OMR_UPPANV".equals(property.getName())));
        assertTrue(properties.stream().anyMatch(property -> "OMR_GEOM_B".equals(property.getName())));
    }

    @Test
    public void getShapefileAndAreaEntityMetadataTest() {
        AreaUpdateEntity createdEntity = createAreaUpdateEntity();

        AreaUploadMetadata response = fileUploadBean.getShapeFileAndAreaMetadata(createdEntity);
        assertEquals(12, response.getDomain().size());
        assertEquals(15, response.getFile().size());
        assertEquals(1, response.getAdditionalProperties().keySet().size());
        assertTrue(response.getAdditionalProperties().keySet().contains("ref"));
        assertEquals(createdEntity.getId(), (long)response.getAdditionalProperties().get("ref"));
    }

    @Test
    public void readShapeFileTest() throws Exception {
        AreaUpdateEntity updateEntity = createAreaUpdateEntity();

        Map<String, List<Property>> propertyMap = fileUploadBean.readShapeFile(updateEntity, 4326);
        //System.out.println(propertyMap);
        assertEquals(20, propertyMap.size());
        for (String key : propertyMap.keySet()) {
            List<Property> entry = propertyMap.get(key);
            assertEquals(16, entry.size());
            entry.stream().anyMatch(p -> p.getValue().toString().startsWith("Bratten"));
            assertTrue(entry.get(0).getName().toString().equals("the_geom"));
            Geometry geo = (Geometry) entry.get(0).getValue();
            assertEquals(4326, geo.getSRID());
        }
    }

    @Test
    public void upsertReferenceData() throws Exception{
        AreaUpdateEntity createdEntity = createAreaUpdateEntity();

        AreaUploadMetadata response = fileUploadBean.getShapeFileAndAreaMetadata(createdEntity);

        AreaUploadMapping mapping = new AreaUploadMapping();
        mapping.setAdditionalProperty("ref",response.getAdditionalProperties().get("ref"));

        //fileUploadBean.upsertReferenceData(mapping, 4326);

    }

    private AreaUpdateEntity createAreaUpdateEntity(){
        String base64EncodedZipfile = TestZipFile.omradeZip;
        byte[] bytes = Base64.getDecoder().decode(base64EncodedZipfile);

        AreaUpdateEntity createdEntity = fileUploadBean.saveUploadedAreaDefinitionsToDB(bytes, "EEZ", "joemat");
        assertNotNull(createdEntity.getId());
        assertEquals(bytes.length, createdEntity.getUploadedFile().length);

        return createdEntity;
    }
}
