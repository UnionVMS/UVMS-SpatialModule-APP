package eu.europa.ec.fisheries.uvms.spatial.util;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;

public enum SpatialTypeEnum {
	
	EEZ("EEZ", EezEntity.EEZ_BY_COORDINATE, QueryNameConstants.EEZ_COLUMNS, EezEntity.class),
	RFMO("RFMO", RfmoEntity.RFMO_BY_COORDINATE, QueryNameConstants.RFMO_COLUMNS, RfmoEntity.class),
	PORT("PORT", PortEntity.PORT_BY_COORDINATE, PortEntity.class),
	PORTAREA("PORTAREA", PortAreasEntity.PORT_AREA_BY_COORDINATE, PortAreasEntity.PORT_AREA_BY_ID, PortAreasEntity.class),
	USERAREA("USERAREA", UserAreasEntity.USER_AREA_BY_COORDINATE, QueryNameConstants.USERAREA_COLUMNS, UserAreasEntity.class);
	
	private String type;
	private String nativeQuery;
	private Class entityClass;
	private String namedQuery;
	
	private SpatialTypeEnum(String type, String nativeQuery, Class entityClass) {
		this(type, nativeQuery, "", entityClass);
	}
	
	private SpatialTypeEnum(String type, String nativeQuery, String namedQuery, Class entityClass) {
		this.type = type;
		this.nativeQuery = nativeQuery;
		this.entityClass = entityClass;
		this.namedQuery = namedQuery;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getNativeQuery() {
		return this.nativeQuery;
	}
	
	public String getNamedQuery() {
		return this.namedQuery;
	}
	
	public Class getEntityClass() {
		return this.entityClass;
	}
	
	public static String getNativeQueryByType(String value) {
		for (SpatialTypeEnum areaType : SpatialTypeEnum.values()) {
			if(areaType.getType().equalsIgnoreCase(value)) {
				return areaType.getNativeQuery();
			}
		}
		throw new IllegalArgumentException(value);
	}

}
