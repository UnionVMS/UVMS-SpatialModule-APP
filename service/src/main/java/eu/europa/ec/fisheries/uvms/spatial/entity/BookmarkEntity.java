package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "bookmark", schema = "spatial")
public class BookmarkEntity implements Serializable {
	
	private static final long serialVersionUID = 6797853213499502860L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "user_id", nullable = false)
	private long userId;
	
	@Lob
	@Column(name = "bookmark_definition", nullable = false)
	private String bookmarkDefinition;

	public BookmarkEntity() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getBookmarkDefinition() {
		return this.bookmarkDefinition;
	}

	public void setBookmarkDefinition(String bookmarkDefinition) {
		this.bookmarkDefinition = bookmarkDefinition;
	}

}
