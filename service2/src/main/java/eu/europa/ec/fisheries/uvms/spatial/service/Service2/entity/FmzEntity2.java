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
@Table(name = "fmz")
@NamedQueries({
        @NamedQuery(name = FmzEntity2.DISABLE, query = "UPDATE FmzEntity2 SET enabled = false"),
        @NamedQuery(name = FmzEntity2.BY_INTERSECT,
                query = "FROM FmzEntity2 WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = FmzEntity2.SEARCH_FMZ, query = "FROM FmzEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = FmzEntity2.SEARCH_FMZ_NAMES_BY_CODE, query = "From FmzEntity2 where code in (SELECT distinct(code) from FmzEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = FmzEntity2.FMZ_COLUMNS, query = "SELECT fmz.id as gid, fmz.name AS name, fmz.code AS code FROM FmzEntity2 AS fmz WHERE fmz.id in (:ids)")
})
public class FmzEntity2 extends BaseAreaEntity2 {

    public static final String DISABLE = "fmzEntity2.disable";
    public static final String BY_INTERSECT = "fmzEntity2.byIntersect";
    public static final String SEARCH_FMZ = "fmzEntity2.SearcgFmzByNameOrCode";
    public static final String SEARCH_FMZ_NAMES_BY_CODE = "fmzEntity2.searchNamesByCode";
    public static final String FMZ_COLUMNS = "fmzEntity2.fmzColumns";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="fmz_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    @JsonProperty("gid")
    private Long id;

    @Column(name = "fmz_id")
    private Long fmzId;

    @Column(name = "edited")
    private String edited;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFmzId() {
        return fmzId;
    }

    public void setFmzId(Long fmzId) {
        this.fmzId = fmzId;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FmzEntity2 fmzEntity = (FmzEntity2) o;
        return Objects.equals(id, fmzEntity.id) &&
                Objects.equals(fmzId, fmzEntity.fmzId) &&
                Objects.equals(edited, fmzEntity.edited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, fmzId, edited);
    }
}