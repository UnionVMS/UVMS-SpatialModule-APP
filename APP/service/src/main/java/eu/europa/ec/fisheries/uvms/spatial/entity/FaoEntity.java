package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fao")
@NamedQueries({
        @NamedQuery(name = FaoEntity.DISABLE_FAO_AREAS, query = "UPDATE FaoEntity SET enabled = 'N'"),
        @NamedQuery(name = FaoEntity.FAO_BY_INTERSECT,
                query = "FROM FaoEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'")
})
//@SequenceGenerator(name = "default_gen", sequenceName = "fao_seq", allocationSize = 1)
public class FaoEntity extends BaseSpatialEntity {

    public static final String DISABLE_FAO_AREAS = "faoEntity.disableFaoAreas";
    public static final String FAO_BY_INTERSECT = "faoEntity.faoByIntersect";

    public FaoEntity() {
        // why JPA why
    }

    public FaoEntity(Map<String, Object> values) throws ServiceException {
        super(values);
    }
}
