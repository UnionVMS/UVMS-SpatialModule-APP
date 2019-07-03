/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = EezEntity.ALL_AREAS, query = "SELECT e FROM EezEntity e WHERE enabled = true"),
        @NamedQuery(name = EezEntity.EEZ_BY_COORDINATE, query = "FROM EezEntity WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = EezEntity.EEZ_COLUMNS, query = "SELECT eez.id as gid, eez.name AS name, eez.code AS code FROM EezEntity AS eez WHERE eez.id in (:ids)"),
        @NamedQuery(name = EezEntity.DISABLE_EEZ_AREAS, query = "UPDATE EezEntity SET enabled = false"),
        @NamedQuery(name = EezEntity.SEARCH_EEZ, query = "FROM EezEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = EezEntity.AREA_BY_AREA_CODES, query = "From EezEntity where code in :code AND enabled=true "),
        @NamedQuery(name = EezEntity.SEARCH_EEZ_NAMES_BY_CODE, query = "From EezEntity where code in (SELECT distinct(code) from EezEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)")
})
@Table(name = "eez")
public class EezEntity extends BaseAreaEntity {

    public static final String ALL_AREAS = "EezEntity.AllAreas";
    public static final String EEZ_BY_COORDINATE = "EezEntity.ByCoordinate";
    public static final String DISABLE_EEZ_AREAS = "EezEntity.disableEezAreas";
    public static final String SEARCH_EEZ = "EezEntity.searchByNameAndCode";
    public static final String SEARCH_EEZ_NAMES_BY_CODE = "EezEntity.searchNameByCode";
    public static final String EEZ_COLUMNS = "EezEntity.findSelectedColumns";
    public static final String AREA_BY_AREA_CODES = "EezEntity.areaByAreaCodes";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_EEZ_GEN", sequenceName="eez_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_EEZ_GEN")
    @JsonProperty("gid")
    private Long id;
	
    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String sovereign;

    @Column(length = 150)
    private String remarks;

    @Column(name = "sov_id")
    private Long sovId;

    @Column(name = "eez_id")
    private Long eezId;

    @Column(name = "mrgid")
    private BigDecimal mrGid;

    @Column(name = "date_chang", length = 50)
    private String dateChang;

    @Column(name = "area_m2")
    private Double areaM2;

    private Double longitude;

    private Double latitude;

    @Column(name = "mrgid_eez")
    private Long mrgidEez;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSovereign() {
        return sovereign;
    }

    public void setSovereign(String sovereign) {
        this.sovereign = sovereign;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getSovId() {
        return sovId;
    }

    public void setSovId(Long sovId) {
        this.sovId = sovId;
    }

    public Long getEezId() {
        return eezId;
    }

    public void setEezId(Long eezId) {
        this.eezId = eezId;
    }

    public BigDecimal getMrGid() {
        return mrGid;
    }

    public void setMrGid(BigDecimal mrGid) {
        this.mrGid = mrGid;
    }

    public String getDateChang() {
        return dateChang;
    }

    public void setDateChang(String dateChang) {
        this.dateChang = dateChang;
    }

    public Double getAreaM2() {
        return areaM2;
    }

    public void setAreaM2(Double areaM2) {
        this.areaM2 = areaM2;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getMrgidEez() {
        return mrgidEez;
    }

    public void setMrgidEez(Long mrgidEez) {
        this.mrgidEez = mrgidEez;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EezEntity eezEntity = (EezEntity) o;
        return Objects.equals(id, eezEntity.id) &&
                Objects.equals(country, eezEntity.country) &&
                Objects.equals(sovereign, eezEntity.sovereign) &&
                Objects.equals(remarks, eezEntity.remarks) &&
                Objects.equals(sovId, eezEntity.sovId) &&
                Objects.equals(eezId, eezEntity.eezId) &&
                Objects.equals(mrGid, eezEntity.mrGid) &&
                Objects.equals(dateChang, eezEntity.dateChang) &&
                Objects.equals(areaM2, eezEntity.areaM2) &&
                Objects.equals(longitude, eezEntity.longitude) &&
                Objects.equals(latitude, eezEntity.latitude) &&
                Objects.equals(mrgidEez, eezEntity.mrgidEez);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, country, sovereign, remarks, sovId, eezId, mrGid, dateChang, areaM2, longitude, latitude, mrgidEez);
    }
}