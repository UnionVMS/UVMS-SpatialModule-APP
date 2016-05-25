package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "bookmark")
@NamedQuery(name = BookmarkEntity.LIST_BY_USERNAME, query = "FROM BookmarkEntity b WHERE b.createdBy = :createdBy")
@ToString
//@SequenceGenerator(name = "default_gen", sequenceName = "bookmark_seq", allocationSize = 1)
public class BookmarkEntity extends BaseEntity {

    public static final String LIST_BY_USERNAME = "Bookmark.listByUsername";

    @Column(nullable = false)
	private Integer srs;
	
	@Column(nullable = false)
	private String name;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String extent;

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

}
