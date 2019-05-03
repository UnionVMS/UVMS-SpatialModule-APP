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
@Table(name = "system_configurations")
@NamedQueries({
        @NamedQuery(name= SysConfigEntity2.FIND_CONFIG_BY_NAME,
        query = "FROM SysConfigEntity2 config WHERE config.name = :name")
})
public class SysConfigEntity2 {

    public static final String FIND_CONFIG_BY_NAME = "sysConfig2.findById";

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_SYSCONF_GEN", sequenceName="system_configurations_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_SYSCONF_GEN")
	private Long id;
	
	
    private String name;

    private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SysConfigEntity2 that = (SysConfigEntity2) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(name, that.name) &&
				Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, value);
	}
}