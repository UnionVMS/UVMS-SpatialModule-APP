package eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate;

import java.io.Serializable;

import org.hibernate.validator.constraints.ScriptAssert;

import com.fasterxml.jackson.annotation.JsonProperty;

@ScriptAssert(script="_this.isInputValid(_this.id,_this.crs,_this.longitude,_this.latitude)", lang = "javascript")
public class GeoCoordinateType implements Serializable {
	
	private static final long serialVersionUID = 4875764412505175274L;
	
	protected String id;
	
	protected Double longitude;
	
	protected Double latitude;
	
	protected Integer crs;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty("gid")
	public String getGid() {
		return id;
	}

	@JsonProperty("gid")
	public void setGid(String gid) {
		this.id = gid;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getCrs() {
		return crs;
	}

	public void setCrs(Integer crs) {
		this.crs = crs;
	}
	
	public boolean isInputValid(Object id, Object crs, Object longitude, Object latitude) {
		if (id != null) {
			return true;
		} else if (crs != null && longitude != null && latitude != null) {
			return true;
		}
		return false;
	}
}
