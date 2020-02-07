/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stat_rect")
@NamedQueries({
        @NamedQuery(name = StatRectEntity.ALL_AREAS, query = "SELECT e FROM StatRectEntity e WHERE enabled = true"),
        @NamedQuery(name = StatRectEntity.DISABLE, query = "UPDATE StatRectEntity SET enabled = false"),
        @NamedQuery(name = StatRectEntity.BY_INTERSECT,
                query = "FROM StatRectEntity WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = StatRectEntity.SEARCH_STATRECT, query = "FROM StatRectEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = StatRectEntity.SEARCH_STATRECT_NAMES_BY_CODE, query = "From StatRectEntity where code in (SELECT distinct(code) from StatRectEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = StatRectEntity.AREA_BY_AREA_CODES, query = "From StatRectEntity where code in :code AND enabled=true "),
        @NamedQuery(name = StatRectEntity.STATRECT_COLUMNS, query = "SELECT statrect.id as gid, statrect.name AS name, statrect.code AS code FROM StatRectEntity AS statrect WHERE statrect.id in (:ids)")
})

public class StatRectEntity extends BaseAreaEntity {

    public static final String ALL_AREAS = "StatRectEntity.AllAreas";
    public static final String BY_INTERSECT = "StatRectEntity.byIntersect";
    public static final String DISABLE = "StatRectEntity.disable";
    public static final String SEARCH_STATRECT = "StatRectEntity.searchStatrectByNameOrCode";
    public static final String SEARCH_STATRECT_NAMES_BY_CODE = "StatRectEntity.searchNamesByCode";
    public static final String STATRECT_COLUMNS = "StatRectEntity.statRectColumns";
    public static final String AREA_BY_AREA_CODES = "StatRectEntity.areaByAreaCodes";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_STATRECT_GEN", sequenceName="stat_rect_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STATRECT_GEN")
    //@JsonProperty("gid")
    @JsonbProperty("gid")
    private Long id;
	
    private Double north;

    private Double south;

    private Double east;

    private Double west;

    @Override
    public String getDisableQueryName(){
        return DISABLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StatRectEntity that = (StatRectEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(north, that.north) &&
                Objects.equals(south, that.south) &&
                Objects.equals(east, that.east) &&
                Objects.equals(west, that.west);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, north, south, east, west);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNorth() {
        return north;
    }

    public void setNorth(Double north) {
        this.north = north;
    }

    public Double getSouth() {
        return south;
    }

    public void setSouth(Double south) {
        this.south = south;
    }

    public Double getEast() {
        return east;
    }

    public void setEast(Double east) {
        this.east = east;
    }

    public Double getWest() {
        return west;
    }

    public void setWest(Double west) {
        this.west = west;
    }
}