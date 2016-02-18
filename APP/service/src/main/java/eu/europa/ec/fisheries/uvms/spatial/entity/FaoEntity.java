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
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "implicit.fao", entities = @EntityResult(entityClass = FaoEntity.class))
})
@NamedNativeQuery(
		name = QueryNameConstants.FAO_BY_COORDINATE, 
		query = "select * from fao where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) and enabled = 'Y'", resultSetMapping = "implicit.fao")
@Where(clause = "enabled = 'Y'")
@Table(name = "fao", schema = "spatial")
public class FaoEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502863L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnAliasName(aliasName="id")
	private long id;

	public FaoEntity() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
