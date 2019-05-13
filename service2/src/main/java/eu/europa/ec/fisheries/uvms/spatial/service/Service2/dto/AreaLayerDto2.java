package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto;

import java.util.ArrayList;
import java.util.List;

public class AreaLayerDto2 {

    private String typeName;

    private String areaTypeDesc;

    private String geoName;

    private String serviceType;

    private String style;

    private List<BaseAreaDto> idList;

    public AreaLayerDto2(String typeName, String geoName, String areaTypeDesc, String style, String serviceType) {
        this.typeName = typeName;
        this.areaTypeDesc = areaTypeDesc;
        this.geoName = geoName;
        this.serviceType = serviceType;
        this.style = style;
    }


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAreaTypeDesc() {
        return areaTypeDesc;
    }

    public void setAreaTypeDesc(String areaTypeDesc) {
        this.areaTypeDesc = areaTypeDesc;
    }

    public String getGeoName() {
        return geoName;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<BaseAreaDto> getIdList() {
        if(idList == null){
            idList = new ArrayList<>();
        }
        return idList;
    }

    public void setIdList(List<BaseAreaDto> idList) {
        this.idList = idList;
    }
}
