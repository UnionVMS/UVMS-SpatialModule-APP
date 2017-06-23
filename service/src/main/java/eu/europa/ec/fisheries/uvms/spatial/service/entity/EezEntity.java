/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.util.ColumnAliasName;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


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
@Data
public class EezEntity extends BaseAreaEntity {

    public static final String EEZ_BY_COORDINATE = "eezEntity.ByCoordinate";
    public static final String DISABLE_EEZ_AREAS = "eezEntity.disableEezAreas";
    public static final String SEARCH_EEZ = "eezEntity.searchByNameAndCode";
    public static final String SEARCH_EEZ_NAMES_BY_CODE = "eezEntity.searchNameByCode";
    public static final String EEZ_COLUMNS = "eezEntity.findSelectedColumns";

    private static final String COUNTRY_ALIAS = "country";
    private static final String SOVEREIGN_ALIAS = "sovereign";
    private static final String REMARKS_ALIAS = "remarks";
    private static final String SOV_ID = "sov_id";
    private static final String EEZ_ID = "eez_id";
    private static final String MRGID = "mrgid";
    private static final String DATE_CHANG = "date_chang";
    private static final String AREA_M_2 = "area_m2";
    private static final String LONGITUDE_ALIAS = "longitude";
    private static final String LATITUDE_ALIAS = "latitude";
    private static final String MRGID_EEZ = "mrgid_eez";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="eez_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long id;
	
    @Column(length = 100)
    @ColumnAliasName(aliasName = COUNTRY_ALIAS)
    private String country;

    @Column(length = 100)
    @ColumnAliasName(aliasName = SOVEREIGN_ALIAS)
    private String sovereign;

    @Column(length = 150)
    @ColumnAliasName(aliasName = REMARKS_ALIAS)
    private String remarks;

    @Column(name = SOV_ID)
    @ColumnAliasName(aliasName = "sovId")
    private Long sovId;

    @Column(name = EEZ_ID)
    @ColumnAliasName(aliasName = "eezId")
    private Long eezId;

    @Column(name = MRGID)
    @ColumnAliasName(aliasName = MRGID)
    private BigDecimal mrGid;

    @Column(name = "date_chang", length = 50)
    @ColumnAliasName(aliasName = "dateChang")
    private String dateChang;

    @Column(name = AREA_M_2)
    @ColumnAliasName(aliasName = "areaM2")
    private Double areaM2;

    @Column(name = LONGITUDE_ALIAS)
    @ColumnAliasName(aliasName = LONGITUDE_ALIAS)
    private Double longitude;

    @Column(name = LATITUDE_ALIAS)
    @ColumnAliasName(aliasName = LATITUDE_ALIAS)
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

}