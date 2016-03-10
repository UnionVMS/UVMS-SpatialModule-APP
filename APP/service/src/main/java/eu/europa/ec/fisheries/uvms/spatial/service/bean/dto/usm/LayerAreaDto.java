package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.util.AreaTypeEnum;

import java.util.List;

/**
 * Created by padhyad on 1/11/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LayerAreaDto extends LayersDto {

    @JsonProperty("areaType")
    private AreaTypeEnum areaType;

    @JsonProperty("gid")
    private Long gid;

    @JsonProperty("areaName")
    private String areaName;

    @JsonProperty("desc")
    private String areaDesc;

    @JsonProperty("areaGroupName")
    private String areaGroupName;

    public LayerAreaDto() {}

    public LayerAreaDto(AreaTypeEnum areaType, String serviceLayerId, Long order) {
        super(serviceLayerId, order);
        this.areaType = areaType;
    }

    @JsonProperty("areaType")
    public AreaTypeEnum getAreaType() {
        return areaType;
    }

    @JsonProperty("areaType")
    public void setAreaType(AreaTypeEnum areaType) {
        this.areaType = areaType;
    }

    @JsonProperty("gid")
    public Long getGid() {
        return gid;
    }

    @JsonProperty("gid")
    public void setGid(Long gid) {
        this.gid = gid;
    }

    @JsonProperty("areaName")
    public String getAreaName() {
        return areaName;
    }

    @JsonProperty("areaName")
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @JsonProperty("desc")
    public String getAreaDesc() {
        return areaDesc;
    }

    @JsonProperty("desc")
    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    @JsonProperty("areaGroupName")
    public String getAreaGroupName() {
        return areaGroupName;
    }

    @JsonProperty("areaGroupName")
    public void setAreaGroupName(String areaGroupName) {
        this.areaGroupName = areaGroupName;
    }

}
