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
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.util.ColumnAliasName;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity
@Table(name = "stat_rect")
@NamedQueries({
        @NamedQuery(name = StatRectEntity.DISABLE, query = "UPDATE StatRectEntity SET enabled = 'N'"),
        @NamedQuery(name = StatRectEntity.BY_INTERSECT,
                query = "FROM StatRectEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = StatRectEntity.SEARCH_STATRECT, query = "FROM StatRectEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = StatRectEntity.SEARCH_STATRECT_NAMES_BY_CODE, query = "From StatRectEntity where code in (SELECT distinct(code) from StatRectEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)"),
        @NamedQuery(name = StatRectEntity.STATRECT_COLUMNS, query = "SELECT statrect.id as gid, statrect.name AS name, statrect.code AS code FROM StatRectEntity AS statrect WHERE statrect.id in (:ids)")
})
@EqualsAndHashCode(callSuper = true)
@Data
public class StatRectEntity extends BaseAreaEntity {

    public static final String BY_INTERSECT = "statRectEntity.byIntersect";
    public static final String DISABLE = "statRectEntity.disable";
    public static final String SEARCH_STATRECT = "statrectEntity.searchStatrectByNameOrCode";
    public static final String SEARCH_STATRECT_NAMES_BY_CODE = "statrectEntity.searchNamesByCode";
    public static final String STATRECT_COLUMNS = "statrectEntity.statRectColumns";

    public static final String NORTH = "north";
    public static final String SOUTH = "south";
    public static final String EAST = "east";
    public static final String WEST = "west";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="stat_rect_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    @JsonProperty("gid")
    private Long id;
	
    @Column(name = "north")
    @ColumnAliasName(aliasName = NORTH)
    private Double north;

    @Column(name = "south")
    @ColumnAliasName(aliasName = SOUTH)
    private Double south;

    @Column(name = "east")
    @ColumnAliasName(aliasName = EAST)
    private Double east;

    @Column(name = "west")
    @ColumnAliasName(aliasName = WEST)
    private Double west;

    public StatRectEntity() {
        // why JPA why
    }

    public StatRectEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
       super(values, mapping);
    }
}