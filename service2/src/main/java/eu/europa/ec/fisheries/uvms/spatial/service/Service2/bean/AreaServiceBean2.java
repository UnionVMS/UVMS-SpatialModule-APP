/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao.AreaDao2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao.SpatialQueriesDao;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.BaseAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.PortDistanceInfoDto;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortAreaEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortEntity2;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils.GeometryMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils.GeometryUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils.MeasurementUnit;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AreaServiceBean2 {

    private static final String MULTIPOINT = "MULTIPOINT";

    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    @Inject
    private AreaDao2 portAreaDao;

    @Inject
    SpatialQueriesDao spatialQueriesDao;





    public List<PortEntity2> getPortsByAreaCodes(List<String> codes){
        return portAreaDao.getPortsByAreaCodes(codes);
    }

    public List<PortAreaEntity2> getPortAreasByPoint(Double lat, Double lon){
        Point point = (Point) GeometryUtils.createPoint(lat, lon);
        return portAreaDao.getPortAreasByPoint(point);
    }

    public List<BaseAreaDto> getAreasByPoint(Double lat, Double lon){

        Point point = (Point) GeometryUtils.createPoint(lat, lon);
        return spatialQueriesDao.getAreasByPoint(point);

    }

    public PortDistanceInfoDto findClosestPortByPosition(Double lat,  Double lon){
        Point point = (Point) GeometryUtils.createPoint(lat, lon);
        return portAreaDao.getClosestPort(point);
    }

    public List<BaseAreaDto> getClosestAreasByPoint(Double lat, Double lon){
        Point point = (Point) GeometryUtils.createPoint(lat, lon);
        return spatialQueriesDao.getClosestAreaByPoint(point);
    }


    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ request){

        PointType pointType = request.getPoint();

        Point point = (Point) GeometryUtils.createPoint(pointType.getLatitude(), pointType.getLongitude());
        return computeSpatialEnrichment(point);
    }



    private SpatialEnrichmentRS computeSpatialEnrichment(Point point){


        List<BaseAreaDto> areaTypesByLocation = spatialQueriesDao.getAreasByPoint(point);

        List<BaseAreaDto> closestAreas = spatialQueriesDao.getClosestAreaByPoint(point);

        PortDistanceInfoDto closestLocation = portAreaDao.getClosestPort(point);

        SpatialEnrichmentRS response = new SpatialEnrichmentRS();

        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();

            for( BaseAreaDto entity : areaTypesByLocation){
                AreaExtendedIdentifierType areaExtendedIdentifierType = new AreaExtendedIdentifierType();
                areaExtendedIdentifierType.setCode(entity.getCode());
                areaExtendedIdentifierType.setName(entity.getName());
                areaExtendedIdentifierType.setAreaType(entity.getType());
                areaExtendedIdentifierType.setId(String.valueOf(entity.getGid()));
                areasByLocationType.getAreas().add(areaExtendedIdentifierType);
            }
            response.setAreasByLocation(areasByLocationType);
        }

        if (closestAreas != null) {
            ClosestAreasType closestAreasType = new ClosestAreasType();
            for(BaseAreaDto baseArea : closestAreas){
                Area area = new Area();
                area.setAreaType(baseArea.getType());
                area.setCode(baseArea.getCode());
                area.setDistance(baseArea.getDistance() / MeasurementUnit.NAUTICAL_MILES.getRatio());
                area.setId(String.valueOf(baseArea.getGid()));
                area.setName(baseArea.getName());
                area.setUnit(UnitType.NAUTICAL_MILES);

                closestAreasType.getClosestAreas().add(area);
            }
            response.setClosestAreas(closestAreasType);
        }

        if (closestLocation != null){
            ClosestLocationsType locationType = new ClosestLocationsType();
            Location location = new Location();
            location.setCentroid(closestLocation.getPort().getCentroid());
            location.setCode(closestLocation.getPort().getCode());
            location.setCountryCode(closestLocation.getPort().getCountryCode());

            double distanceInMeters = closestLocation.getDistance();

            location.setDistance(distanceInMeters / MeasurementUnit.NAUTICAL_MILES.getRatio());
            location.setEnabled(closestLocation.getPort().getEnabled());
            location.setExtent(closestLocation.getPort().getExtent());
            location.setGid(String.valueOf(closestLocation.getPort().getId()));
            location.setId(String.valueOf(closestLocation.getPort().getId()));
            location.setLocationType(LocationType.PORT);
            location.setName(closestLocation.getPort().getName());
            location.setUnit(UnitType.NAUTICAL_MILES);
            location.setWkt(closestLocation.getPort().getGeometry());


            locationType.getClosestLocations().add(location);
            response.setClosestLocations(locationType);
        }
        return response;
    }


}