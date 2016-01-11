package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 1/7/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServiceLayerDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("layerDesc")
    private String layerDesc;

    @JsonProperty("subType")
    private String subType;

    public ServiceLayerDto() {}

    public ServiceLayerDto(Integer id, String name, String layerDesc, String subType) {
        this.id = id;
        this.name = name;
        this.layerDesc = layerDesc;
        this.subType = subType;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
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
}
