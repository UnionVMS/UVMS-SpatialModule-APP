/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMappingProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "fao")
@NamedQueries({
        @NamedQuery(name = FaoEntity.DISABLE_FAO_AREAS, query = "UPDATE FaoEntity SET enabled = false"),
        @NamedQuery(name = FaoEntity.FAO_BY_INTERSECT,
                query = "FROM FaoEntity WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = FaoEntity.SEARCH_FAO, query = "FROM FaoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = FaoEntity.SEARCH_FAO_NAMES_BY_CODE, query = "From FaoEntity where code in (SELECT distinct(code) from FaoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = FaoEntity.FAO_COLUMNS, query = "SELECT fao.id as gid, fao.name AS name, fao.code AS code FROM FaoEntity AS fao WHERE fao.id in (:ids)")
})
@EqualsAndHashCode(callSuper = true)
@Data
public class FaoEntity extends BaseAreaEntity {

    public static final String DISABLE_FAO_AREAS = "faoEntity.disableFaoAreas";
    public static final String FAO_BY_INTERSECT = "faoEntity.faoByIntersect";
    public static final String SEARCH_FAO = "FaoEntity.searchFaoByNameOrCode";
    public static final String SEARCH_FAO_NAMES_BY_CODE = "FaoEntity.searchNamesByCode";
    public static final String FAO_COLUMNS = "FaoEntity.FaoColumns";

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

    public FaoEntity() {
        // why JPA why
    }

    public FaoEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }
}