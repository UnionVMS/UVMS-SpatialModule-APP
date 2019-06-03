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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMappingProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@NamedQueries({
        @NamedQuery(name = EezEntity.EEZ_BY_COORDINATE, query = "FROM EezEntity WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = EezEntity.EEZ_COLUMNS, query = "SELECT eez.id as gid, eez.name AS name, eez.code AS code FROM EezEntity AS eez WHERE eez.id in (:ids)"),
        @NamedQuery(name = EezEntity.DISABLE_EEZ_AREAS, query = "UPDATE EezEntity SET enabled = false"),
        @NamedQuery(name = EezEntity.SEARCH_EEZ, query = "FROM EezEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = EezEntity.SEARCH_EEZ_NAMES_BY_CODE, query = "From EezEntity where code in (SELECT distinct(code) from EezEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)")
})
@Table(name = "eez")
@EqualsAndHashCode(callSuper = true)
@Data
public class EezEntity extends BaseAreaEntity {

    public static final String EEZ_BY_COORDINATE = "eezEntity.ByCoordinate";
    public static final String DISABLE_EEZ_AREAS = "eezEntity.disableEezAreas";
    public static final String SEARCH_EEZ = "eezEntity.searchByNameAndCode";
    public static final String SEARCH_EEZ_NAMES_BY_CODE = "eezEntity.searchNameByCode";
    public static final String EEZ_COLUMNS = "eezEntity.findSelectedColumns";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="eez_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
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

    public EezEntity() {
        // why JPA why
    }

    public EezEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }

}