package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

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

	public AreaLayerDto() {
	}
	
	public AreaLayerDto(String typeName, String areaTypeDesc, String geoName, String serviceUrl, String serviceType, String style, Boolean isInternal) {
		super();
		this.typeName = typeName;
		this.areaTypeDesc = areaTypeDesc;
		this.geoName = geoName;
		this.serviceUrl = serviceUrl;
		this.serviceType = serviceType;
		this.style = style;
		this.isInternal = isInternal;
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
}
