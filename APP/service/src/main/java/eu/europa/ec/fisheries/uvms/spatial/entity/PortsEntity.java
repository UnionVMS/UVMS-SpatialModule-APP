package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.GeoJsonDto;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "implicit.port", entities = @EntityResult(entityClass = PortsEntity.class))
})
@NamedQueries({
        @NamedQuery(name = QueryNameConstants.FIND_PORT_AREA_BY_ID,
                query = "SELECT port FROM PortsEntity port WHERE port.gid = :portAreaId")
})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = QueryNameConstants.PORT_BY_COORDINATE,
                query = "select * from port order by ST_Distance_Spheroid(geom, st_geomfromtext(CAST(:wktPoint as text), :crs), 'SPHEROID[\"WGS 84\",6378137,298.257223563]') limit 1"
                , resultSetMapping = "implicit.port"),
        @NamedNativeQuery(
                name = QueryNameConstants.PORTAREA_BY_COORDINATE,
                query = "select * from port where st_intersects(area_geom, st_geomfromtext(CAST(:wktPoint as text), :crs))"
                , resultSetMapping = "implicit.port")
})
@Table(name = "port", schema = "spatial")
public class PortsEntity implements Serializable {

    private static final long serialVersionUID = -2233177907262739920L;

    @Id
    @Column(name = "gid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnAliasName(aliasName = "gid")
    private long gid;

    @Column(name = "geom", nullable = false)
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName = GeoJsonDto.GEOMETRY)
    private Geometry geom;

    @Column(name = "area_geom", nullable = true)
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName = GeoJsonDto.AREA_GEOMETRY)
    private Geometry areaGeom;

    @Column(name = "country_code", length = 3)
    @ColumnAliasName(aliasName = "countrycode")
    private String countryCode;

    @Column(name = "code", length = 10)
    @ColumnAliasName(aliasName = "code")
    private String code;

    @Column(name = "name", length = 100)
    @ColumnAliasName(aliasName = "name")
    private String name;

    @Column(name = "fishing_port", length = 1)
    @ColumnAliasName(aliasName = "fishingport")
    private String fishingPort;

    @Column(name = "landing_place")
    @ColumnAliasName(aliasName = "landingplace")
    private String landingPlace;

    @Column(name = "commercial_port")
    @ColumnAliasName(aliasName = "commercialport")
    private String commercialPort;

    public PortsEntity() {
    }

    public long getGid() {
        return this.gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public Geometry getAreaGeom() {
        return areaGeom;
    }

    public void setAreaGeom(Geometry areaGeom) {
        this.areaGeom = areaGeom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFishingPort() {
        return fishingPort;
    }

    public void setFishingPort(String fishingPort) {
        this.fishingPort = fishingPort;
    }

    public String getLandingPlace() {
        return landingPlace;
    }

    public void setLandingPlace(String landingPlace) {
        this.landingPlace = landingPlace;
    }

    public String getCommercialPort() {
        return commercialPort;
    }

    public void setCommercialPort(String commercialPort) {
        this.commercialPort = commercialPort;
    }
}
