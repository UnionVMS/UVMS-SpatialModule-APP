package eu.europa.ec.fisheries.uvms.spatial.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "provider_format", schema = "spatial")
public class ProviderFormatEntity implements Serializable { 
	
	private static final long serialVersionUID = 6797853213499502854L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "service_type", nullable = false, length = 10)
	private String serviceType;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "providerFormat", cascade = CascadeType.ALL)
	private Set<ServiceLayerEntity> serviceLayers;

	public ProviderFormatEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Set<ServiceLayerEntity> getServiceLayers() {
		return this.serviceLayers;
	}

	public void setServiceLayers(Set<ServiceLayerEntity> serviceLayers) {
		this.serviceLayers = serviceLayers;
	}
}