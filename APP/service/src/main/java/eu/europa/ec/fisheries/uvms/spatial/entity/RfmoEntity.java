package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import org.hibernate.annotations.Where;
import org.opengis.feature.Property;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = RfmoEntity.RFMO_BY_COORDINATE,
                query = "FROM RfmoEntity WHERE intersects(geom, :shape) = true) AND enabled = 'Y'"),
        @NamedQuery(name = QueryNameConstants.RFMO_COLUMNS,
                query = "SELECT rfmo.name AS name, rfmo.code AS code FROM RfmoEntity AS rfmo WHERE rfmo.gid =:gid"),
        @NamedQuery(name = RfmoEntity.DISABLE_RFMO_AREAS,
                query = "UPDATE RfmoEntity SET enabled = 'N'")
})
@Where(clause = "enabled = 'Y'")
@Table(name = "rfmo", schema = "spatial")
public class RfmoEntity extends BaseAreaEntity {

    public static final String RFMO_BY_COORDINATE = "rfmoEntity.ByCoordinate";
    public static final String DISABLE_RFMO_AREAS = "rfmoEntity.disableRfmoAreas";
    private static final String TUNA = "tuna";

    @Column(length = 10)
    @ColumnAliasName(aliasName = TUNA)
    private String tuna;

    public RfmoEntity() {
    }

    public RfmoEntity(List<Property> properties) throws UnsupportedEncodingException {
        Map<String, Object> values = createAttributesMap(properties);
        setName(readStringProperty(values, NAME));
        setCode(readStringProperty(values, CODE));
        setTuna(readStringProperty(values, TUNA));
        setEnabled(true);
        setEnabledOn(new Date());
        setGeom((Geometry) values.get(THE_GEOM));
    }

    public String getTuna() {
        return this.tuna;
    }

    public void setTuna(String tuna) {
        this.tuna = tuna;
    }

}
