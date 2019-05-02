/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity;

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
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.upload.UploadMappingProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@NamedQueries({
        @NamedQuery(name = RfmoEntity.RFMO_BY_COORDINATE,
                query = "FROM RfmoEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = RfmoEntity.RFMO_COLUMNS,
                query = "SELECT rfmo.id as gid, rfmo.name AS name, rfmo.code AS code FROM RfmoEntity AS rfmo WHERE rfmo.id in (:ids)"),
        @NamedQuery(name = RfmoEntity.DISABLE_RFMO_AREAS,
                query = "UPDATE RfmoEntity SET enabled = 'N'"),
        @NamedQuery(name = RfmoEntity.SEARCH_RFMO, query = "FROM RfmoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = RfmoEntity.SEARCH_RFMO_NAMES_BY_CODE, query = "From RfmoEntity where code in (SELECT distinct(code) from RfmoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)")})
@Table(name = "rfmo")
@EqualsAndHashCode(callSuper = true)
@Data
public class RfmoEntity extends BaseAreaEntity {

    public static final String RFMO_BY_COORDINATE = "rfmoEntity.ByCoordinate";
    public static final String DISABLE_RFMO_AREAS = "rfmoEntity.disableRfmoAreas";
    public static final String SEARCH_RFMO = "rfmoEntity.searchRfmoByNameOrCode";
    public static final String SEARCH_RFMO_NAMES_BY_CODE = "rfmoEntity.searchNamesByCode";
    public static final String RFMO_COLUMNS = "rfmoEntity.findSelectedColumns";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="rfmo_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    @JsonProperty("gid")
    private Long id;
	
	
    @Column(length = 10)
    private String tuna;

    public RfmoEntity() {
        // why JPA why
    }

    public RfmoEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }

}