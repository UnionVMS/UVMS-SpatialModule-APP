/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasByLocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreasType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationsType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialEnrichmentService;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Local(SpatialEnrichmentService.class)
@Slf4j
public class SpatialEnrichmentServiceBean implements SpatialEnrichmentService {

    @EJB
    private AreaService areaService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ request) throws ServiceException {

        AreaByLocationSpatialRQ areaByLocationSpatialRQ = new AreaByLocationSpatialRQ();
        areaByLocationSpatialRQ.setPoint(request.getPoint());
        List<AreaExtendedIdentifierType> areaTypesByLocation = areaService.getAreasByPoint(areaByLocationSpatialRQ);

        List<AreaType> areaTypes = request.getAreaTypes().getAreaTypes();
        ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
        ClosestAreaSpatialRQ.AreaTypes types = new ClosestAreaSpatialRQ.AreaTypes();
        types.getAreaTypes().addAll(areaTypes);
        closestAreaSpatialRQ.setAreaTypes(types);
        closestAreaSpatialRQ.setUnit(request.getUnit());
        closestAreaSpatialRQ.setPoint(request.getPoint());

        List<Area> closestAreas = areaService.getClosestArea(request.getPoint().getLongitude(), request.getPoint().getLatitude(), 3857, request.getUnit());

        List<LocationType> locationTypes = request.getLocationTypes().getLocationTypes();
        ClosestLocationSpatialRQ closestLocationSpatialRQ = new ClosestLocationSpatialRQ();
        closestLocationSpatialRQ.setPoint(request.getPoint());
        closestLocationSpatialRQ.setUnit(request.getUnit());
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