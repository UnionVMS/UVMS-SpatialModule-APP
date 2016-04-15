package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
        @NamedQuery(name = PortEntity.DISABLE, query = "UPDATE PortEntity SET enabled = 'N'")
})
@Table(name = "port", schema = "spatial")
public class PortEntity extends BaseAreaEntity {

    public static final String PORT_BY_COORDINATE = "portEntity.ByCoordinate";
    public static final String DISABLE = "portsEntity.disable";

    @Column(name = "country_code", length = 3)
    @ColumnAliasName(aliasName = "countrycode")
    private String countryCode;

    @Column(name = "fishing_port", length = 1)
    @ColumnAliasName(aliasName = "fishingport")
    private String fishingPort;

    @Column(name = "landing_place")
    @ColumnAliasName(aliasName = "landingplace")
    private String landingPlace;

    @Column(name = "commercial_port")
    @ColumnAliasName(aliasName = "commercialport")
    private String commercialPort;

    public PortEntity() {
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
