package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.ScriptAssert;

@ScriptAssert(script="_this.isInputValid(_this.id,_this.crs,_this.longitude,_this.latitude)", lang = "javascript")
public class GeoCoordinateDto implements Serializable {
	
	private static final long serialVersionUID = 4875764412505175274L;

	protected String id;
	
	protected Double longitude;
	
	protected Double latitude;
	
	protected Integer crs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
