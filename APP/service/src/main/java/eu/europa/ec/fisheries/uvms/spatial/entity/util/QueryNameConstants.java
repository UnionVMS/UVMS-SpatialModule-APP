/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.entity.util;

/**
 * All constants for Spatial modules
 **/
public final class QueryNameConstants {

    private QueryNameConstants() {}

    /**
     * Find System
     */
    public static final String FIND_USER_AREA_LAYER = "AreaLocationType.findUserAreaLayerMappings";
    
    /**
     * Find User Area
     */
    public static final String FIND_ALL_USER_AREAS = "UserArea.findAllUserAreas";
    public static final String FIND_ALL_USER_AREAS_GROUP = "UserArea.findAllUserAreaGroup";
    public static final String FIND_ALL_USER_AREAS_BY_GIDS = "UserAreas.findAllUserAreasByGid";

    /**
     * Native Queries
     */
    public static final String EEZ_COLUMNS = "eezEntity.findSelectedColumns";
    public static final String RFMO_COLUMNS = "rfmoEntity.findSelectedColumns";
    public static final String USERAREA_COLUMNS = "userAreasEntity.findSelectedColumns";

    /**
     * MapDto Config queries
     */
    public static final String FIND_BY_ID = "ReportLayerConfig.findById";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE = "ServiceLayer.findServiceLayerBySubType";
    public static final String FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING = "ServiceLayer.findServiceLayerBySubTypeWithoutBing";

}