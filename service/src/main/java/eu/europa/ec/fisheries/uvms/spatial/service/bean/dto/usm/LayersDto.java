package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayersDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("serviceLayerId")
    private String serviceLayerId;

    public LayersDto() {}

    public LayersDto(String name, String serviceLayerId) {
        this.name = name;
        this.serviceLayerId = serviceLayerId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("serviceLayerId")
    public String getServiceLayerId() {
        return serviceLayerId;
    }

    @JsonProperty("serviceLayerId")
    public void setServiceLayerId(String serviceLayerId) {
        this.serviceLayerId = serviceLayerId;
    }

    @Override
    public String toString() {
        return "ClassPojo [type = " + name + ", serviceLayerId = " + serviceLayerId + "]";
    }
}

