/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.MultiPoint;

public abstract class UtilsDao {

    Map<Integer, Integer> SRID_EPSG_MAP = Collections.synchronizedMap(new LinkedHashMap() {

        private static final int MAX_ENTRIES = 250;

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {

            return size() > MAX_ENTRIES;
        }
    });

    Map<Integer, Integer> EPSG_SRID_MAP = Collections.synchronizedMap(new LinkedHashMap() {

        private static final int MAX_ENTRIES = 250;

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {

            return size() > MAX_ENTRIES;
        }
    });

    public abstract Integer mapDefaultSRIDToEPSG(Integer srid);

    public abstract Integer mapEPSGtoDefaultSRID(Integer epsg);

    /**
     * Converts a polygon or multi-polygon into a multi-point composed of randomly location points within the original areas.
     * @param wkt
     * @return
     */
    public abstract MultiPoint generatePoints(String wkt, Integer numberOfPoints);

}
