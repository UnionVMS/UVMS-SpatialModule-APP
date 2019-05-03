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
@Table(name = "bookmark")
@NamedQuery(name = BookmarkEntity2.LIST_BY_USERNAME, query = "FROM BookmarkEntity2 b WHERE b.createdBy = :createdBy")
public class BookmarkEntity2 {

    public static final String LIST_BY_USERNAME = "Bookmark2.listByUsername";

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_BOOKMARK_GEN", sequenceName="bookmark_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_BOOKMARK_GEN")
	private Long id;
	
    @Column(nullable = false)
	private Integer srs;
	
	@Column(nullable = false)
	private String name;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String extent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSrs() {
		return srs;
	}

	public void setSrs(Integer srs) {
		this.srs = srs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookmarkEntity2 that = (BookmarkEntity2) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(srs, that.srs) &&
				Objects.equals(name, that.name) &&
				Objects.equals(createdBy, that.createdBy) &&
				Objects.equals(extent, that.extent);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, srs, name, createdBy, extent);
	}
}