package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;

public class UserAreaDto implements Serializable {

	private static final long serialVersionUID = 754300599619875100L;

	private String areaType = "USERAREA";
	
	private Number gid;

	private String areaName;

	private String areaDesc;

	private String geomExtent;

	private Geometry geometry;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaDesc() {
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	public String getGeomExtent() {
		return geomExtent;
	}

	public void setGeomExtent(String geomExtent) {
		this.geomExtent = geomExtent;
	}
}
