package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "rfmo", schema = "spatial")
public class RfmoEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502870L;
	
	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int gid;
	
    @Basic
    @Column(name = "geom")
    @Type(type = "org.hibernate.spatial.GeometryType")
	private Geometry geom;
    
    @Column(name = "rfmo", length = 10)
	private String rfmo;
    
    @Column(name = "name", length = 125)
	private String name;
    
    @Column(name = "tuna", length = 10)
	private String tuna;

	public RfmoEntity() {
	}

	public int getGid() {
		return this.gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public Geometry getGeom() {
		return this.geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	public String getRfmo() {
		return this.rfmo;
	}

	public void setRfmo(String rfmo) {
		this.rfmo = rfmo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTuna() {
		return this.tuna;
	}

	public void setTuna(String tuna) {
		this.tuna = tuna;
	}

}
