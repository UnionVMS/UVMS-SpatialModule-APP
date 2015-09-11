package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.Geometry;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;

@Entity
@Table(name = "ports", schema = "spatial")
public class PortsEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502865L;
	
	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnAliasName(aliasName="gid")
	private long gid;
	
	@Column(name = "geom", nullable = false)
	@ColumnAliasName(aliasName="geom")
	private Geometry geom;
	
	@Column(name = "scalerank")
	@ColumnAliasName(aliasName="scalerank")
	private Integer scalerank;
	
	@Column(name = "featurecla", length = 80)
	@ColumnAliasName(aliasName="featurecla")
	private String featurecla;
	
	@Column(name = "name", length = 50)
	@ColumnAliasName(aliasName="name")
	private String name;
	
	@Column(name = "website", length = 254)
	@ColumnAliasName(aliasName="website")
	private String website;
	
	@Column(name = "natlscale")
	@ColumnAliasName(aliasName="natlscale")
	private Double natlscale;

	public PortsEntity() {
	}

	public long getGid() {
		return this.gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public Geometry getGeom() {
		return geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	public Integer getScalerRank() {
		return scalerank;
	}

	public void setScalerRank(Integer scalerRank) {
		this.scalerank = scalerRank;
	}

	public String getFeaturecla() {
		return featurecla;
	}

	public void setFeaturecla(String featurecla) {
		this.featurecla = featurecla;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Double getNatlscale() {
		return natlscale;
	}

	public void setNatlscale(Double natlscale) {
		this.natlscale = natlscale;
	}
}
