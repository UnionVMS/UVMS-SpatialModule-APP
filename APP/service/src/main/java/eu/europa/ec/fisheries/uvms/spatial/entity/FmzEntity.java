package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import java.util.Date;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity
@Table(name = "fmz")
@NamedQueries({
        @NamedQuery(name = FmzEntity.DISABLE, query = "UPDATE FmzEntity SET enabled = 'N'"),
        @NamedQuery(name = FmzEntity.BY_INTERSECT,
                query = "FROM FaoEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'")
})
public class FmzEntity extends BaseSpatialEntity {

    public static final String DISABLE = "fmzEntity.disable";
    public static final String BY_INTERSECT = "fmzEntity.byIntersect";
    private static final String FMZ_ID = "fmzId";
    private static final String EDITED = "edited";
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    //@Column(name = "fmz_id")
    //@ColumnAliasName(aliasName = "fmzId")
    //private Long fmzId;

    //@Temporal(TemporalType.DATE)
    //@Column(name = "edited")
    //private Date edited;

    public FmzEntity() {
        // why JPA why
    }

    public FmzEntity(Map<String, Object> values) throws ServiceException {
        super(values);
      //  String fmzId = readStringProperty(values, "f");
      //  if (fmzId != null){
      //      this.fmzId = Long.valueOf(fmzId);
      //  }
      //  String edited = readStringProperty(values, EDITED);
      //  if (edited != null){
      //      DateTime dateTime = DATE_TIME_FORMATTER.parseDateTime(edited);
      //      this.edited = dateTime.toDate();
      //  }
    }

    /*
    public Long getFmzId() {
        return fmzId;
    }

    public void setFmzId(Long fmzId) {
        this.fmzId = fmzId;
    }

    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }
    */
}
