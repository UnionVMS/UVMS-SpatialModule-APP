package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices;

import eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum;
import java.io.Serializable;

public class UserAreaDto implements Serializable {

	private String areaType = SpatialTypeEnum.USERAREA.getType();
	private Number gid;
	private String name;
    private String desc;
	private String extent;
    private String owner;

    public UserAreaDto(Number gid, String name, String desc, String extent, String owner) {
        this.owner = owner;
        this.gid = gid;
        this.name = name;
        this.desc = desc;
        this.extent = extent;
    }

    public UserAreaDto() {


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

    public String getOwner() {

        return owner;
    }

    public void setOwner(String owner) {

        this.owner = owner;
    }
}
