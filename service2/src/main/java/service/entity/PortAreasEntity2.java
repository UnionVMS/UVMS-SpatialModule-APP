/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = PortAreasEntity2.PORT_AREA_BY_COORDINATE,
                query = "FROM PortAreasEntity WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = PortAreasEntity2.DISABLE_PORT_AREAS, query = "UPDATE PortAreasEntity SET enabled = false"),
        @NamedQuery(name = PortAreasEntity2.SEARCH_PORTAREAS, query = "FROM PortAreasEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = PortAreasEntity2.SEARCH_PORT_AREA_NAMES_BY_CODE, query = "From PortAreasEntity where code in (SELECT distinct(code) from PortAreasEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = PortAreasEntity2.PORTAREA_COLUMNS, query = "SELECT portarea.id as gid, portarea.name AS name, portarea.code AS code FROM PortAreasEntity AS portarea WHERE portarea.id in (:ids)")})
@Table(name = "port_area")
public class PortAreasEntity2 extends BaseAreaEntity2 {

    public static final String PORT_AREA_BY_COORDINATE = "portEntity.PortAreaByCoordinate";
    public static final String DISABLE_PORT_AREAS = "portAreasEntity.disablePortAreas";
    public static final String SEARCH_PORTAREAS = "portAreaEntity.searchPortAreaByNameOrCode";
    public static final String SEARCH_PORT_AREA_NAMES_BY_CODE = "portAreaEntity.searchNamesByCode";
    public static final String PORTAREA_COLUMNS = "portAreaEntity.portAreaColumns";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="port_area_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    @JsonProperty("gid")
    private Long id;

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
        PortAreasEntity2 that = (PortAreasEntity2) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}