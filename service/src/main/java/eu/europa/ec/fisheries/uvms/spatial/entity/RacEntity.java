package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "rac")
@NamedQueries({
        @NamedQuery(name = RacEntity.DISABLE_RAC, query = "UPDATE RacEntity SET enabled = 'N'"),
        @NamedQuery(name = RacEntity.RAC_BY_INTERSECT, query = "FROM RacEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
})
public class RacEntity extends BaseSpatialEntity {

    public static final String RAC_BY_INTERSECT = "racEntity.byIntersect";
    public static final String DISABLE_RAC = "racEntity.disable";

    public RacEntity() {
        // why JPA why
    }

    public RacEntity(Map<String, Object> values) throws ServiceException {
        super(values);
    }
}
