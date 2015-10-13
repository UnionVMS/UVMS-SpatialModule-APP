package eu.europa.ec.fisheries.uvms.spatial.message.event;

import javax.jms.TextMessage;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AllAreaTypesRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialFault;

public class SpatialMessageEvent {

    private TextMessage message;
    private AreaByLocationSpatialRQ areaByLocationSpatialRQ;
    private ClosestAreaSpatialRQ closestAreaSpatialRQ;
    private SpatialEnrichmentRQ spatialEnrichmentRQ;
    private AllAreaTypesRequest allAreaTypesRequest;
    private ClosestLocationSpatialRQ closestLocationSpatialRQ;
    private FilterAreasSpatialRQ filterAreasSpatialRQ;
    private SpatialFault fault;

    public SpatialMessageEvent(TextMessage message, AreaByLocationSpatialRQ areaByLocationSpatialRQ){
        this.message = message;
        this.areaByLocationSpatialRQ = areaByLocationSpatialRQ;
    }

    public SpatialMessageEvent(TextMessage message, SpatialFault fault) {
        this.message = message;
        this.fault = fault;
    }

    public SpatialMessageEvent(TextMessage message, ClosestAreaSpatialRQ closestAreaSpatialRQ){
        this.message = message;
        this.closestAreaSpatialRQ = closestAreaSpatialRQ;
    }

    public SpatialMessageEvent(TextMessage message, SpatialEnrichmentRQ spatialEnrichmentRQ){
        this.message = message;
        this.spatialEnrichmentRQ = spatialEnrichmentRQ;
    }

    public SpatialMessageEvent(TextMessage textMessage, AllAreaTypesRequest allAreaTypesRequest) {
        this.message = textMessage;
        this.allAreaTypesRequest = allAreaTypesRequest;
    }

    public SpatialMessageEvent(TextMessage textMessage, ClosestLocationSpatialRQ closestLocationSpatialRQ) {
        this.message = textMessage;
        this.closestLocationSpatialRQ = closestLocationSpatialRQ;
    }
    
    public SpatialMessageEvent(TextMessage textMessage, FilterAreasSpatialRQ filterAreasSpatialRQ) {
    	this.message = textMessage;
    	this.filterAreasSpatialRQ = filterAreasSpatialRQ;
    }

    public TextMessage getMessage() {
        return message;
    }

    public AreaByLocationSpatialRQ getAreaByLocationSpatialRQ() {
        return areaByLocationSpatialRQ;
    }

    public ClosestAreaSpatialRQ getClosestAreaSpatialRQ() {
        return closestAreaSpatialRQ;
    }

    public SpatialEnrichmentRQ getSpatialEnrichmentRQ() {
        return spatialEnrichmentRQ;
    }

    public SpatialFault getFault() {
        return fault;
    }

    public ClosestLocationSpatialRQ getClosestLocationSpatialRQ() {
        return closestLocationSpatialRQ;
    }
    
    public FilterAreasSpatialRQ getFilterAreasSpatialRQ() {
    	return filterAreasSpatialRQ;
    }
    
    public AllAreaTypesRequest getAllAreaTypesRequest() {
    	return allAreaTypesRequest;
    }
}
