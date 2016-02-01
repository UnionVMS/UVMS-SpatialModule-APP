package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserAreaLayerDto extends AreaLayerDto implements Serializable {

	private static final long serialVersionUID = 3751017881001167114L;
	
	@JsonInclude
	private List<Long> idList = new ArrayList<Long>();
	
	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
}
