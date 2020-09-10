/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaSimpleType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.GenericSystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.SystemAreaNamesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMapping;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMetadata;

public interface AreaService {

    // FIXME reuse searchAreasByCode somehow
    String getGeometryForPort(String portCode);

    Map<String, Object> getLocationDetails(LocationTypeEntry locationTypeEntry) throws ServiceException;

    List<Map<String, Object> > getAreasByPoint(@NotNull Double latitude, @NotNull Double longitude, @NotNull Integer crs, @NotNull String userName, @NotNull AreaType areaType) throws ServiceException;

    List<AreaExtendedIdentifierType> getPortAreasByPoint(Point incoming) throws ServiceException;

    List<GenericSystemAreaDto> searchAreasByNameOrCode(@NotNull String areaType, @NotNull String filter) throws ServiceException;

    List<SystemAreaNamesDto> searchAreasByCode(String areaType, String filter) throws ServiceException;

    FilterAreasSpatialRS computeAreaFilter(FilterAreasSpatialRQ filterAreasSpatialRQ) throws ServiceException;

    List<Map<String, Object>> getAreasByIds(List<AreaTypeEntry> areaTypes) throws ServiceException;

    UploadMetadata metadata(byte[] data, String areaType) throws ServiceException;

    Map<String, Object> getAreaById(@NotNull Long id, @NotNull AreaType areaType) throws ServiceException;

    List<Area> getClosestArea(@NotNull Double longitude, @NotNull Double latitude, @NotNull Integer crs, @NotNull UnitType unit) throws ServiceException;

    List<AreaExtendedIdentifierType> getAreasByPoint(AreaByLocationSpatialRQ request) throws ServiceException;

    List<AreaExtendedIdentifierType> getUserAreasByPoint(Date activeDate, Geometry incoming) throws ServiceException;
        
    List<Location> getClosestPointByPoint(ClosestLocationSpatialRQ request) throws ServiceException;

    Map<String, String> getAllCountriesDesc() throws ServiceException;

    void upload(UploadMapping mapping, String type, Integer code) throws ServiceException;

    List<AreaSimpleType> getAreasByCode(List<AreaSimpleType> areaSimpleTypeList) throws ServiceException;

}