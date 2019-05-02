/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = RfmoEntity2.RFMO_BY_COORDINATE,
                query = "FROM RfmoEntity2 WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = RfmoEntity2.RFMO_COLUMNS,
                query = "SELECT rfmo.id as gid, rfmo.name AS name, rfmo.code AS code FROM RfmoEntity2 AS rfmo WHERE rfmo.id in (:ids)"),
        @NamedQuery(name = RfmoEntity2.DISABLE_RFMO_AREAS,
                query = "UPDATE RfmoEntity2 SET enabled = false"),
        @NamedQuery(name = RfmoEntity2.SEARCH_RFMO, query = "FROM RfmoEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = RfmoEntity2.SEARCH_RFMO_NAMES_BY_CODE, query = "From RfmoEntity2 where code in (SELECT distinct(code) from RfmoEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)")})
@Table(name = "rfmo")
public class RfmoEntity2 extends BaseAreaEntity2 {

    public static final String RFMO_BY_COORDINATE = "rfmoEntity2.ByCoordinate";
    public static final String DISABLE_RFMO_AREAS = "rfmoEntity2.disableRfmoAreas";
    public static final String SEARCH_RFMO = "rfmoEntity2.searchRfmoByNameOrCode";
    public static final String SEARCH_RFMO_NAMES_BY_CODE = "rfmoEntity2.searchNamesByCode";
    public static final String RFMO_COLUMNS = "rfmoEntity2.findSelectedColumns";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="rfmo_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    @JsonProperty("gid")
    private Long id;
	
	
    private Boolean tuna;

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
        RfmoEntity2 that = (RfmoEntity2) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tuna, that.tuna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, tuna);
    }
}