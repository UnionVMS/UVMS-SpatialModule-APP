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
import java.util.Objects;

@Entity
@Table(name = "fao")
@NamedQueries({
        @NamedQuery(name = FaoEntity2.DISABLE_FAO_AREAS, query = "UPDATE FaoEntity2 SET enabled = false"),
        @NamedQuery(name = FaoEntity2.FAO_BY_INTERSECT,
                query = "FROM FaoEntity2 WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = FaoEntity2.SEARCH_FAO, query = "FROM FaoEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = FaoEntity2.SEARCH_FAO_NAMES_BY_CODE, query = "From FaoEntity2 where code in (SELECT distinct(code) from FaoEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = FaoEntity2.FAO_COLUMNS, query = "SELECT fao.id as gid, fao.name AS name, fao.code AS code FROM FaoEntity2 AS fao WHERE fao.id in (:ids)")
})
public class FaoEntity2 extends BaseAreaEntity2 {

    public static final String DISABLE_FAO_AREAS = "faoEntity2.disableFaoAreas";
    public static final String FAO_BY_INTERSECT = "faoEntity2.faoByIntersect";
    public static final String SEARCH_FAO = "FaoEntity2.searchFaoByNameOrCode";
    public static final String SEARCH_FAO_NAMES_BY_CODE = "FaoEntity2.searchNamesByCode";
    public static final String FAO_COLUMNS = "FaoEntity2.FaoColumns";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="fao_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    @JsonProperty("gid")
    private Long id;
	
    private String ocean;

    private String subocean;

    @Column(name = "f_area")
    private String fArea;

    @Column(name = "area_l")
    private String areaL;

    @Column(name = "f_subarea")
    private String fSubarea;

    @Column(name = "subarea_n")
    private String subareaN;

    @Column(name = "subarea_l")
    private String subareaL;

    @Column(name = "f_division")
    private String fDivision;

    @Column(name = "division_n")
    private String divisionN;

    @Column(name = "division_l")
    private String divisionL;

    @Column(name = "f_subdivis")
    private String fSubdivis;

    @Column(name = "subdivis_n")
    private String subdivisN;

    @Column(name = "subdivis_l")
    private String subdivisL;

    @Column(name = "f_subunit")
    private String fSubunit;

    @Column(name = "subunit_n")
    private String subunitN;

    @Column(name = "subunit_l")
    private String subunitL;

    @Column(name = "ele_name")
    private String eleName;

    @Column(name = "ele_label")
    private String eleLabel;

    @Column(name = "ele_type")
    private String eleType;

    @Column(name = "f_label")
    private String fLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOcean() {
        return ocean;
    }

    public void setOcean(String ocean) {
        this.ocean = ocean;
    }

    public String getSubocean() {
        return subocean;
    }

    public void setSubocean(String subocean) {
        this.subocean = subocean;
    }

    public String getfArea() {
        return fArea;
    }

    public void setfArea(String fArea) {
        this.fArea = fArea;
    }

    public String getAreaL() {
        return areaL;
    }

    public void setAreaL(String areaL) {
        this.areaL = areaL;
    }

    public String getfSubarea() {
        return fSubarea;
    }

    public void setfSubarea(String fSubarea) {
        this.fSubarea = fSubarea;
    }

    public String getSubareaN() {
        return subareaN;
    }

    public void setSubareaN(String subareaN) {
        this.subareaN = subareaN;
    }

    public String getSubareaL() {
        return subareaL;
    }

    public void setSubareaL(String subareaL) {
        this.subareaL = subareaL;
    }

    public String getfDivision() {
        return fDivision;
    }

    public void setfDivision(String fDivision) {
        this.fDivision = fDivision;
    }

    public String getDivisionN() {
        return divisionN;
    }

    public void setDivisionN(String divisionN) {
        this.divisionN = divisionN;
    }

    public String getDivisionL() {
        return divisionL;
    }

    public void setDivisionL(String divisionL) {
        this.divisionL = divisionL;
    }

    public String getfSubdivis() {
        return fSubdivis;
    }

    public void setfSubdivis(String fSubdivis) {
        this.fSubdivis = fSubdivis;
    }

    public String getSubdivisN() {
        return subdivisN;
    }

    public void setSubdivisN(String subdivisN) {
        this.subdivisN = subdivisN;
    }

    public String getSubdivisL() {
        return subdivisL;
    }

    public void setSubdivisL(String subdivisL) {
        this.subdivisL = subdivisL;
    }

    public String getfSubunit() {
        return fSubunit;
    }

    public void setfSubunit(String fSubunit) {
        this.fSubunit = fSubunit;
    }

    public String getSubunitN() {
        return subunitN;
    }

    public void setSubunitN(String subunitN) {
        this.subunitN = subunitN;
    }

    public String getSubunitL() {
        return subunitL;
    }

    public void setSubunitL(String subunitL) {
        this.subunitL = subunitL;
    }

    public String getEleName() {
        return eleName;
    }

    public void setEleName(String eleName) {
        this.eleName = eleName;
    }

    public String getEleLabel() {
        return eleLabel;
    }

    public void setEleLabel(String eleLabel) {
        this.eleLabel = eleLabel;
    }

    public String getEleType() {
        return eleType;
    }

    public void setEleType(String eleType) {
        this.eleType = eleType;
    }

    public String getfLabel() {
        return fLabel;
    }

    public void setfLabel(String fLabel) {
        this.fLabel = fLabel;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FaoEntity2 faoEntity = (FaoEntity2) o;
        return Objects.equals(id, faoEntity.id) &&
                Objects.equals(ocean, faoEntity.ocean) &&
                Objects.equals(subocean, faoEntity.subocean) &&
                Objects.equals(fArea, faoEntity.fArea) &&
                Objects.equals(areaL, faoEntity.areaL) &&
                Objects.equals(fSubarea, faoEntity.fSubarea) &&
                Objects.equals(subareaN, faoEntity.subareaN) &&
                Objects.equals(subareaL, faoEntity.subareaL) &&
                Objects.equals(fDivision, faoEntity.fDivision) &&
                Objects.equals(divisionN, faoEntity.divisionN) &&
                Objects.equals(divisionL, faoEntity.divisionL) &&
                Objects.equals(fSubdivis, faoEntity.fSubdivis) &&
                Objects.equals(subdivisN, faoEntity.subdivisN) &&
                Objects.equals(subdivisL, faoEntity.subdivisL) &&
                Objects.equals(fSubunit, faoEntity.fSubunit) &&
                Objects.equals(subunitN, faoEntity.subunitN) &&
                Objects.equals(subunitL, faoEntity.subunitL) &&
                Objects.equals(eleName, faoEntity.eleName) &&
                Objects.equals(eleLabel, faoEntity.eleLabel) &&
                Objects.equals(eleType, faoEntity.eleType) &&
                Objects.equals(fLabel, faoEntity.fLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, ocean, subocean, fArea, areaL, fSubarea, subareaN, subareaL, fDivision, divisionN, divisionL, fSubdivis, subdivisN, subdivisL, fSubunit, subunitN, subunitL, eleName, eleLabel, eleType, fLabel);
    }
}