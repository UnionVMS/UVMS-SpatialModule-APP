package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.dao.AreaDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.AreaLocationTypesDao;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMetadata;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.AreaUploadMapping;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaUpdateEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.EntityUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Stateless
public class FileUploadBean {

    @Inject
    AreaDao areaDao;

    @Inject
    AreaLocationTypesDao areaLocationTypesDao;


    /*------------------- Metadata ----------------------*/

    public AreaUpdateEntity saveUploadedAreaDefinitionsToDB(byte[] bytes, String type, String username){
        AreaUpdateEntity updateEntity = new AreaUpdateEntity();
        updateEntity.setAreaType(type);
        updateEntity.setUploadDate(Instant.now());
        updateEntity.setUploader(username);
        updateEntity.setUploadedFile(bytes);

        updateEntity = areaDao.create(updateEntity);

        return updateEntity;
    }

    public AreaUploadMetadata getShapeFileAndAreaMetadata(AreaUpdateEntity areaUpdate){
        AreaUploadMetadata response = new AreaUploadMetadata();
        byte[] bytes = areaUpdate.getUploadedFile();
        try {
            if (bytes.length == 0) {
                throw new IllegalArgumentException("File is empty.");
            }

            response.getDomain().addAll(getAreaEntityMetadata(areaUpdate.getAreaType()));

            response.getFile().addAll(getShapeFileMetadata(areaUpdate));
            response.withAdditionalProperty("ref", areaUpdate.getId());

        } catch (Exception ex) {
            throw new RuntimeException("Invalid upload area data.", ex);
        }
        return response;
    }

    public List<AreaUploadProperty> getAreaEntityMetadata(String typeName){
        AreaLocationTypesEntity typeEntity = areaLocationTypesDao.findOneByTypeName(typeName);

        if (typeEntity == null) {
            throw new IllegalArgumentException("Area type " + typeName + " not supported");
        }

        BaseAreaEntity instance = EntityUtils.getInstance(typeEntity.getTypeName());
        List<Field> properties = EntityUtils.listMembers(instance);

        List<AreaUploadProperty> domain = new ArrayList<>();
        for (Field field : properties) {
            AreaUploadProperty uploadProperty = new AreaUploadProperty();
            uploadProperty.withName(field.getName()).withType(field.getType().getSimpleName());
            domain.add(uploadProperty);
        }

        return domain;
    }

    public List<AreaUploadProperty> getShapeFileMetadata(AreaUpdateEntity areaUpdate) throws IOException {
        File zip = saveZipfileToDisk(areaUpdate.getUploadedFile(), areaUpdate.getAreaType());

        String shapefileName = getShapeFileName(zip);
        URL shape = new URL("jar:"+zip.toURI().toURL()+"!/" + shapefileName);
        Map<String, URL> params = new HashMap<>();
        params.put("url", shape);
        DataStore ds = DataStoreFinder.getDataStore(params );
        String name = ds.getTypeNames()[0];
        SimpleFeatureSource source = ds.getFeatureSource(name);

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(Filter.INCLUDE);
        FeatureIterator<SimpleFeature> iterator = collection.features();
        SimpleFeature next = iterator.next();
        List<AreaUploadProperty> properties = new ArrayList<>();
        try {

            List<AttributeDescriptor> attributeDescriptors = next.getFeatureType().getAttributeDescriptors();
            for (AttributeDescriptor attributeDescriptor : attributeDescriptors){
                String localPart = attributeDescriptor.getName().getLocalPart();
                switch (localPart){
                    case "the_geom":                //no clue why we ignore these
                    case "geom":
                        break;
                    default:
                        properties.add(new AreaUploadProperty().withName(localPart).withType(attributeDescriptor.getType().getBinding().getSimpleName())); // TODO nullpointer checks
                }
            }

        } finally {
            iterator.close();
            ds.dispose();
        }

        deleteTempZipFile(zip);

        return properties;

    }

    private File saveZipfileToDisk(byte[] bytes, String type) throws IOException {
        File zip = File.createTempFile(type,".zip");
        try (FileOutputStream stream = new FileOutputStream(zip)) {
            stream.write(bytes);
        }

        return zip;
    }

    private boolean deleteTempZipFile(File file){
        return file.delete();
    }

    private String getShapeFileName(File f) throws IOException {
        ZipFile zipfile = new ZipFile(f);
        final Enumeration<? extends ZipEntry> entries = zipfile.entries();
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(entry.getName().endsWith(".shp")){
                return entry.getName();
            }
        }
        throw new IllegalArgumentException("Uploaded zip-file lacks a .shp file");
    }

    /*------------------- Real deal ----------------------*/

    public void upsertReferenceData(final AreaUploadMapping mapping, final Integer epsg){
        long ref = (long) mapping.getAdditionalProperties().get("ref");
        AreaUpdateEntity updateEntity = areaDao.find(AreaUpdateEntity.class, ref);
        if(updateEntity == null){
            throw new IllegalArgumentException("Reference for uploaded data is missing/invalid");
        }

        AreaLocationTypesEntity typeEntity = areaLocationTypesDao.findOneByTypeName(updateEntity.getAreaType());
        BaseAreaEntity instance = EntityUtils.getInstance(typeEntity.getTypeName());

        areaDao

    }
}
