package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 1/18/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaGroupTypeDto {

    @JsonProperty("gid")
    private long gid;

    @JsonProperty("areaType")
    private String areaType;

    @JsonProperty("name")
    private String name;

    @JsonProperty("code")
    private String code;

    @JsonProperty("extent")
    private String extent;

    @JsonProperty("gid")
    public long getGid() {
        return gid;
    }

    @JsonProperty("gid")
    public void setGid(long gid) {
        this.gid = gid;
    }

    @JsonProperty("areaType")
    public String getAreaType() {
        return areaType;
    }

    @JsonProperty("areaType")
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("extent")
    public String getExtent() {
        return extent;
    }

    @JsonProperty("extent")
    public void setExtent(String extent) {
        this.extent = extent;
    }
}