package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import eu.europa.ec.fisheries.wsdl.vessel.types.CarrierSource;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselHistoryId;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;

import java.math.BigDecimal;
import java.math.BigInteger;

public class VesselDto {

    private VesselId vesselId;
    private boolean active;
    private CarrierSource source;
    private VesselHistoryId eventHistory;
    private String name;
    private String countryCode;
    private String vesselType;
    private boolean hasIrcs;
    private String ircs;
    private String externalMarking;
    private String cfr;
    private BigInteger imo;
    private String mmsiNo;
    private String billing;
    private boolean hasLicense;
    private String homePort;
    private BigDecimal lengthOverAll;
    private BigDecimal lengthBetweenPerpendiculars;
    private BigDecimal grossTonnage;
    private BigDecimal otherGrossTonnage;
    private BigDecimal safetyGrossTonnage;
    private BigDecimal powerMain;
    private BigDecimal powerAux;

    public VesselId getVesselId() {
        return vesselId;
    }

    public void setVesselId(VesselId vesselId) {
        this.vesselId = vesselId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CarrierSource getSource() {
        return source;
    }

    public void setSource(CarrierSource source) {
        this.source = source;
    }

    public VesselHistoryId getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(VesselHistoryId eventHistory) {
        this.eventHistory = eventHistory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getVesselType() {
        return vesselType;
    }

    public void setVesselType(String vesselType) {
        this.vesselType = vesselType;
    }

    public boolean isHasIrcs() {
        return hasIrcs;
    }

    public void setHasIrcs(boolean hasIrcs) {
        this.hasIrcs = hasIrcs;
    }

    public String getIrcs() {
        return ircs;
    }

    public void setIrcs(String ircs) {
        this.ircs = ircs;
    }

    public String getExternalMarking() {
        return externalMarking;
    }

    public void setExternalMarking(String externalMarking) {
        this.externalMarking = externalMarking;
    }

    public String getCfr() {
        return cfr;
    }

    public void setCfr(String cfr) {
        this.cfr = cfr;
    }

    public BigInteger getImo() {
        return imo;
    }

    public void setImo(BigInteger imo) {
        this.imo = imo;
    }

    public String getMmsiNo() {
        return mmsiNo;
    }

    public void setMmsiNo(String mmsiNo) {
        this.mmsiNo = mmsiNo;
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public boolean isHasLicense() {
        return hasLicense;
    }

    public void setHasLicense(boolean hasLicense) {
        this.hasLicense = hasLicense;
    }

    public String getHomePort() {
        return homePort;
    }

    public void setHomePort(String homePort) {
        this.homePort = homePort;
    }

    public BigDecimal getLengthOverAll() {
        return lengthOverAll;
    }

    public void setLengthOverAll(BigDecimal lengthOverAll) {
        this.lengthOverAll = lengthOverAll;
    }

    public BigDecimal getLengthBetweenPerpendiculars() {
        return lengthBetweenPerpendiculars;
    }

    public void setLengthBetweenPerpendiculars(BigDecimal lengthBetweenPerpendiculars) {
        this.lengthBetweenPerpendiculars = lengthBetweenPerpendiculars;
    }

    public BigDecimal getGrossTonnage() {
        return grossTonnage;
    }

    public void setGrossTonnage(BigDecimal grossTonnage) {
        this.grossTonnage = grossTonnage;
    }

    public BigDecimal getOtherGrossTonnage() {
        return otherGrossTonnage;
    }

    public void setOtherGrossTonnage(BigDecimal otherGrossTonnage) {
        this.otherGrossTonnage = otherGrossTonnage;
    }

    public BigDecimal getSafetyGrossTonnage() {
        return safetyGrossTonnage;
    }

    public void setSafetyGrossTonnage(BigDecimal safetyGrossTonnage) {
        this.safetyGrossTonnage = safetyGrossTonnage;
    }

    public BigDecimal getPowerMain() {
        return powerMain;
    }

    public void setPowerMain(BigDecimal powerMain) {
        this.powerMain = powerMain;
    }

    public BigDecimal getPowerAux() {
        return powerAux;
    }

    public void setPowerAux(BigDecimal powerAux) {
        this.powerAux = powerAux;
    }
}
