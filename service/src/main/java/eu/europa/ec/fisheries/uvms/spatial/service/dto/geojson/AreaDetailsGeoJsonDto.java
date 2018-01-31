/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;

public class AreaDetailsGeoJsonDto extends GeoJsonDto {

    private List<Map<String, Object>> allAreaProperties = new ArrayList<>();

    public SimpleFeature toFeature() throws ParseException {
        return super.toFeature(MultiPolygon.class);
    }

    public SimpleFeature toFeature(Map<String, Object> properties) throws ParseException {
        return super.toFeature(MultiPolygon.class, properties);
    }

    public List<Map<String, Object>> getAllAreaProperties() {
        return allAreaProperties;
    }

    public void setAllAreaProperties(List<Map<String, Object>> allAreaProperties) {
        this.allAreaProperties = allAreaProperties;
    }

}