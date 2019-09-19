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
        @NamedQuery(name = RfmoEntity.ALL_AREAS, query = "SELECT e FROM RfmoEntity e WHERE enabled = true"),
        @NamedQuery(name = RfmoEntity.RFMO_BY_COORDINATE,
                query = "FROM RfmoEntity WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = RfmoEntity.RFMO_COLUMNS,
                query = "SELECT rfmo.id as gid, rfmo.name AS name, rfmo.code AS code FROM RfmoEntity AS rfmo WHERE rfmo.id in (:ids)"),
        @NamedQuery(name = RfmoEntity.DISABLE_RFMO_AREAS,
                query = "UPDATE RfmoEntity SET enabled = false"),
        @NamedQuery(name = RfmoEntity.SEARCH_RFMO, query = "FROM RfmoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = RfmoEntity.AREA_BY_AREA_CODES, query = "From RfmoEntity where code in :code AND enabled=true "),
        @NamedQuery(name = RfmoEntity.SEARCH_RFMO_NAMES_BY_CODE, query = "From RfmoEntity where code in (SELECT distinct(code) from RfmoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)")})
@Table(name = "rfmo")
public class RfmoEntity extends BaseAreaEntity {

    public static final String ALL_AREAS = "RfmoEntity.AllAreas";
    public static final String RFMO_BY_COORDINATE = "RfmoEntity.ByCoordinate";
    public static final String DISABLE_RFMO_AREAS = "RfmoEntity.disableRfmoAreas";
    public static final String SEARCH_RFMO = "RfmoEntity.searchRfmoByNameOrCode";
    public static final String SEARCH_RFMO_NAMES_BY_CODE = "RfmoEntity.searchNamesByCode";
    public static final String RFMO_COLUMNS = "RfmoEntity.findSelectedColumns";
    public static final String AREA_BY_AREA_CODES = " RfmoEntity.areaByAreaCodes";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_RFMO_GEN", sequenceName="rfmo_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RFMO_GEN")
    @JsonProperty("gid")
    private Long id;
	
	
    private Boolean tuna;

    @Override
    public String getDisableQueryName(){
        return DISABLE_RFMO_AREAS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTuna() {
        return tuna;
    }

    public void setTuna(Boolean tuna) {
        this.tuna = tuna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RfmoEntity that = (RfmoEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tuna, that.tuna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, tuna);
    }
}