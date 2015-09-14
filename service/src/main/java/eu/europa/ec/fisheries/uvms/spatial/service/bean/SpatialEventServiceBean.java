package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialEventQ;
import eu.europa.ec.fisheries.uvms.spatial.message.event.*;
import eu.europa.ec.fisheries.uvms.spatial.model.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialEventService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

@Stateless
@Slf4j
@Local(SpatialEventService.class)
public class SpatialEventServiceBean implements SpatialEventService {

    @EJB
    private AreaByLocationService areaByLocationService;

    @EJB
    private ClosestAreaService closestAreaService;

    @EJB
    private ClosestLocationService closestLocationService;

    @EJB
    private AreaDetailsService areaDetailsService;

    @EJB
    private SpatialEnrichmentService enrichmentService;

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @EJB
    private SpatialEventQ messageProducer;

    @Inject
    @SpatialMessageErrorEvent
    Event<SpatialMessageEvent> spatialErrorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getAreaByLocation(@Observes @GetAreaByLocationEvent SpatialMessageEvent message) {
        log.info("Getting area by location.");
        try {
            List<AreaTypeEntry> areaTypesByLocation = areaByLocationService.getAreaTypesByLocation(message.getAreaByLocationSpatialRQ());
            log.debug("Send back areaByLocation response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapAreaByLocationResponse(areaTypesByLocation));
        }
        catch (Exception e){
            log.error("[ Error when getting areaTypesByLocation from source. ] ", e);
            spatialErrorEvent.fire(new SpatialMessageEvent(message.getMessage(), SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Exception when getting areaTypesByLocation [ " + e.getMessage())));
        }
    }

    @Override
    public void getAreaTypeNames(@Observes @GetAreaTypeNamesEvent SpatialMessageEvent message) {
        log.info("Getting area names.");
        try {
            List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();
            log.debug("Send back area types response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapAreaTypeNamesResponse(areaTypeNames));
        }
        catch (Exception e) {
            log.error("[ Error when getting area types from source. ] ", e);
            spatialErrorEvent.fire(new SpatialMessageEvent(message.getMessage(), SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Exception when getting areaByLocation [ " + e.getMessage())));
        }
    }

    @Override
    public void getClosestArea(@Observes @GetClosestAreaEvent SpatialMessageEvent message) {
        log.info("Getting closest area.");
        try {
            List<Area> closestAreas = closestAreaService.getClosestAreas(message.getClosestAreaSpatialRQ());
            log.debug("Send back closestAreas response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapClosestAreaResponse(closestAreas));
        }
        catch (Exception e){
            log.error("[ Error when getting closestAreas from source. ] ", e);
            spatialErrorEvent.fire(new SpatialMessageEvent(message.getMessage(), SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Exception when getting closestAreas [ " + e.getMessage())));
        }
    }

    @Override
    public void getClosestLocation(@Observes @GetClosestLocationEvent SpatialMessageEvent message) {
        log.info("Getting closest locations.");
        try {
            List<Location> closestLocations = closestLocationService.getClosestLocations(message.getClosestLocationSpatialRQ());
            log.debug("Send back closest locations response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapClosestLocationResponse(closestLocations));
        }
        catch (Exception e){
            log.error("[ Error when getting closest location from source. ] ", e);
            spatialErrorEvent.fire(new SpatialMessageEvent(message.getMessage(), SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Exception when getting closest locations [ " + e.getMessage())));
        }
    }

    @Override
    public void getSpatialEnrichment(@Observes @GetSpatialEnrichmentEvent SpatialMessageEvent message) {
        log.info("Getting spatial enrichment.");
        try {
            SpatialEnrichmentRS spatialEnrichmentRS = enrichmentService.getSpatialEnrichment(message.getSpatialEnrichmentRQ());
            log.debug("Send back enrichment response.");
            messageProducer.sendModuleResponseMessage(message.getMessage(), SpatialModuleResponseMapper.mapEnrichmentResponse(spatialEnrichmentRS));
        }
        catch (Exception e){
            log.error("[ Error when getting enrichment from source. ] ", e);
            spatialErrorEvent.fire(new SpatialMessageEvent(message.getMessage(), SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Exception when getting enrichment [ " + e.getMessage())));
        }
    }
}
