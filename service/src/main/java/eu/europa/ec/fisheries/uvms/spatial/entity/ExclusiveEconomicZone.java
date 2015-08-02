package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * //TODO create test
 */
@Entity
@Table(name = "eez", schema = "spatial")
@Data
@EqualsAndHashCode
@ToString
public class ExclusiveEconomicZone implements Serializable {

    @Id
    @Column(name = "gid")
    private Integer gid;

    @Column(name = "geom")
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Geometry geometry;

    private String eez;
    private String country;
    private String remarks;

    @Column(name = "sov_id")
    private Integer sovId;

    @Column(name = "eez_id")
    private Integer eezId;

    @Column(name = "iso_3digit")
    private String iso3Digit;

}
