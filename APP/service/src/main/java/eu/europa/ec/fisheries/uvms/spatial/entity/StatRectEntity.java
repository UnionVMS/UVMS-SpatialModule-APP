package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "stat_rect")
@NamedQueries({
        @NamedQuery(name = StatRectEntity.DISABLE, query = "UPDATE StatRectEntity SET enabled = 'N'"),
        @NamedQuery(name = StatRectEntity.BY_INTERSECT,
                query = "FROM StatRectEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'")
})
//@SequenceGenerator(name = "default_gen", sequenceName = "stat_rect_seq", allocationSize = 1)
public class StatRectEntity extends BaseSpatialEntity {

    public static final String BY_INTERSECT = "statRectEntity.byIntersect";
    public static final String DISABLE = "statRectEntity.disable";

    public StatRectEntity() {
        // why JPA why
    }

    public StatRectEntity(Map<String, Object> values) throws ServiceException {
        super(values);
    }
}
