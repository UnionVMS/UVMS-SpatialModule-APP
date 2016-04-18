package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import org.hibernate.annotations.Where;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = PortAreasEntity.PORT_AREA_BY_COORDINATE,
                query = "FROM PortAreasEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = PortAreasEntity.DISABLE_PORT_AREAS, query = "UPDATE PortAreasEntity SET enabled = 'N'")
})
@Where(clause = "enabled = 'Y'")
@Table(name = "port_area", schema = "spatial")
public class PortAreasEntity extends BaseAreaEntity {

    public static final String PORT_AREA_BY_COORDINATE = "portEntity.PortAreaByCoordinate";
    public static final String DISABLE_PORT_AREAS = "portAreasEntity.disablePortAreas";

    public PortAreasEntity() {
    }

    public PortAreasEntity(Map<String, Object> values) throws ServiceException {
        super(values);
    }
}
