/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.common.ZipExtractor;
import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.interceptors.SimpleTracingInterceptor;
import eu.europa.ec.fisheries.uvms.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DAOFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialectFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.EntityFactory;
import eu.europa.ec.fisheries.uvms.spatial.message.service.UploadConsumerBean;
import eu.europa.ec.fisheries.uvms.spatial.message.service.UploadProducerBean;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMapping;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMetadata;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.SpatialTypeEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

@Stateless
@Local(AreaService.class)
@Slf4j
public class AreaServiceBean implements AreaService {

    private EntityManager em;

    @PersistenceContext(unitName = "spatialPUpostgres")
    private EntityManager postgres;

    @PersistenceContext(unitName = "spatialPUoracle")
    private EntityManager oracle;	

	
    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    private @EJB AreaTypeNamesService areaTypeService;
    private @EJB AreaService areaService;
    private @EJB SpatialRepository repository;
    private @EJB UploadProducerBean uploadSender;
    private @EJB UploadConsumerBean uploadReceiver;
    private @EJB PropertiesBean properties;
    private DatabaseDialect databaseDialect;

	
    public void initEntityManager() {
        String dbDialect = System.getProperty("db.dialect");
        if ("oracle".equalsIgnoreCase(dbDialect)) {
            em = oracle;
        } else {
            em = postgres;
        }
    }
		
	
	
    @PostConstruct
    public void init(){
		initEntityManager();
        databaseDialect = new DatabaseDialectFactory(properties).getInstance();
    }

    @Override
    public Map<String, String> getAllCountriesDesc() throws ServiceException {
        Map<String, String> stringStringMap = new HashMap<>();
        List<CountryEntity> entityByNamedQuery = repository.findAllCountries();
        for (CountryEntity country : entityByNamedQuery){
            stringStringMap.put(country.getCode(), country.getName());
        }
        return stringStringMap;
    }

