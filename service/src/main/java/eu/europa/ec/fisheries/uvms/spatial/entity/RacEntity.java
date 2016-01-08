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

@Entity
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "implicit.rac", entities = @EntityResult(entityClass = RacEntity.class))
})
@NamedNativeQuery(
		name = QueryNameConstants.RAC_BY_COORDINATE, 
		query = "select * from rac where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs))", resultSetMapping = "implicit.rac")

@Table(name = "rac", schema = "spatial")
public class RacEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502867L;
	
	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnAliasName(aliasName="gid")
	private long gid;

	public RacEntity() {
	}

	public long getGid() {
		return this.gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}
}
