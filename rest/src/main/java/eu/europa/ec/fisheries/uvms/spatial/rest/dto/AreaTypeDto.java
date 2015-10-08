package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class AreaTypeDto extends GeoCoordinateDto implements Serializable {

	private static final long serialVersionUID = -7838777696394872946L;

	@NotNull
	@NotEmpty
	private String areaType;
	
	@NotNull
	private Boolean isGeom = false;
	
	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public Boolean getIsGeom() {
		return isGeom;
	}

	public void setIsGeom(Boolean isGeom) {
		this.isGeom = isGeom;
	}
}
