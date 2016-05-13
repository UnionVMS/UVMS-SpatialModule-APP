package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "provider_format")
public class ProviderFormatEntity extends BaseEntity {

	@Column(name = "service_type", nullable = false, length = 10)
	private String serviceType;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "providerFormat", cascade = CascadeType.ALL)
	private Set<ServiceLayerEntity> serviceLayers;

	public ProviderFormatEntity() {
        // why JPA why
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
