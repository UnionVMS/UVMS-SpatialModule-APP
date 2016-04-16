package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
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
        @NamedQuery(name = PortEntity.DISABLE, query = "UPDATE PortEntity SET enabled = 'N'")
})
@Table(name = "port", schema = "spatial")
public class PortEntity extends BaseAreaEntity {

    public static final String PORT_BY_COORDINATE = "portEntity.ByCoordinate";
    public static final String DISABLE = "portsEntity.disable";
    private static final String COUNTRY_CO = "country_co";
    private static final String FISHING_PO = "fishing_po";
    private static final String LANDING_PL = "landing_pl";
    private static final String COMMERCIAL = "commercial";

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

    public PortEntity(List<Property> properties) throws UnsupportedEncodingException {
        Map<String, Object> values = createAttributesMap(properties);
        setGeom((Geometry) values.get(THE_GEOM));
        setCode(readStringProperty(values, CODE));
        setName(readStringProperty(values, NAME));
        setCountryCode(readStringProperty(values, COUNTRY_CO));
        setFishingPort(readStringProperty(values, FISHING_PO));
        setLandingPlace(readStringProperty(values, LANDING_PL));
        setCommercialPort(readStringProperty(values, COMMERCIAL));
        setEnabled(true);
        setEnabledOn(new Date());
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
