/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Table(name = "bookmark")
@NamedQuery(name = BookmarkEntity.LIST_BY_USERNAME, query = "FROM BookmarkEntity b WHERE b.createdBy = :createdBy")
@Data
@EqualsAndHashCode(callSuper = true)
public class BookmarkEntity extends BaseEntity {

    public static final String LIST_BY_USERNAME = "Bookmark.listByUsername";

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="bookmark_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long id;
	
    @Column(nullable = false)
	private Integer srs;
	
	@Column(nullable = false)
	private String name;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String extent;

}