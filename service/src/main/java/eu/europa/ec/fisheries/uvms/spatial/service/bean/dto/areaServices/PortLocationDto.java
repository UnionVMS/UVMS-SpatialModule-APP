package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices;

import com.vividsolutions.jts.geom.Point;

import java.util.Date;

public class PortLocationDto {
    private String name;
    private String code;
    private Point geometry;
    private String countryCode;
    private String fishingPort;
    private String landingPlace;
    private String commercialPort;
    private Boolean enabled;
    private Date enabledOn;

    public Point getGeometry() {
        return geometry;
    }

    public void setGeometry(Point geometry) {
        this.geometry = geometry;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getEnabledOn() {
        return enabledOn;
    }

    public void setEnabledOn(Date enabledOn) {
        this.enabledOn = enabledOn;
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
