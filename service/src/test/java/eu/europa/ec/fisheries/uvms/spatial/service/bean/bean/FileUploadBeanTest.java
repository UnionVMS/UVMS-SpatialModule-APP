package eu.europa.ec.fisheries.uvms.spatial.service.bean.bean;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.FileUploadBean;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.TestZipFile;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.TransactionalTests;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.AreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMapping;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMetadata;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaUpdateEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.StatRectEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.Property;

import javax.ejb.EJBTransactionRolledbackException;
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

        AreaUpdateEntity createdEntity = createAreaUpdateEntity("EEZ");

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
        AreaUpdateEntity createdEntity = createAreaUpdateEntity("EEZ");

        List<AreaUploadProperty> properties = fileUploadBean.getShapeFileMetadata(createdEntity);
        assertEquals(15, properties.size());
        assertTrue(properties.stream().anyMatch(property -> "OMR_ID".equals(property.getName())));
        assertTrue(properties.stream().anyMatch(property -> "OMR_NAMN".equals(property.getName())));
        assertTrue(properties.stream().anyMatch(property -> "OMR_UPPANV".equals(property.getName())));
        assertTrue(properties.stream().anyMatch(property -> "OMR_GEOM_B".equals(property.getName())));
    }

    @Test
    public void getShapefileAndAreaEntityMetadataTest() {
        AreaUpdateEntity createdEntity = createAreaUpdateEntity("EEZ");

        AreaUploadMetadata response = fileUploadBean.getShapeFileAndAreaMetadata(createdEntity);
        assertEquals(12, response.getDomain().size());
        assertEquals(15, response.getFile().size());
        assertEquals(1, response.getAdditionalProperties().keySet().size());
        assertTrue(response.getAdditionalProperties().keySet().contains("ref"));
        assertEquals(createdEntity.getId(), (long)response.getAdditionalProperties().get("ref"));
    }

    @Test(expected = EJBTransactionRolledbackException.class)
    public void upsertReferenceDataEmptyInputMappingTest() throws IOException {
        AreaUploadMapping mapping = new AreaUploadMapping();
        fileUploadBean.upsertReferenceData(mapping, 4326);
    }


    @Test(expected = EJBTransactionRolledbackException.class)
    public void upsertReferenceDataNonexistantRefMappingTest() throws IOException {
        AreaUploadMapping mapping = new AreaUploadMapping();
        mapping.getAdditionalProperties().put("ref", -1);
        fileUploadBean.upsertReferenceData(mapping, 4326);
    }

    @Inject
    AreaDao areaDao;

    @Test
    public void upsertReferenceDataMinimalAmountOfMappingTest() throws IOException {
        AreaUpdateEntity createdEntity = createAreaUpdateEntity("EEZ");

        AreaUploadMetadata response = fileUploadBean.getShapeFileAndAreaMetadata(createdEntity);
        AreaUploadMapping mapping = new AreaUploadMapping();
        mapping.setAdditionalProperty("ref", response.getAdditionalProperties().get("ref"));

        mapping.getMapping().add(createAreaUploadMappingProperty("name", "OMR_NAMN"));
        mapping.getMapping().add(createAreaUploadMappingProperty("code", "OMR_ID"));


        fileUploadBean.upsertReferenceData(mapping, 4326);

        assertTrue(createdEntity.isProcessCompleted());
        List<EezEntity> eezAreas = areaDao.getAllEezAreas();
        assertEquals(20, eezAreas.size());
        assertTrue(eezAreas.stream().allMatch(eez -> eez.getName().startsWith("Bratten")));
        assertTrue(eezAreas.stream().allMatch(eez -> eez.getEnabled()));

    }

    @Test
    public void upsertReferenceDataLargerAmountOfMappingOnStatRectTest() throws IOException {
        AreaUpdateEntity createdEntity = createAreaUpdateEntity("StatRect");

        AreaUploadMetadata response = fileUploadBean.getShapeFileAndAreaMetadata(createdEntity);
        AreaUploadMapping mapping = new AreaUploadMapping();
        mapping.setAdditionalProperty("ref", response.getAdditionalProperties().get("ref"));

        mapping.getMapping().add(createAreaUploadMappingProperty("name", "OMR_NAMN"));
        mapping.getMapping().add(createAreaUploadMappingProperty("code", "OMR_ID"));
        mapping.getMapping().add(createAreaUploadMappingProperty("north", "OMR_MAXNOR"));
        mapping.getMapping().add(createAreaUploadMappingProperty("south", "OMR_MINNOR"));
        mapping.getMapping().add(createAreaUploadMappingProperty("west", "OMR_MINEAS"));
        mapping.getMapping().add(createAreaUploadMappingProperty("east", "OMR_MAXEAS"));

        fileUploadBean.upsertReferenceData(mapping, 4326);

        assertTrue(createdEntity.isProcessCompleted());
        List<StatRectEntity> stat_rectAreas = areaDao.getAllStatRectAreas();
        assertEquals(20, stat_rectAreas.size());
        assertTrue(stat_rectAreas.stream().allMatch(stat -> stat.getName().startsWith("Bratten")));
        assertTrue(stat_rectAreas.stream().allMatch(stat -> stat.getEnabled()));
        assertTrue(stat_rectAreas.stream().allMatch(stat -> stat.getNorth() != null));
        assertTrue(stat_rectAreas.stream().allMatch(stat -> stat.getSouth() != null));
        assertTrue(stat_rectAreas.stream().allMatch(stat -> stat.getEast() != null));
        assertTrue(stat_rectAreas.stream().allMatch(stat -> stat.getWest() != null));

    }


    @Test
    public void upsertReferenceDataLargerAmountOfMappingOnUserAreaTest() throws IOException {
        AreaUpdateEntity createdEntity = createAreaUpdateEntity("userArea");

        AreaUploadMetadata response = fileUploadBean.getShapeFileAndAreaMetadata(createdEntity);
        AreaUploadMapping mapping = new AreaUploadMapping();
        mapping.setAdditionalProperty("ref", response.getAdditionalProperties().get("ref"));

        mapping.getMapping().add(createAreaUploadMappingProperty("name", "OMR_NAMN"));
        mapping.getMapping().add(createAreaUploadMappingProperty("code", "OMR_ID"));
        mapping.getMapping().add(createAreaUploadMappingProperty("userName", "OMR_UPPANV"));
        mapping.getMapping().add(createAreaUploadMappingProperty("createdOn", "OMR_UPPDAT"));
        mapping.getMapping().add(createAreaUploadMappingProperty("areaDesc", "OMR_BESKRI"));


        fileUploadBean.upsertReferenceData(mapping, 4326);

        assertTrue(createdEntity.isProcessCompleted());
        List<UserAreasEntity> userAreas = areaDao.getAllUserAreas("STITHO", "");
        assertEquals(20, userAreas.size());
        assertTrue(userAreas.stream().allMatch(user -> user.getName().startsWith("Bratten")));
        assertTrue(userAreas.stream().allMatch(user -> user.getEnabled()));
        assertTrue(userAreas.stream().allMatch(user -> user.getCreatedOn() != null));
        assertTrue(userAreas.stream().allMatch(user -> user.getAreaDesc() != null));
        assertTrue(userAreas.stream().allMatch(user -> user.getUserName() != null));

    }


        @Test
    public void readShapeFileTest() throws Exception {
        AreaUpdateEntity updateEntity = createAreaUpdateEntity("EEZ");

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

    private AreaUpdateEntity createAreaUpdateEntity(String type){
        String base64EncodedZipfile = TestZipFile.omradeZip;
        byte[] bytes = Base64.getDecoder().decode(base64EncodedZipfile);

        AreaUpdateEntity createdEntity = fileUploadBean.saveUploadedAreaDefinitionsToDB(bytes, type, "joemat");
        assertNotNull(createdEntity.getId());
        assertEquals(bytes.length, createdEntity.getUploadedFile().length);

        return createdEntity;
    }

    private AreaUploadMappingProperty createAreaUploadMappingProperty(String source, String target){
        AreaUploadMappingProperty property = new AreaUploadMappingProperty(source, target);
        return property;
    }
}
