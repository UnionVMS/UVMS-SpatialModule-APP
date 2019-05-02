/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson;

import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import org.apache.commons.lang3.StringUtils;
import java.util.Date;
import java.util.List;

public class UserAreaGeoJsonDto extends GeoJsonDto {

    private static final String NAME = "name", DESCRIPTION = "description", ID = "id";
    private static final String SCOPE_SELECTION = "scopeSelection", START_DATE = "startDate", END_DATE = "endDate";
    private static final String SUB_TYPE = "subType", DATASET_NAME = "datasetName";

    public String getName() {
        return getNullOrString(properties.get(NAME));
    }

    public void setName(String name) {
        if (name != null) {
            properties.put(NAME, name);
        }
    }

    public String getDesc() {
        return getNullOrString(properties.get(DESCRIPTION));
    }

    public void setDesc(String desc) {
        if (desc != null) {
            properties.put(DESCRIPTION, desc);
        }
    }

    public String getStartDate() {
        return getNullOrString(properties.get(START_DATE));
    }

    public void setStartDate(Date startDate) {
        if (startDate != null) {
            properties.put(START_DATE, DateUtils.dateToString(startDate));
        }
    }

    public String getEndDate() {
        return getNullOrString(properties.get(END_DATE));
    }

    public void setEndDate(Date startDate) {
        if (startDate != null) {
            properties.put(END_DATE, DateUtils.dateToString(startDate));
        }
    }

    public String getSubType() {
        return getNullOrString(properties.get(SUB_TYPE));
    }

    public void setSubType(String subType) {
        if (subType != null) {
            properties.put(SUB_TYPE, subType);
        }
    }


    public Long getId() {
        try {
            Object gid = properties.get(ID);
            if (gid != null) {
                if (gid instanceof Number) {
                    return ((Number) gid).longValue();
                } else {
                    return Long.valueOf((String) gid);
                }

            }
        } catch (NumberFormatException nfe) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_USER_AREA_ID, nfe);
        }
        return null;
    }

    public void setId(Long gid) {
        properties.put(ID, getNullOrString(gid));
    }

    public List<String> getScopeSelection() {
        Object scopeSelectionObj = properties.get(SCOPE_SELECTION);
        List<String> returnList = null;

        if (scopeSelectionObj != null) {
            returnList = (List<String>) scopeSelectionObj;
        }

        return returnList;
    }

    public void setScopeSelection(List<String> scopeSelection) {
        properties.put(SCOPE_SELECTION, scopeSelection);
    }

    public String getDatasetName() {
        Object datasetNameObj = properties.get(DATASET_NAME);
        String datasetName = null;

        if (datasetNameObj instanceof String && StringUtils.isNotBlank((String) datasetNameObj)) {
            datasetName = (String) datasetNameObj;
        }

        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        properties.put(DATASET_NAME, datasetName);
    }

    private String getNullOrString(Object propObj) {
        String propValue = null;

        if (propObj != null) {
            propValue = String.valueOf(propObj);
        }

        return propValue;
    }


    public String getExtent() {
        return (String) properties.get(EXTENT);
    }

    public void setExtent(String extent) {
        properties.put(EXTENT, extent);
    }
}