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
@Table(name = "fmz")
@NamedQueries({
        @NamedQuery(name = FmzEntity.DISABLE, query = "UPDATE FmzEntity SET enabled = 'N'"),
        @NamedQuery(name = FmzEntity.BY_INTERSECT,
                query = "FROM FmzEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = FmzEntity.SEARCH_FMZ, query = "FROM FmzEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = FmzEntity.SEARCH_FMZ_NAMES_BY_CODE, query = "From FmzEntity where code in (SELECT distinct(code) from FmzEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)"),
        @NamedQuery(name = FmzEntity.FMZ_COLUMNS, query = "SELECT fmz.id as gid, fmz.name AS name, fmz.code AS code FROM FmzEntity AS fmz WHERE fmz.id in (:ids)")
})
@EqualsAndHashCode(callSuper = true)
@Data
public class FmzEntity extends BaseAreaEntity {

    public static final String DISABLE = "fmzEntity.disable";
    public static final String BY_INTERSECT = "fmzEntity.byIntersect";
    public static final String SEARCH_FMZ = "fmzEntity.SearcgFmzByNameOrCode";
    public static final String SEARCH_FMZ_NAMES_BY_CODE = "fmzEntity.searchNamesByCode";
    public static final String FMZ_COLUMNS = "fmzEntity.fmzColumns";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN_fmz", sequenceName="fmz_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN_fmz")
    @JsonProperty("gid")
    private Long id;

    @Column(name = "fmz_id")
    private Long fmzId;

    @Column(name = "edited")
    private String edited;

    public FmzEntity() {
        // why JPA why
    }

    public FmzEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }

}