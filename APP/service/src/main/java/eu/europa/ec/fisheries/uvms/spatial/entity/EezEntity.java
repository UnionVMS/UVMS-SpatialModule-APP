/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import java.util.List;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = EezEntity.EEZ_BY_COORDINATE, query = "FROM EezEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = EezEntity.EEZ_COLUMNS, query = "SELECT eez.id as gid, eez.name AS name, eez.code AS code FROM EezEntity AS eez WHERE eez.id in (:ids)"),
        @NamedQuery(name = EezEntity.DISABLE_EEZ_AREAS, query = "UPDATE EezEntity SET enabled = 'N'"),
        @NamedQuery(name = EezEntity.SEARCH_EEZ, query = "FROM EezEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = EezEntity.SEARCH_EEZ_NAMES_BY_CODE, query = "From EezEntity where code in (SELECT distinct(code) from EezEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)")
})
@Table(name = "eez")
@EqualsAndHashCode(callSuper = true)
public class EezEntity extends BaseAreaEntity {

    public static final String EEZ_BY_COORDINATE = "eezEntity.ByCoordinate";
    public static final String DISABLE_EEZ_AREAS = "eezEntity.disableEezAreas";
    public static final String SEARCH_EEZ = "eezEntity.searchByNameAndCode";
    public static final String SEARCH_EEZ_NAMES_BY_CODE = "eezEntity.searchNameByCode";
    public static final String EEZ_COLUMNS = "eezEntity.findSelectedColumns";

    private static final String COUNTRY = "country";
    private static final String SOVEREIGN = "sovereign";
    private static final String REMARKS = "remarks";
    private static final String SOV_ID = "sov_id";
    private static final String EEZ_ID = "eez_id";
    private static final String MRGID = "mrgid";
    private static final String DATE_CHANG = "date_chang";
    private static final String AREA_M_2 = "area_m2";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String MRGID_EEZ = "mrgid_eez";

    @Column(length = 100)
    @ColumnAliasName(aliasName = COUNTRY)
    private String country;

    @Column(length = 100)
    @ColumnAliasName(aliasName = SOVEREIGN)
    private String sovereign;

    @Column(length = 150)
    @ColumnAliasName(aliasName = REMARKS)
    private String remarks;

    @Column(name = SOV_ID)
    @ColumnAliasName(aliasName = "sovId")
    private Long sovId;

    @Column(name = EEZ_ID)
    @ColumnAliasName(aliasName = "eezId")
    private Long eezId;

    @Column(name = "mrgid")
    @ColumnAliasName(aliasName = "mrgid")
    private BigDecimal mrgid;

    @Column(name = "date_chang", length = 50)
    @ColumnAliasName(aliasName = "dateChang")
    private String dateChang;

    @Column(name = "area_m2")
    @ColumnAliasName(aliasName = "areaM2")
    private Double areaM2;

    @Column(name = LONGITUDE)
    @ColumnAliasName(aliasName = LONGITUDE)
    private Double longitude;

    @Column(name = LATITUDE)
    @ColumnAliasName(aliasName = LATITUDE)
    private Double latitude;

    @Column(name = "mrgid_eez")
    @ColumnAliasName(aliasName = "mrgidEez")
    private Long mrgidEez;

    public EezEntity() {
        // why JPA why
    }

    public EezEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSovereign() {
        return this.sovereign;
    }

    public void setSovereign(String sovereign) {
        this.sovereign = sovereign;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getSovId() {
        return this.sovId;
    }

    public void setSovId(Long sovId) {
        this.sovId = sovId;
    }

    public Long getEezId() {
        return this.eezId;
    }

    public void setEezId(Long eezId) {
        this.eezId = eezId;
    }

    public BigDecimal getMrgid() {
        return this.mrgid;
    }

    public void setMrgid(BigDecimal mrgid) {
        this.mrgid = mrgid;
    }

    public String getDateChang() {
        return this.dateChang;
    }

    public void setDateChang(String dateChang) {
        this.dateChang = dateChang;
    }

    public Double getAreaM2() {
        return this.areaM2;
    }

    public void setAreaM2(Double areaM2) {
        this.areaM2 = areaM2;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getMrgidEez() {
        return this.mrgidEez;
    }

    public void setMrgidEez(Long mrgidEez) {
        this.mrgidEez = mrgidEez;
    }

}