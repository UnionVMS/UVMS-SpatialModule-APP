/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.message.event.*;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

@Local
public interface SpatialEventService {

    void getAreaByLocation(@Observes @GetAreaByLocationEvent SpatialMessageEvent message);

    void getClosestArea(@Observes @GetClosestAreaEvent SpatialMessageEvent message);

    void getClosestLocation(@Observes @GetClosestLocationEvent SpatialMessageEvent message);

    void getSpatialEnrichment(@Observes @GetSpatialEnrichmentEvent SpatialMessageEvent message);

    void getAreaTypeNames(@Observes @GetAreaTypeNamesEvent SpatialMessageEvent message);

    void getFilterAreas(@Observes @GetFilterAreaEvent SpatialMessageEvent message);

    void saveOrUpdateSpatialMapConfiguration(@Observes @SaveOrUpdateMapConfigurationEvent SpatialMessageEvent message);

    void deleteMapConfiguration(@Observes @DeleteMapConfigurationEvent SpatialMessageEvent message);

    void getMapConfiguration(@Observes @GetMapConfigurationEvent SpatialMessageEvent message);

    void ping(@Observes @PingEvent SpatialMessageEvent message);

    void areaByCode(@Observes @AreaByCodeEvent SpatialMessageEvent message);

    void getGeometryForPortCode(@Observes @GetGeometryByPortCodeEvent SpatialMessageEvent message);

}