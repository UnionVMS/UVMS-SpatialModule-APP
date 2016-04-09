package eu.europa.ec.fisheries.uvms.spatial.rest.type;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class FilterType implements Serializable {

	@NotNull
	@NotEmpty
	protected String filter;
	
	public FilterType() {
	}
	
	public FilterType(String filter) {
		this.filter = filter;
	}
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

}
