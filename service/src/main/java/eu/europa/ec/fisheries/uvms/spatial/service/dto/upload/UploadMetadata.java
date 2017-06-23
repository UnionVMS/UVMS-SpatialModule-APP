/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dto.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadMetadata {

    @JsonProperty("domain")
    @Valid
    private List<UploadProperty> domain = new ArrayList<>();
    @JsonProperty("file")
    @Valid
    private List<UploadProperty> file = new ArrayList<>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public UploadMetadata() {
    }

    /**
     * 
     * @param domain
     * @param file
     */
    public UploadMetadata(List<UploadProperty> domain, List<UploadProperty> file) {
        this.domain = domain;
        this.file = file;
    }

    /**
     * 
     * @return
     *     The domain
     */
    @JsonProperty("domain")
    public List<UploadProperty> getDomain() {
        return domain;
    }

    /**
     * 
     * @param domain
     *     The database-properties
     */
    @JsonProperty("domain")
    public void setDomain(List<UploadProperty> domain) {
        this.domain = domain;
    }

    public UploadMetadata withDatabaseProperties(List<UploadProperty> databaseProperties) {
        this.domain = databaseProperties;
        return this;
    }

    /**
     * 
     * @return
     *     The file
     */
    @JsonProperty("file")
    public List<UploadProperty> getFile() {
        return file;
    }

    /**
     * 
     * @param file
     *     The file
     */
    @JsonProperty("file")
    public void setFile(List<UploadProperty> file) {
        this.file = file;
    }

    public UploadMetadata withFileProperties(List<UploadProperty> file) {
        this.file = file;
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

    public UploadMetadata withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(domain).append(file).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UploadMetadata)) {
            return false;
        }
        UploadMetadata rhs = ((UploadMetadata) other);
        return new EqualsBuilder().append(domain, rhs.domain).append(file, rhs.file).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}