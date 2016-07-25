/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.util;

import eu.europa.ec.fisheries.uvms.spatial.entity.*;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;

public enum SpatialTypeEnum {
	
	EEZ("EEZ", EezEntity.EEZ_COLUMNS),
	RFMO("RFMO", RfmoEntity.RFMO_COLUMNS),
	FAO("FAO", FaoEntity.FAO_COLUMNS),
	FMZ("FMZ", FmzEntity.FMZ_COLUMNS),
	GFCM("GFCM", GfcmEntity.GFCM_COLUMNS),
	STATRECT("STATRECT", StatRectEntity.STATRECT_COLUMNS),
	PORTAREA("PORTAREA", PortAreasEntity.PORTAREA_COLUMNS),
	USERAREA("USERAREA", UserAreasEntity.USERAREA_COLUMNS);
	
	private String type;
	private String namedQuery;
	
	SpatialTypeEnum(String type, String namedQuery) {
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