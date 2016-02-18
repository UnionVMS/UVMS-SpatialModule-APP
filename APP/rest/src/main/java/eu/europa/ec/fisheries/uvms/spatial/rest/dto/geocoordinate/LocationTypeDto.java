package eu.europa.ec.fisheries.uvms.spatial.rest.dto.geocoordinate;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.geocoordinate.GeoCoordinateDto;
import org.hibernate.validator.constraints.NotEmpty;

public class LocationTypeDto extends GeoCoordinateDto implements Serializable {
	
	private static final long serialVersionUID = 6434161134531651548L;

	@NotNull
	@NotEmpty
	private String locationType;

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
}
