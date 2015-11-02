package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import java.io.Serializable;

public class UserAreaDto implements Serializable {

	private static final long serialVersionUID = 754300599619875100L;

	private String areaType = "USERAREA";
	
	private Number gid;
	
	private String name;
	
	private String desc;
	
	private String extent;

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public Number getGid() {
		return gid;
	}

	public void setGid(Number gid) {
		this.gid = gid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}
}
