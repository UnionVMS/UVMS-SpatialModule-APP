package eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class LocationCoordinateType extends GeoCoordinateType implements Serializable {
	
	private static final long serialVersionUID = 6434161134531651548L;

	@NotNull
	@NotEmpty
	private String locationType;

	@NotNull
	private Boolean isGeom = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public Boolean getIsGeom() {
		return isGeom;
	}

	public void setIsGeom(Boolean isGeom) {
		this.isGeom = isGeom;
	}
}
