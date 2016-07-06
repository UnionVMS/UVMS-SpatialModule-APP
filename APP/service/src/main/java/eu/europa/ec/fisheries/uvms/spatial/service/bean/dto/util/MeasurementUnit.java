/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

public enum MeasurementUnit {

    METERS(1),
    KILOMETERS(1000),
    NAUTICAL_MILES(1852),
    MILES(1609.344);

    private double ratio;

    MeasurementUnit(double ratio) {
        this.ratio = ratio;
    }

    public double getRatio() {
        return ratio;
    }

    public static MeasurementUnit getMeasurement(String unit) {
        for (MeasurementUnit measurementUnit : values()) {
            if (measurementUnit.name().equalsIgnoreCase(unit)) {
                return measurementUnit;
            }
        }
        throw new SpatialServiceException(SpatialServiceErrors.WRONG_MEASUREMENT_UNIT, unit);
    }
}