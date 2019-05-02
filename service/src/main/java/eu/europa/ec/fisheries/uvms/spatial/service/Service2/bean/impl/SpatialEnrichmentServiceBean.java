/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.impl;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.AreaService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
@Slf4j
public class SpatialEnrichmentServiceBean {

    @EJB
    private AreaService areaService;

    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ request) throws ServiceException {
        PointType point = request.getPoint();
        UnitType unit = request.getUnit();
        List<AreaType> areaTypes = request.getAreaTypes().getAreaTypes();
        List<LocationType> locationTypes = request.getLocationTypes().getLocationTypes();
        return computeSpatialEnrichment(point, unit, areaTypes, locationTypes);
    }

    public BatchSpatialEnrichmentRS getBatchSpatialEnrichment(BatchSpatialEnrichmentRQ spatialBatchEnrichmentRQ) throws ServiceException {
        List<SpatialEnrichmentRS> spatialBatchEnrichRespList = new ArrayList<>();
        for (SpatialEnrichmentRQListElement enrichmentListElement : spatialBatchEnrichmentRQ.getEnrichmentLists()) {
            spatialBatchEnrichRespList.add(computeSpatialEnrichment(enrichmentListElement.getPoint(), enrichmentListElement.getUnit(),
                    enrichmentListElement.getAreaTypes(), enrichmentListElement.getLocationTypes()));
        }
        return convertToBatchReponse(spatialBatchEnrichRespList);
    }

    private BatchSpatialEnrichmentRS convertToBatchReponse(List<SpatialEnrichmentRS> spatialBatchEnrichRespList) {
        BatchSpatialEnrichmentRS batchResponse = new BatchSpatialEnrichmentRS();
        List<SpatialEnrichmentRSListElement> enrichmentRespList = batchResponse.getEnrichmentRespLists();
        for (SpatialEnrichmentRS spatialEnrichmentRS : spatialBatchEnrichRespList) {
            enrichmentRespList.add(new SpatialEnrichmentRSListElement(spatialEnrichmentRS.getAreasByLocation(), spatialEnrichmentRS.getClosestAreas(),
                    spatialEnrichmentRS.getClosestLocations()));
        }
        return batchResponse;
    }

    private SpatialEnrichmentRS computeSpatialEnrichment(PointType point, UnitType unit, List<AreaType> areaTypes, List<LocationType> locationTypes) throws ServiceException {
        AreaByLocationSpatialRQ areaByLocationSpatialRQ = new AreaByLocationSpatialRQ();
        areaByLocationSpatialRQ.setPoint(point);
        List<AreaExtendedIdentifierType> areaTypesByLocation = areaService.getAreasByPoint(areaByLocationSpatialRQ);

        ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
        ClosestAreaSpatialRQ.AreaTypes types = new ClosestAreaSpatialRQ.AreaTypes();
        types.getAreaTypes().addAll(areaTypes);
        closestAreaSpatialRQ.setAreaTypes(types);
        closestAreaSpatialRQ.setUnit(unit);
        closestAreaSpatialRQ.setPoint(point);

        List<Area> closestAreas = areaService.getClosestArea(point.getLongitude(), point.getLatitude(), 3857, unit);

        ClosestLocationSpatialRQ closestLocationSpatialRQ = new ClosestLocationSpatialRQ();
        closestLocationSpatialRQ.setPoint(point);
        closestLocationSpatialRQ.setUnit(unit);
        ClosestLocationSpatialRQ.LocationTypes locationTp = new ClosestLocationSpatialRQ.LocationTypes();
        locationTp.getLocationTypes().addAll(locationTypes);
        closestLocationSpatialRQ.setLocationTypes(locationTp);

        List<Location> closestLocations = areaService.getClosestPointByPoint(closestLocationSpatialRQ);

        SpatialEnrichmentRS response = new SpatialEnrichmentRS();

        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaTypesByLocation);
            response.setAreasByLocation(areasByLocationType);
        }

        if (closestAreas != null) {
            ClosestAreasType closestAreasType = new ClosestAreasType();
            closestAreasType.getClosestAreas().addAll(closestAreas);
            response.setClosestAreas(closestAreasType);
        }

        if (closestLocations != null){
            ClosestLocationsType locationType = new ClosestLocationsType();
            locationType.getClosestLocations().addAll(closestLocations);
            response.setClosestLocations(locationType);
        }
        return response;
    }

}