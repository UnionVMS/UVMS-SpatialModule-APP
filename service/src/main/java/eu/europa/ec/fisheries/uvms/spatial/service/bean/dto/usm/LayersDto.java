package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayersDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("serviceLayerId")
    private String serviceLayerId;

    public LayersDto() {}

    public LayersDto(String type, String serviceLayerId) {
        this.type = type;
        this.serviceLayerId = serviceLayerId;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
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
        return "ClassPojo [type = " + type + ", serviceLayerId = " + serviceLayerId + "]";
    }
}

