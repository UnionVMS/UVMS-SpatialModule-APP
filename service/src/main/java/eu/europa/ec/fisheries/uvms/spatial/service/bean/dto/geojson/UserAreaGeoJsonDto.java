package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson;

import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.GeoJsonDto;

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


    public String getName() {
        return String.valueOf(properties.get(NAME));
    }

    public void setName(String name) {
        if (name != null) {
            properties.put(NAME, name);
        }
    }

    public String getDesc() {
        return String.valueOf(properties.get(DESCRIPTION));
    }

    public void setDesc(String desc) {
        if (desc != null) {
            properties.put(DESCRIPTION, desc);
        }
    }

    public String getStartDate() {
        return String.valueOf(properties.get(START_DATE));
    }

    public void setStartDate(Date startDate) {
        if (startDate != null) {
            properties.put(START_DATE, DateUtils.dateToString(startDate));
        }
    }

    public String getEndDate() {
        return String.valueOf( properties.get(END_DATE));
    }

    public void setEndDate(Date startDate) {
        if (startDate != null) {
            properties.put(END_DATE, DateUtils.dateToString(startDate));
        }
    }

    public String getSubType() {
        return String.valueOf(properties.get(SUB_TYPE));
    }

    public void setSubType(String subType) {
        if (subType != null) {
            properties.put(SUB_TYPE, subType);
        }
    }



    public Long getId() {
        Object gid = properties.get(ID);
        if (gid != null) {
            try {
                return Long.valueOf((String)gid);
            } catch (NumberFormatException nfe ) {
                //do nothing, later we return null anyway
            }
        }

        return null;
    }

    public void setId(Long gid) {
        properties.put(ID, String.valueOf(gid));
    }

    public List<String> getScopeSelection() {
        Object scopeSelectionObj = properties.get(SCOPE_SELECTION);
        List<String> returnList = null;

        if (scopeSelectionObj != null) {
            returnList = (List<String>) scopeSelectionObj;
        }

        return returnList;
    }

    public void setScopeSelection(List<String> scopeSelection){
        properties.put(SCOPE_SELECTION, scopeSelection);
    }

}
