package eu.europa.ec.fisheries.uvms.spatial.rest.type;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class AreaFilterType extends FilterType implements Serializable {

	private static final long serialVersionUID = -7738213219041918102L;
	
	@NotNull
	@NotEmpty
	private String areaType;
	
	public AreaFilterType() {
		super();
	}

	public AreaFilterType(String areaType, String filter) {
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
