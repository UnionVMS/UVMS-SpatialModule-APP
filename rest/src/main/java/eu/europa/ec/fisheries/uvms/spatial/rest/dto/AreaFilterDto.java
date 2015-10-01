package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class AreaFilterDto implements Serializable {

	private static final long serialVersionUID = -7738213219041918102L;
	
	@NotNull
	@NotEmpty
	private String areaType;
	
	@NotNull
	@NotEmpty
	private String filter;
	
	public AreaFilterDto() {
		
	}

	public AreaFilterDto(String areaType, String filter) {
		super();
		this.areaType = areaType;
		this.filter = filter;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
