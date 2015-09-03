package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;

@Entity
@Table(name = "gfcm", schema = "spatial")
public class GfcmEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502864L;

	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnAliasName(aliasName="gid")
	private int gid;

	public GfcmEntity() {
	}

	public int getGid() {
		return this.gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

}
