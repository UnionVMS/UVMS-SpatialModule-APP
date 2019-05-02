/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.area;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaLayerDto implements Serializable {

	private static final long serialVersionUID = 1751448380717332660L;

	protected String typeName;
	
	protected String areaTypeDesc;
	
	protected String geoName;
	
	protected String serviceUrl;
	
	protected String serviceType;

	protected String style;

	protected Boolean isInternal;

	protected Boolean isLocation = false;

	public AreaLayerDto() {
	}
	
	public AreaLayerDto(String typeName, String areaTypeDesc, String geoName, String serviceUrl, String serviceType, String style, Boolean isInternal, Boolean isLocation) {
		super();
		this.typeName = typeName;
		this.areaTypeDesc = areaTypeDesc;
		this.geoName = geoName;
		this.serviceUrl = serviceUrl;
		this.serviceType = serviceType;
		this.style = style;
		this.isInternal = isInternal;
		this.isLocation = isLocation;
	}

	@JsonProperty("style")
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@JsonProperty("typeName")
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@JsonProperty("areaTypeDesc")
	public String getAreaTypeDesc() {
		return areaTypeDesc;
	}

	public void setAreaTypeDesc(String areaTypeDesc) {
		this.areaTypeDesc = areaTypeDesc;
	}

	@JsonProperty("geoName")
	public String getGeoName() {
		return geoName;
	}

	public void setGeoName(String geoName) {
		this.geoName = geoName;
	}

	@JsonProperty("serviceUrl")
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@JsonProperty("serviceType")
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@JsonProperty("isInternal")
	public Boolean getIsInternal() {
		return isInternal;
	}

	public void setIsInternal(Boolean isInternal) {
		this.isInternal = isInternal;
	}

	@JsonProperty("isLocation")
	public Boolean getIsLocation() {
		return isLocation;
	}

	public void setIsLocation(Boolean isLocation) {
		this.isLocation = isLocation;
	}
}