package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class FilterDto implements Serializable {
	
	private static final long serialVersionUID = -3027324059511336094L;
	
	@NotNull
	@NotEmpty
	protected String filter;
	
	public FilterDto() {
	}
	
	public FilterDto(String filter) {
		this.filter = filter;
	}
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

}
