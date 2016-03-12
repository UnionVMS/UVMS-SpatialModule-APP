package eu.europa.ec.fisheries.uvms.spatial.model.area;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemAreaDto {

    @JsonProperty("gid")
    private Integer gid;
    @JsonProperty("code")
    private String code;
    @JsonProperty("areaType")
    private String areaType;
    @JsonProperty("extent")
    private String extent;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     *
     */
    public SystemAreaDto() {
    }

    /**
     *
     * @param extent
     * @param areaType
     * @param name
     * @param gid
     * @param code
     */
    public SystemAreaDto(Integer gid, String code, String areaType, String extent, String name) {
        this.gid = gid;
        this.code = code;
        this.areaType = areaType;
        this.extent = extent;
        this.name = name;
    }

    /**
     *
     * @return
     * The gid
     */
    @JsonProperty("gid")
    public long getGid() {
        return gid;
    }

    /**
     *
     * @param gid
     * The gid
     */
    @JsonProperty("gid")
    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public SystemAreaDto withGid(Integer gid) {
        this.gid = gid;
        return this;
    }

    /**
     *
     * @return
     * The code
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    public SystemAreaDto withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     *
     * @return
     * The areaType
     */
    @JsonProperty("areaType")
    public String getAreaType() {
        return areaType;
    }

    /**
     *
     * @param areaType
     * The areaType
     */
    @JsonProperty("areaType")
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public SystemAreaDto withAreaType(String areaType) {
        this.areaType = areaType;
        return this;
    }

    /**
     *
     * @return
     * The extent
     */
    @JsonProperty("extent")
    public String getExtent() {
        return extent;
    }

    /**
     *
     * @param extent
     * The extent
     */
    @JsonProperty("extent")
    public void setExtent(String extent) {
        this.extent = extent;
    }

    public SystemAreaDto withExtent(String extent) {
        this.extent = extent;
        return this;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public SystemAreaDto withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public SystemAreaDto withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(gid).append(code).append(areaType).append(extent).append(name).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SystemAreaDto) == false) {
            return false;
        }
        SystemAreaDto rhs = ((SystemAreaDto) other);
        return new EqualsBuilder().append(gid, rhs.gid).append(code, rhs.code).append(areaType, rhs.areaType).append(extent, rhs.extent).append(name, rhs.name).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}