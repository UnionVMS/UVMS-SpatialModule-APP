package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class CustomAreaPropertiesEntity implements Serializable { // This is not an entity

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "prop_name", nullable = false)
	private String propName;
	
	@Column(name = "prop_value", nullable = false)
	private String propValue;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "custom_areas_id", nullable = false)
	private CustomAreasEntity customAreas;
	
	@Column(name = "view_ref", nullable = false)
	private String viewRef;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropValue() {
		return propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	public CustomAreasEntity getCustomAreas() {
		return customAreas;
	}

	public void setCustomAreas(CustomAreasEntity customAreas) {
		this.customAreas = customAreas;
	}

	public String getViewRef() {
		return viewRef;
	}

	public void setViewRef(String viewRef) {
		this.viewRef = viewRef;
	}
}
