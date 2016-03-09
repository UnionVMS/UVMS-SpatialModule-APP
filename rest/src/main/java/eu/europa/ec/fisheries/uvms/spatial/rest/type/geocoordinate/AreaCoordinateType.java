package eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class AreaCoordinateType extends GeoCoordinateType {

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
