/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.validator;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;

public class SpatialValidator {


    private SpatialValidator(){

    }

    private static void validateNull(Object object){

        if (object == null) {

            throw new IllegalArgumentException("ARGUMENT CAN NOT BE NULL");

        }
    }

    public static void validate(SpatialSaveOrUpdateMapConfigurationRQ request) {

        validateNull(request);

        MapConfigurationType mapConfiguration = request.getMapConfiguration();


        if (mapConfiguration == null) {

            throw new IllegalArgumentException("MAP CONFIGURATION CAN NOT BE NULL");

        }

        if (mapConfiguration.getDisplayProjectionId() != null && mapConfiguration.getMapProjectionId() == null) {

            throw new IllegalArgumentException("MAP PROJECTION IS MANDATORY");

        }

    }

    public static void validate(SpatialDeleteMapConfigurationRQ request) {

        validateNull(request);

    }

    public static void validate(Long reportId) {

        validateNull(reportId);

    }
}