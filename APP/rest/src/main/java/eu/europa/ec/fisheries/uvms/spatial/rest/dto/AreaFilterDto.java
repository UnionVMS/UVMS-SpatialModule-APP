package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class AreaFilterDto extends FilterDto implements Serializable {

	private static final long serialVersionUID = -7738213219041918102L;
	
	@NotNull
	@NotEmpty
	private String areaType;
	
	public AreaFilterDto() {
		super();
	}

	public AreaFilterDto(String areaType, String filter) {
		super(filter);
		this.areaType = areaType;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
}
