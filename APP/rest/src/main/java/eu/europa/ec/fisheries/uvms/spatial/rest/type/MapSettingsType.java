/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.type;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import lombok.experimental.Delegate;

public class MapSettingsType {

    @Delegate(excludes = Exclude.class)
    private MapConfigurationType mapConfigurationType;

    public MapSettingsType(MapConfigurationType mapConfigurationType) {
        this.mapConfigurationType = mapConfigurationType;
    }


    public interface Exclude {
        CoordinatesFormat getCoordinatesFormat();
        ScaleBarUnits getScaleBarUnits();
    }

    public String getCoordinatesFormat(){

        return mapConfigurationType.getCoordinatesFormat().value().toLowerCase();

    }

    public String getScaleBarUnits(){

        return mapConfigurationType.getScaleBarUnits().value().toLowerCase();

    }
}