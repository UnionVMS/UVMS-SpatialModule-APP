package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 1/11/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaDto {

    @JsonProperty("gid")
    private Long gid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("desc")
    private String desc;

    public AreaDto() {

    }

    public AreaDto(Long gid, String name, String desc) {
        this.gid = gid;
        this.name = name;
        this.desc = desc;
    }

    @JsonProperty("gid")
    public Long getGid() {
        return gid;
    }

    @JsonProperty("gid")
    public void setGid(Long gid) {
        this.gid = gid;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
