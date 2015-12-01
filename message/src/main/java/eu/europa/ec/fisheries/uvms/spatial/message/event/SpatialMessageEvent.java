package eu.europa.ec.fisheries.uvms.spatial.message.event;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;

import javax.jms.TextMessage;

public class SpatialMessageEvent {

    private TextMessage message;

    private AreaByLocationSpatialRQ areaByLocationSpatialRQ;
    private ClosestAreaSpatialRQ closestAreaSpatialRQ;
    private SpatialEnrichmentRQ spatialEnrichmentRQ;
    private AllAreaTypesRequest allAreaTypesRequest;
    private ClosestLocationSpatialRQ closestLocationSpatialRQ;
    private FilterAreasSpatialRQ filterAreasSpatialRQ;
    private SpatialDeleteMapConfigurationRQ spatialDeleteMapConfigurationRQ;
    private SpatialSaveOrUpdateMapConfigurationRQ spatialSaveOrUpdateMapConfigurationRQ;
    private SpatialGetMapConfigurationRQ spatialGetMapConfigurationRQ;
    private PingRQ pingRQ;
    private SpatialFault fault;

    public SpatialMessageEvent(TextMessage message, AreaByLocationSpatialRQ areaByLocationSpatialRQ) {
        this.message = message;
        this.areaByLocationSpatialRQ = areaByLocationSpatialRQ;
    }

    public SpatialMessageEvent(TextMessage message, SpatialFault fault) {
        this.message = message;
        this.fault = fault;
    }

    public SpatialMessageEvent(TextMessage message, ClosestAreaSpatialRQ closestAreaSpatialRQ) {
        this.message = message;
        this.closestAreaSpatialRQ = closestAreaSpatialRQ;
    }

    public SpatialMessageEvent(TextMessage message, SpatialEnrichmentRQ spatialEnrichmentRQ) {
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

    public SpatialMessageEvent(TextMessage textMessage, PingRQ pingRQ) {
        this.message = textMessage;
        this.pingRQ = pingRQ;
    }

    public SpatialMessageEvent(TextMessage textMessage, SpatialSaveOrUpdateMapConfigurationRQ spatialSaveOrUpdateMapConfigurationRQ) {
        this.message = textMessage;
        this.spatialSaveOrUpdateMapConfigurationRQ = spatialSaveOrUpdateMapConfigurationRQ;
    }

    public SpatialMessageEvent(TextMessage textMessage, SpatialGetMapConfigurationRQ spatialGetMapConfigurationRQ) {
        this.message = textMessage;
        this.spatialGetMapConfigurationRQ = spatialGetMapConfigurationRQ;
    }

    public SpatialMessageEvent(TextMessage textMessage, SpatialDeleteMapConfigurationRQ mapConfigurationRQ) {
        this.message = textMessage;
        this.spatialDeleteMapConfigurationRQ = mapConfigurationRQ;
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

    public PingRQ getPingRQ() {
        return pingRQ;
    }

    public SpatialSaveOrUpdateMapConfigurationRQ getSpatialSaveOrUpdateMapConfigurationRQ() {
        return spatialSaveOrUpdateMapConfigurationRQ;
    }

    public SpatialGetMapConfigurationRQ getSpatialGetMapConfigurationRQ() {
        return spatialGetMapConfigurationRQ;
    }

    public AllAreaTypesRequest getAllAreaTypesRequest() {
        return allAreaTypesRequest;
    }

    public SpatialDeleteMapConfigurationRQ getSpatialDeleteMapConfigurationRQ() {
        return spatialDeleteMapConfigurationRQ;
    }
}
