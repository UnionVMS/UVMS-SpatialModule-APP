package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaGroup;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;

import java.util.List;

/**
 * Created by padhyad on 1/18/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaGroupDto {

    @JsonProperty("groupId")
    private Long groupId;

    @JsonProperty("groupName")
    private String groupName;

    @JsonProperty("groupDescription")
    private String groupDesc;

    @JsonProperty("areas")
    private List<AreaDetailsDto> areaDetailsDtos;

    @JsonProperty("groupId")
    public Long getGroupId() {
        return groupId;
    }

    @JsonProperty("groupId")
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("groupName")
    public String getGroupName() {
        return groupName;
    }

    @JsonProperty("groupName")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @JsonProperty("groupDescription")
    public String getGroupDesc() {
        return groupDesc;
    }

    @JsonProperty("groupDescription")
    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    @JsonProperty("areas")
    public List<AreaDetailsDto> getAreaDetailsDtos() {
        return areaDetailsDtos;
    }

    @JsonProperty("areas")
    public void setAreaDetailsDtos(List<AreaDetailsDto> areaDetailsDtos) {
        this.areaDetailsDtos = areaDetailsDtos;
    }
}