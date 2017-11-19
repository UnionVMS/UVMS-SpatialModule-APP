/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.message.event;

import javax.jms.TextMessage;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AllAreaTypesRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByCodeRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PingRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;

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
    private AreaByCodeRequest areaByCodeRequest;

    public SpatialMessageEvent(TextMessage message, AreaByLocationSpatialRQ areaByLocationSpatialRQ) {
        this.message = message;
        this.areaByLocationSpatialRQ = areaByLocationSpatialRQ;
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

    public SpatialMessageEvent(TextMessage textMessage, AreaByCodeRequest areaByCodeRequest) {
        this.message = textMessage;
        this.areaByCodeRequest = areaByCodeRequest;
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

    public AreaByCodeRequest getAreaByCodeRequest() {

        return areaByCodeRequest;
    }
}