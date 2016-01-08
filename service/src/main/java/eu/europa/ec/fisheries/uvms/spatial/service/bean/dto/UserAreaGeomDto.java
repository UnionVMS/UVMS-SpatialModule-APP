package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import eu.europa.ec.fisheries.uvms.common.DateUtils;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.isNumeric;

public class UserAreaGeomDto extends GeoJsonDto {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ID = "id";
    private static final String IS_SHARED = "isShared";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String SUB_TYPE = "subType";
    private static final boolean DEFAULT_IS_SHARED_VALUE = false;

    public String getName() {
        return properties.get(NAME);
    }

    public void setName(String name) {
        if (name != null) {
            properties.put(NAME, name);
        }
    }

    public String getDesc() {
        return properties.get(DESCRIPTION);
    }

    public void setDesc(String desc) {
        if (desc != null) {
            properties.put(DESCRIPTION, desc);
        }
    }

    public String getStartDate() {
        return properties.get(START_DATE);
    }

    public void setStartDate(Date startDate) {
        if (startDate != null) {
            properties.put(START_DATE, DateUtils.dateToString(startDate));
        }
    }

    public String getEndDate() {
        return properties.get(END_DATE);
    }

    public void setEndDate(Date startDate) {
        if (startDate != null) {
            properties.put(END_DATE, DateUtils.dateToString(startDate));
        }
    }

    public String getSubType() {
        return properties.get(SUB_TYPE);
    }

    public void setSubType(String subType) {
        if (subType != null) {
            properties.put(SUB_TYPE, subType);
        }
    }

    public Boolean isShared() {
        String isShared = properties.get(IS_SHARED);
        if (isShared != null) {
            return Boolean.valueOf(isShared);
        }
        return DEFAULT_IS_SHARED_VALUE;
    }

    public void setShared(Boolean isShared) {
        if (isShared != null) {
            properties.put(IS_SHARED, String.valueOf(isShared));
        }
    }

    public Long getId() {
        String gid = properties.get(ID);
        if (gid != null && isNumeric(gid)) {
            return Long.valueOf(gid);
        }
        return null;
    }

    public void setId(Long gid) {
        properties.put(ID, String.valueOf(gid));
    }

}
