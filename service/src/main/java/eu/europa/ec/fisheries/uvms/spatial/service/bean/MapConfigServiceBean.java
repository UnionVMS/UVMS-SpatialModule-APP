package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ControlDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.PositionsDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.SegmentDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.TbControlDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.VectorStylesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.ProjectionMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.geotools.util.UnsupportedImplementationException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Stateless
@Local(MapConfigService.class)
@Transactional
@Slf4j
public class MapConfigServiceBean implements MapConfigService {

    @EJB
    private SpatialRepository repository;

    private ProjectionMapper projectionMapper = ProjectionMapper.mapper();

    @Override
    @SneakyThrows
    public List<ProjectionDto> getAllProjections() {
        List<ProjectionEntity> projections = repository.findAllEntity(ProjectionEntity.class);
        return Lists.transform(projections, new Function<ProjectionEntity, ProjectionDto>() {
            @Override
            public ProjectionDto apply(ProjectionEntity projection) {
                return projectionMapper.projectionEntityToProjectionDto(projection);
            }
        });
    }

    public MapConfigDto getReportConfig(int reportId) {
        MapDto map = new MapDto(getMapProjection(reportId), createMockControls(), createMockTbControls(), getServiceAreaLayer(reportId));
        return new MapConfigDto(map, createMockVectorStyle());
    }

    private ProjectionDto getMapProjection(int reportId) {
        List<ProjectionDto> projectionDtoList = repository.findProjectionByMap(reportId);
        return (projectionDtoList != null && !projectionDtoList.isEmpty()) ? projectionDtoList.get(0) : null;
    }

    @Override
    @SneakyThrows
    public SpatialSaveMapConfigurationRS saveMapConfiguration(final SpatialSaveMapConfigurationRQ request) {

        MapConfigurationType mapConfiguration = request.getMapConfiguration();

        MapConfigurationType mapConfigurationType = repository.saveMapConfiguration(mapConfiguration);

        SpatialSaveMapConfigurationRS response = new SpatialSaveMapConfigurationRS();

        response.setResponse("OK");

        throw new org.apache.commons.lang.NotImplementedException("not completed");
        //return response;

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

    private ArrayList<TbControlDto> createMockTbControls() {
        ArrayList<TbControlDto> controls = Lists.newArrayList();
        controls.add(new TbControlDto("measure"));
        controls.add(new TbControlDto("fullscreen"));
        controls.add(new TbControlDto("print"));
        return controls;
    }

    private ArrayList<ControlDto> createMockControls() {
            ArrayList<ControlDto> controlDtos = Lists.newArrayList();
            controlDtos.add(new ControlDto("zoom"));
            controlDtos.add(new ControlDto("drag"));
            controlDtos.add(new ControlDto("scale", "nautical", null, null));
            controlDtos.add(new ControlDto("mousecoords", null, 4326, "dd"));
            controlDtos.add(new ControlDto("history"));
            controlDtos.add(new ControlDto("measure"));
            return controlDtos;
        }

    private VectorStylesDto createMockVectorStyle() {
        PositionsDto positionsDto = new PositionsDto();
        positionsDto.setAttribute("fs");
        positionsDto.setStyle(Arrays.asList((Map<String, String>)
                        ImmutableMap.<String, String>builder().put("dnk", "#0066FF").build(),
                ImmutableMap.<String, String>builder().put("swe", "#FF0066").build()));

        SegmentDto segmentDto = new SegmentDto();
        segmentDto.setAttribute("speed");
        segmentDto.setStyle(Arrays.asList((Map<String, String>)
                        ImmutableMap.<String, String>builder().put("color", "#1a9641").put("speed", "0-24").build(),
                        ImmutableMap.<String, String>builder().put("color", "#a6d96a").put("speed", "25-49").build(),
                        ImmutableMap.<String, String>builder().put("color", "#fdae61").put("speed", "50-74").build(),
                        ImmutableMap.<String, String>builder().put("color", "#d7191c").put("speed", "75-100").build()));

        return new VectorStylesDto(positionsDto, segmentDto);
    }
}
