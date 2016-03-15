package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;

// FIXME DEAD CODE also tables is db

public class CustomAreasEntity implements Serializable {  // This is not an entity

	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnAliasName(aliasName ="gid")
	private long gid;

    @Column(name = "geom", nullable = false)
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName ="geometry")
	private Geometry geom;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customAreas", cascade = CascadeType.ALL)
    private Set<CustomAreaPropertiesEntity> customAreaProperties;

	public long getGid() {
		return gid;
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

	public Set<CustomAreaPropertiesEntity> getCustomAreaProperties() {
		return customAreaProperties;
	}

	public void setCustomAreaProperties(Set<CustomAreaPropertiesEntity> customAreaProperties) {
		this.customAreaProperties = customAreaProperties;
	}
}
