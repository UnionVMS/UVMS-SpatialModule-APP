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
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fao")
@NamedQueries({
        @NamedQuery(name = FaoEntity.DISABLE_FAO_AREAS, query = "UPDATE FaoEntity SET enabled = 'N'"),
        @NamedQuery(name = FaoEntity.FAO_BY_INTERSECT,
                query = "FROM FaoEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = FaoEntity.SEARCH_FAO, query = "FROM FaoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = FaoEntity.SEARCH_FAO_NAMES_BY_CODE, query = "From FaoEntity where code in (SELECT distinct(code) from FaoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)"),
        @NamedQuery(name = FaoEntity.FAO_COLUMNS, query = "SELECT fao.id as gid, fao.name AS name, fao.code AS code FROM FaoEntity AS fao WHERE fao.id in (:ids)")
})
public class FaoEntity extends BaseAreaEntity {

    public static final String DISABLE_FAO_AREAS = "faoEntity.disableFaoAreas";
    public static final String FAO_BY_INTERSECT = "faoEntity.faoByIntersect";
    public static final String SEARCH_FAO = "FaoEntity.searchFaoByNameOrCode";
    public static final String SEARCH_FAO_NAMES_BY_CODE = "FaoEntity.searchNamesByCode";
    public static final String FAO_COLUMNS = "FaoEntity.FaoColumns";

    public static final String OCEAN = "ocean";
    public static final String SUBOCEAN = "subocean";
    public static final String F_AREA = "f_area";
    public static final String AREA_L = "area_l";
    public static final String F_SUBAREA = "f_subarea";
    public static final String SUBAREA_N = "subarea_n";
    public static final String SUBAREA_L = "subarea_l";
    public static final String F_DIVISION = "f_division";
    public static final String DIVISION_N = "division_n";
    public static final String DIVISION_L = "division_l";
    public static final String F_SUBDIVIS = "f_subdivis";
    public static final String SUBDIVIS_N = "subdivis_n";
    public static final String SUBDIVIS_L = "subdivis_l";
    public static final String F_SUBUNIT = "f_subunit";
    public static final String SUBUNIT_N = "subunit_n";
    public static final String SUBUNIT_L = "subunit_l";
    public static final String ELE_NAME = "ele_name";
    public static final String ELE_LABEL = "ele_label";
    public static final String ELE_TYPE = "ele_type";
    public static final String F_LABEL = "f_label";

    @Column(name = "ocean")
    @ColumnAliasName(aliasName = OCEAN)
    private String ocean;

    @Column(name = "subocean")
    @ColumnAliasName(aliasName = SUBOCEAN)
    private String subocean;

    @Column(name = "f_area")
    @ColumnAliasName(aliasName = F_AREA)
    private String fArea;

    @Column(name = "area_l")
    @ColumnAliasName(aliasName = AREA_L)
    private String areaL;

    @Column(name = "f_subarea")
    @ColumnAliasName(aliasName = F_SUBAREA)
    private String fSubarea;

    @Column(name = "subarea_n")
    @ColumnAliasName(aliasName = SUBAREA_N)
    private String subareaN;

    @Column(name = "subarea_l")
    @ColumnAliasName(aliasName = SUBAREA_L)
    private String subareaL;

    @Column(name = "f_division")
    @ColumnAliasName(aliasName = F_DIVISION)
    private String fDivision;

    @Column(name = "division_n")
    @ColumnAliasName(aliasName = DIVISION_N)
    private String divisionN;

    @Column(name = "division_l")
    @ColumnAliasName(aliasName = DIVISION_L)
    private String divisionL;

    @Column(name = "f_subdivis")
    @ColumnAliasName(aliasName = F_SUBDIVIS)
    private String fSubdivis;

    @Column(name = "subdivis_n")
    @ColumnAliasName(aliasName = SUBDIVIS_N)
    private String subdivisN;

    @Column(name = "subdivis_l")
    @ColumnAliasName(aliasName = SUBDIVIS_L)
    private String subdivisL;

    @Column(name = "f_subunit")
    @ColumnAliasName(aliasName = F_SUBUNIT)
    private String fSubunit;

    @Column(name = "subunit_n")
    @ColumnAliasName(aliasName = SUBUNIT_N)
    private String subunitN;

    @Column(name = "subunit_l")
    @ColumnAliasName(aliasName = SUBUNIT_L)
    private String subunitL;

    @Column(name = "ele_name")
    @ColumnAliasName(aliasName = ELE_NAME)
    private String eleName;

    @Column(name = "ele_label")
    @ColumnAliasName(aliasName = ELE_LABEL)
    private String eleLabel;

    @Column(name = "ele_type")
    @ColumnAliasName(aliasName = ELE_TYPE)
    private String eleType;

    @Column(name = "f_label")
    @ColumnAliasName(aliasName = F_LABEL)
    private String fLabel;

    public FaoEntity() {
        // why JPA why
    }

    public FaoEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
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
}