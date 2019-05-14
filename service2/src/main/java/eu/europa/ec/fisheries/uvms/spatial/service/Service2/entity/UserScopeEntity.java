/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_scope")

public class UserScopeEntity {

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_USERSCOPE_GEN", sequenceName="user_scope_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_USERSCOPE_GEN")
	private Long id;

    @ManyToOne
    @JoinColumn(name = "user_area_id", nullable = false)
    private UserAreasEntity userAreas;

    @Column(name = "scope_name", nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAreasEntity getUserAreas() {
        return userAreas;
    }

    public void setUserAreas(UserAreasEntity userAreas) {
        this.userAreas = userAreas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserScopeEntity that = (UserScopeEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userAreas, that.userAreas) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAreas, name);
    }
}