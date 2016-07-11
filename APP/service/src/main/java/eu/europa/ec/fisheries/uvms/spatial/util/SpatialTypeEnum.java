/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
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
	PORTAREA("PORTAREA", PortAreasEntity.PORT_AREA_BY_COORDINATE, null, PortAreasEntity.class),
	USERAREA("USERAREA", UserAreasEntity.USER_AREA_BY_COORDINATE, QueryNameConstants.USERAREA_COLUMNS, UserAreasEntity.class);
	
	private String type;
	private String namedQuery;
	
	private SpatialTypeEnum(String type, String nativeQuery, Class entityClass) {
		this(type, nativeQuery, "", entityClass);
	}
	
	private SpatialTypeEnum(String type, String nativeQuery, String namedQuery, Class entityClass) {
		this.type = type;
		this.namedQuery = namedQuery;
	}
	
	public String getType() {
		return this.type;
	}

	public String getNamedQuery() {
		return this.namedQuery;
	}

}