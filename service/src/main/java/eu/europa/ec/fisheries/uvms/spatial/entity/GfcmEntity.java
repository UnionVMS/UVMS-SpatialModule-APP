package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import org.hibernate.annotations.Where;

@Entity
//@SqlResultSetMappings({
//	@SqlResultSetMapping(name = "implicit.gfcm", entities = @EntityResult(entityClass = GfcmEntity.class))
//})
//@NamedNativeQuery(
//		name = QueryNameConstants.GFCM_BY_COORDINATE,
//		query = "select * from gfcm where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) and enabled = 'Y'", resultSetMapping = "implicit.gfcm")
@Where(clause = "enabled = 'Y'")
@Table(name = "gfcm", schema = "spatial")
public class GfcmEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502864L;

	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnAliasName(aliasName="gid")
	private long gid;

	public GfcmEntity() {
	}

	public long getGid() {
		return this.gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

}
