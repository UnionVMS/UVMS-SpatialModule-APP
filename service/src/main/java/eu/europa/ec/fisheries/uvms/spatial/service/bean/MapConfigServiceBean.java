package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.mapper.ReportConnectSpatialMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ControlDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.TbControlDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.VectorStylesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.MapConfigMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ProjectionMapper;
import eu.europa.ec.fisheries.uvms.spatial.validator.SpatialValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.spatial.service.mapper.ConfigurationMapper.mergeConfiguration;

@Stateless
@Local(MapConfigService.class)
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    private static final String SCALE = "scale";
    private static final String MOUSECOORDS = "mousecoords";
    @EJB
    private SpatialRepository repository;

    @Inject
    private MapConfigMapper mapConfigMapper;

    @Override
    @SneakyThrows
    public List<ProjectionDto> getAllProjections() {

        List<ProjectionEntity> projections = repository.findAllEntity(ProjectionEntity.class); // TODO projectionDAO

        return ProjectionMapper.INSTANCE.projectionEntityListToProjectionDtoList(projections);

    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void handleDeleteMapConfiguration(SpatialDeleteMapConfigurationRQ request) throws ServiceException {

        SpatialValidator.validate(request);

        repository.deleteBy(request.getSpatialConnectIds());
    }

    @Override
    public MapConfigurationType getMapConfigurationType(final Long reportId) throws ServiceException {

        SpatialValidator.validate(reportId);

        ReportConnectSpatialEntity entity = repository.findReportConnectSpatialBy(reportId);

        return ReportConnectSpatialMapper.INSTANCE.reportConnectSpatialEntityToMapConfigurationType(entity);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialGetMapConfigurationRS getMapConfiguration(SpatialGetMapConfigurationRQ mapConfigurationRQ) throws ServiceException {

        long reportId = mapConfigurationRQ.getReportId();

        return new SpatialGetMapConfigurationRS(getMapConfigurationType(reportId));

    }

    @Override
    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialSaveOrUpdateMapConfigurationRS handleSpatialMapConfiguration(final SpatialSaveOrUpdateMapConfigurationRQ request) {

        SpatialValidator.validate(request);

        ReportConnectSpatialEntity entity =
                ReportConnectSpatialMapper.INSTANCE.mapConfigurationTypeToReportConnectSpatialEntity(request.getMapConfiguration());

        repository.saveOrUpdateMapConfiguration(entity);

        return new SpatialSaveOrUpdateMapConfigurationRS();

    }


    @Override
    @SneakyThrows
    public MapConfigDto getReportConfig(int reportId) {
        ConfigurationDto configurationDto = getMergedMapConfig();
        return new MapConfigDto(getMap(configurationDto, reportId), getVectorStyles(configurationDto));
    }

    private ConfigurationDto getMergedMapConfig() throws IOException {
        return mergeConfiguration(getUserConfiguration(), getAdminConfiguration()); //Returns merged config object between Admin and User configuration from USM
    }

    private MapDto getMap(ConfigurationDto configurationDto, int reportId) {
        return new MapDto(getMapProjection(reportId, configurationDto), getControls(reportId, configurationDto), getTbControls(configurationDto), getServiceAreaLayer(reportId));
    }

    private ProjectionDto getMapProjection(int reportId, ConfigurationDto configurationDto) {
        List<ProjectionDto> projectionDtoList = repository.findProjectionByMap(reportId);
        if (projectionDtoList != null && !projectionDtoList.isEmpty()) {
            return projectionDtoList.get(0);
        } else {
            String mapSrsCode = configurationDto.getMapSettings().getMapProjection();
            projectionDtoList = repository.findProjectionBySrsCode(Integer.parseInt(mapSrsCode));
            return (projectionDtoList != null && !projectionDtoList.isEmpty()) ? projectionDtoList.get(0) : null;
        }
    }

    private List<LayerDto> getServiceAreaLayer(int reportId) {
        List<LayerDto> layerDtos = new ArrayList<LayerDto>();
        for (ReportConnectServiceAreasEntity reportConnectServiceArea : getReportConnectServiceAreas(reportId)) {
            layerDtos.add(reportConnectServiceArea.convertToServiceLayer());
        }
        return !layerDtos.isEmpty() ? layerDtos : null;
    }

    private List<ReportConnectServiceAreasEntity> getReportConnectServiceAreas(int reportId) {
        List<ReportConnectServiceAreasEntity> reportConnectServiceAreas = repository.findReportConnectServiceAreas(reportId);
        Collections.sort(reportConnectServiceAreas);
        return reportConnectServiceAreas;
    }

    private VectorStylesDto getVectorStyles(ConfigurationDto configurationDto) {
        return mapConfigMapper.getStyleDtos(configurationDto.getStylesSettings());
    }

    private List<TbControlDto> getTbControls(ConfigurationDto configurationDto) {
        return mapConfigMapper.getTbControls(configurationDto.getToolSettings().getTbControl());
    }

    private List<ControlDto> getControls(int reportId, ConfigurationDto configurationDto) {
        List<ControlDto> controls = mapConfigMapper.getControls(configurationDto.getToolSettings().getControl());
        ProjectionDto displayProjection = getDisplayProjection(reportId);
        if (displayProjection != null) {
            for (ControlDto controlDto : controls) {
                if (controlDto.getType().equalsIgnoreCase(SCALE)) {
                    controlDto.setUnits(displayProjection.getUnits());
                }
                if (controlDto.getType().equalsIgnoreCase(MOUSECOORDS)) {
                    controlDto.setEpsgCode(displayProjection.getEpsgCode());
                    controlDto.setFormat(displayProjection.getFormats());
                }
            }
        } else {
            for (ControlDto controlDto : controls) {
                if (controlDto.getType().equalsIgnoreCase(SCALE)) {
                    controlDto.setUnits(configurationDto.getMapSettings().getScaleBarUnits());
                }
                if (controlDto.getType().equalsIgnoreCase(MOUSECOORDS)) {
                    controlDto.setEpsgCode(Integer.parseInt(configurationDto.getMapSettings().getDisplayProjection()));
                    controlDto.setFormat(configurationDto.getMapSettings().getCoordinatesFormat());
                }
            }
        }
        return controls;
    }

    private ProjectionDto getDisplayProjection(int reportId) {
        List<ProjectionDto> projectionDtoList = repository.findProjectionByMap(reportId);
        if (projectionDtoList != null && !projectionDtoList.isEmpty()) {
            return projectionDtoList.get(0);
        }
        return null;
    }

    private ConfigurationDto getAdminConfiguration() throws IOException {
        // TODO call USM
        InputStream is = new FileInputStream("src/test/resources/Config.json");
        String jsonTxt = IOUtils.toString(is);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(jsonTxt, ConfigurationDto.class);
    }

    private ConfigurationDto getUserConfiguration() throws IOException {
        //TODO call USM
        InputStream is = new FileInputStream("src/test/resources/UserConfig.json");
        String jsonTxt = IOUtils.toString(is);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(jsonTxt, ConfigurationDto.class);
    }
}
