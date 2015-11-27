package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialMessageServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetAreaByLocationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetAreaTypeNamesEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetClosestAreaEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetClosestLocationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetFilterAreaEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetSpatialEnrichmentEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.PingEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SaveMapConfigurationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageErrorEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageEvent;
import eu.europa.ec.fisheries.uvms.spatial.model.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PingRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRS;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

@Stateless
@Slf4j
public class SpatialEventServiceBean implements SpatialEventService {

    @Inject
    @SpatialMessageErrorEvent
    Event<SpatialMessageEvent> spatialErrorEvent;

    @EJB
    private AreaByLocationService areaByLocationService;

    @EJB
    private ClosestAreaService closestAreaService;

    @EJB
    private ClosestLocationService closestLocationService;

    @EJB
    private SpatialEnrichmentService enrichmentService;

    @EJB
    private MapConfigService mapConfigService;

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @EJB
    private FilterAreasService filterAreasService;

    @EJB
    private SpatialMessageServiceBean messageProducer;

    @Override
    public void getAreaByLocation(@Observes @GetAreaByLocationEvent SpatialMessageEvent message) {
        log.info("Getting area by location.");
        try {
            List<AreaExtendedIdentifierType> areaTypesByLocation = areaByLocationService.getAreaTypesByLocation(message.getAreaByLocationSpatialRQ());
            log.debug("Send back areaByLocation response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapAreaByLocationResponse(areaTypesByLocation));
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void getAreaTypeNames(@Observes @GetAreaTypeNamesEvent SpatialMessageEvent message) {
        log.info("Getting area names.");
        try {
            List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();
            log.debug("Send back area types response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapAreaTypeNamesResponse(areaTypeNames));
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    private void sendError(SpatialMessageEvent message, Exception e) {
        log.error("[ Error when getting area types from source. ] ", e);
        spatialErrorEvent.fire(new SpatialMessageEvent(message.getMessage(), SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Exception when getting areaByLocation [ " + e.getMessage())));
    }

    @Override
    public void getClosestArea(@Observes @GetClosestAreaEvent SpatialMessageEvent message) {
        log.info("Getting closest area.");
        try {
            List<Area> closestAreas = closestAreaService.getClosestAreas(message.getClosestAreaSpatialRQ());
            log.debug("Send back closestAreas response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapClosestAreaResponse(closestAreas));
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void getClosestLocation(@Observes @GetClosestLocationEvent SpatialMessageEvent message) {
        log.info("Getting closest locations.");
        try {
            List<Location> closestLocations = closestLocationService.getClosestLocations(message.getClosestLocationSpatialRQ());
            log.debug("Send back closest locations response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapClosestLocationResponse(closestLocations));
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void getSpatialEnrichment(@Observes @GetSpatialEnrichmentEvent SpatialMessageEvent message) {
        log.info("Getting spatial enrichment.");
        try {
            SpatialEnrichmentRS spatialEnrichmentRS = enrichmentService.getSpatialEnrichment(message.getSpatialEnrichmentRQ());
            log.debug("Send back enrichment response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapEnrichmentResponse(spatialEnrichmentRS));
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void getFilterAreas(@Observes @GetFilterAreaEvent SpatialMessageEvent message) {
        log.info("Getting Filter Areas");
        try {
            FilterAreasSpatialRQ filterAreaSpatialRQ = message.getFilterAreasSpatialRQ();
            FilterAreasSpatialRS filterAreasSpatialRS = filterAreasService.filterAreas(filterAreaSpatialRQ);
            log.debug("Send back filtered Areas");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapFilterAreasResponse(filterAreasSpatialRS));
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void saveOrUpdateMapConfiguration(@Observes @SaveMapConfigurationEvent SpatialMessageEvent message) {
        log.info("Saving/Updating map configurations.");
        try {
            mapConfigService.saveOrUpdateMapConfiguration(message.getSpatialSaveOrUpdateMapConfigurationRQ());
            log.debug("Send back map configurations response.");
            String response = SpatialModuleResponseMapper.mapSpatialSaveMapConfigurationRSToString(new SpatialSaveOrUpdateMapConfigurationRS());
            messageProducer.sendModuleResponseMessage(message.getMessage(), response);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    @Override
    public void ping(@Observes @PingEvent SpatialMessageEvent message) {
        log.info("Getting Filter Areas");
        try {
            PingRS pingRS = new PingRS();
            pingRS.setResponse("pong");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapPingResponse(pingRS));
        } catch (Exception e) {
            log.error("[ Error when responding to ping. ] ", e);
            sendError(message, e);
        }
    }
}
