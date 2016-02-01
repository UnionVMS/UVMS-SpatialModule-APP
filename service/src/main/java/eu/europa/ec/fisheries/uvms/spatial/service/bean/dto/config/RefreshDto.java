package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 1/13/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RefreshDto {

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("rate")
    private Integer rate;

    public RefreshDto(){}

    public RefreshDto(Boolean status, Integer rate) {
        this.status = status;
        this.rate = rate;
    }

    @JsonProperty("status")
    public Boolean getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @JsonProperty("rate")
    public Integer getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
