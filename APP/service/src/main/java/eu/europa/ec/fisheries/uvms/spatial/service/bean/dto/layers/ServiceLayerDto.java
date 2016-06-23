package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 1/7/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServiceLayerDto {

    @JsonProperty("serviceLayerId")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("layerDesc")
    private String layerDesc;

    @JsonProperty("subType")
    private String subType;

    @JsonIgnore
    private String areaLocationTypeName;

    public ServiceLayerDto() {}

    public ServiceLayerDto(Long id, String name, String layerDesc, String subType) {
        this.id = id;
        this.name = name;
        this.layerDesc = layerDesc;
        this.subType = subType;
    }

    @JsonProperty("serviceLayerId")
    public Long getId() {
        return id;
    }

    @JsonProperty("serviceLayerId")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("layerDesc")
    public String getLayerDesc() {
        return layerDesc;
    }

    @JsonProperty("layerDesc")
    public void setLayerDesc(String layerDesc) {
        this.layerDesc = layerDesc;
    }

    @JsonProperty("subType")
    public String getSubType() {
        return subType;
    }

    @JsonProperty("subType")
    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getAreaLocationTypeName() {
        return areaLocationTypeName;
    }

    public void setAreaLocationTypeName(String areaLocationTypeName) {
        this.areaLocationTypeName = areaLocationTypeName;
    }
}
