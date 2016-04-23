package eu.europa.ec.fisheries.uvms.spatial.entity;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "bookmark")
@NamedQuery(name = BookmarkEntity.LIST_BY_USERNAME, query = "FROM BookmarkEntity b WHERE b.createdBy = :createdBy")
@ToString
public class BookmarkEntity extends BaseEntity {

    public static final String LIST_BY_USERNAME = "Bookmark.listByUsername";

    @Column(name = "srs", nullable = false)
	private Integer srs;
	
	@Column(name = "name", nullable = false)
	private String name;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "extent", nullable = false)
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
