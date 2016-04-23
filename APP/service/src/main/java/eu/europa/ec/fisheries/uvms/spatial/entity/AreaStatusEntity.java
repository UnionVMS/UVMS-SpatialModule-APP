package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "area_status")
public class AreaStatusEntity extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "user_area_id", nullable = false)
	private UserAreasEntity userAreas;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date", nullable = false)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = false)
	private Date endDate;
	
	@Convert(converter = CharBooleanConverter.class)
	@Column(name = "is_visible", nullable = false, length = 1)
	private Boolean isVisible = false;

	public AreaStatusEntity() {
        // why JPA why
    }

	public UserAreasEntity getUserAreas() {
		return this.userAreas;
	}

	public void setUserAreas(UserAreasEntity userAreas) {
		this.userAreas = userAreas;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsVisible() {
		return this.isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

}
