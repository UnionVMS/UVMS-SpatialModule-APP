package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson;

import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

import java.util.Date;
import java.util.List;

public class UserAreaGeoJsonDto extends GeoJsonDto {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ID = "id";
    private static final String SCOPE_SELECTION = "scopeSelection";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String SUB_TYPE = "subType";
    private static final String DATASET_NAME = "datasetName";


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

        if (datasetNameObj != null) {
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

}
