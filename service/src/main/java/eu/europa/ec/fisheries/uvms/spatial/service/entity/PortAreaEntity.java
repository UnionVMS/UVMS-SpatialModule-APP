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
@NamedQueries({
        @NamedQuery(name = PortAreaEntity.ALL_AREAS, query = "SELECT e FROM PortAreaEntity e WHERE enabled = true"),
        @NamedQuery(name = PortAreaEntity.PORT_AREA_BY_POINT,
                query = "FROM PortAreaEntity WHERE within(:point, geom) = true AND enabled = true"),

        @NamedQuery(name = PortAreaEntity.DISABLE_PORT_AREAS, query = "UPDATE PortAreaEntity SET enabled = false"),
        @NamedQuery(name = PortAreaEntity.SEARCH_PORTAREAS, query = "FROM PortAreaEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = PortAreaEntity.SEARCH_PORT_AREA_NAMES_BY_CODE, query = "From PortAreaEntity where code in (SELECT distinct(code) from PortAreaEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = PortAreaEntity.AREA_BY_AREA_CODES, query = "From PortAreaEntity where code in :code AND enabled=true "),
        @NamedQuery(name = PortAreaEntity.PORTAREA_COLUMNS, query = "SELECT portarea.id as gid, portarea.name AS name, portarea.code AS code FROM PortAreaEntity AS portarea WHERE portarea.id in (:ids)")})
@Table(name = "port_area")
public class PortAreaEntity extends BaseAreaEntity {

    public static final String ALL_AREAS = "PortAreaEntity.AllAreas";
    public static final String PORT_AREA_BY_POINT = "portEntity.PortAreaByPoint";
    public static final String DISABLE_PORT_AREAS = "portAreasEntity.disablePortAreas";
    public static final String SEARCH_PORTAREAS = "PortAreaEntity.searchPortAreaByNameOrCode";
    public static final String SEARCH_PORT_AREA_NAMES_BY_CODE = "PortAreaEntity.searchNamesByCode";
    public static final String PORTAREA_COLUMNS = "PortAreaEntity.portAreaColumns";
    public static final String AREA_BY_AREA_CODES = "PortAreaEntity.areaByAreaCodes";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_PORTAREA_GEN", sequenceName="port_area_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PORTAREA_GEN")
    @JsonProperty("gid")
    private Long id;

    @Override
    public String getDisableQueryName(){
        return DISABLE_PORT_AREAS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PortAreaEntity that = (PortAreaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}