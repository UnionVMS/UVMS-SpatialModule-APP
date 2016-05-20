package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "gfcm")
@NamedQueries({
        @NamedQuery(name = GfcmEntity.DISABLE_GFMC_AREAS, query = "UPDATE GfcmEntity SET enabled = 'N'"),
        @NamedQuery(name = GfcmEntity.GFMC_BY_INTERSECT,
                query = "FROM GfcmEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'")
})
public class GfcmEntity extends BaseSpatialEntity {

    public static final String DISABLE_GFMC_AREAS = "gfmcEntity.disable";
    public static final String GFMC_BY_INTERSECT = "gfmcEntity.gfmcByIntersect";

	public GfcmEntity() {
        // why JPA why
    }

    public GfcmEntity(Map<String, Object> values) throws ServiceException {
        super(values);
    }
}