    @Override
    @Transactional
    public List<Map<String, String>> getSelectedAreaColumns(final List<AreaTypeEntry> areaTypes) throws ServiceException {

        List<Map<String, String>> areaColumnsList = new ArrayList<>();
        Map<String, List<Long>> areaTypeGidMap = new HashMap<>();

        for(AreaTypeEntry areaTypeEntry : areaTypes) { // Loops through the list of gids, areaType and forms a map containing a list of Gids for each Area Type
            String gid = areaTypeEntry.getId();
            String areaType = areaTypeEntry.getAreaType().value();

            if (!StringUtils.isNumeric(gid)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, gid);
            }

            boolean isAdded = false;
            for (Map.Entry<String, List<Long>> entry : areaTypeGidMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(areaType)) {
                    entry.getValue().add(Long.parseLong(gid));
                    isAdded = true;
                }
            }
            if (!isAdded) {
                areaTypeGidMap.put(areaType, new ArrayList(Arrays.asList(Long.parseLong(gid))));
            }
        }

        for (Map.Entry<String, List<Long>> entry : areaTypeGidMap.entrySet()) { // FIXME looping and querying should be avoided
            String namedQuery = null;
            for (SpatialTypeEnum type : SpatialTypeEnum.values()) {
                if(type.getType().equalsIgnoreCase(entry.getKey())) {
                    namedQuery = type.getNamedQuery();
                }
            }
            if (namedQuery != null) {
                List<Map<String, String>> selectedAreaColumns = repository.findSelectedAreaColumns(namedQuery, entry.getValue());
                for (Map<String, String> columnMap : selectedAreaColumns) {
                    columnMap.put(AREA_TYPE, entry.getKey().toUpperCase());
                }
                areaColumnsList.addAll(selectedAreaColumns);
            }
        }
        return areaColumnsList;
    }


    @Override
    @Interceptors(SimpleTracingInterceptor.class)
    public void upload(final UploadMapping mapping, final String type, final Integer code) throws ServiceException {

        String ref = (String) mapping.getAdditionalProperties().get("ref");

        try {
            CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + code);
            AreaLocationTypesEntity typeName = repository.findAreaLocationTypeByTypeName(type.toUpperCase());

            if (typeName == null){
                throw new ServiceException("TYPE NOT SUPPORTED");
            }

            Map<String, List<Property>> features = GeometryUtils.readShapeFile(Paths.get(ref + File.separator + typeName.getAreaDbTable() + ".shp"), sourceCRS);
            DAOFactory.getAbstractSpatialDao(em, typeName.getTypeName()).bulkInsert(features, mapping.getMapping());
            org.apache.commons.io.FileUtils.deleteDirectory(Paths.get(ref).getParent().toFile());

            repository.makeGeomValid(typeName.getAreaDbTable(), databaseDialect);

        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Interceptors(SimpleTracingInterceptor.class)
    public UploadMetadata metadata(byte[] bytes, String type) {

        UploadMetadata response = new UploadMetadata();

        try {
            if (bytes.length == 0) {
                throw new IllegalArgumentException("File is empty.");
            }
            AreaLocationTypesEntity typeName = repository.findAreaLocationTypeByTypeName(type.toUpperCase());

            if (typeName == null){
                throw new ServiceException("TYPE NOT SUPPORTED");
            }

            BaseEntity instance = EntityFactory.getInstance(typeName.getTypeName());
            List<Field> properties = instance.listMembers();

            for(Field field : properties){
                UploadProperty uploadProperty = new UploadProperty();
                uploadProperty.withName(field.getName()).withType(field.getType().getSimpleName());
                response.getDomain().add(uploadProperty);
            }

            Path uploadPath = Paths.get("app/upload/spatial");
            uploadPath.toFile().mkdirs();
            Path absolutePath = Files.createTempDirectory(uploadPath, null, new FileAttribute[0]);
            ZipExtractor.unZipFile(bytes, absolutePath);
            ZipExtractor.renameFiles(absolutePath, typeName.getTypeName());
            List<UploadProperty> propertyList = GeometryUtils.readAttribute(absolutePath);
            response.getFile().addAll(propertyList);
            response.withAdditionalProperty("ref", absolutePath.toString());

        } catch (Exception ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_UPLOAD_AREA_DATA, ex);
        }
        return response;
    }

    @Override
    @Transactional
    public AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException {

        if (areaTypeEntry.getAreaType() == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, StringUtils.EMPTY);
        }

        if (!StringUtils.isNumeric(areaTypeEntry.getId())) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, areaTypeEntry.getId());
        }

        AreaLocationTypesEntity areaLocationTypesEntity =
                repository.findAreaLocationTypeByTypeName(areaTypeEntry.getAreaType().value().toUpperCase());

        Long id = Long.parseLong(areaTypeEntry.getId());
        BaseAreaEntity area = DAOFactory.getAbstractSpatialDao(em, areaLocationTypesEntity.getTypeName()).findOne(id);

        if (area == null) {
            throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, areaLocationTypesEntity.getTypeName());
        }

        Map<String, Object> properties = area.getFieldMap();

        List<AreaProperty> areaProperties = new ArrayList<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            AreaProperty areaProperty = new AreaProperty();
            areaProperty.setPropertyName(entry.getKey());
            areaProperty.setPropertyValue(entry.getValue());
            areaProperties.add(areaProperty);
        }
        if (!properties.isEmpty()) {
            AreaProperty areaProperty = new AreaProperty();
            areaProperty.setPropertyName(GID);
            areaProperty.setPropertyValue(String.valueOf(area.getId()));
            areaProperties.add(areaProperty);
        }

        AreaDetails areaDetails = new AreaDetails();
        areaDetails.setAreaType(areaTypeEntry);
        areaDetails.getAreaProperties().addAll(areaProperties);
        return areaDetails;
    }

    @Override
    @Transactional
    public List<AreaSimpleType> byCode(List<AreaSimpleType> areaSimpleTypeList) throws ServiceException {

        List records = repository.areaByCode(areaSimpleTypeList);
        List<AreaSimpleType> simpleTypeList = new ArrayList<>();

       for (Object record : records) {

            final Object[] result = (Object[]) record;
            String type = (String) result[0];
            String code = (String) result[1];
            Geometry geom = (Geometry) result[2];
            simpleTypeList.add(new AreaSimpleType(type, code, GeometryMapper.INSTANCE.geometryToWkt(geom).getValue()));
        }

        return simpleTypeList;
    }
}