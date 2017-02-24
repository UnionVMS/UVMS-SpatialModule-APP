/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

import org.hibernate.validator.constraints.ScriptAssert;

import com.fasterxml.jackson.annotation.JsonProperty;

@ScriptAssert(script="_this.isInputValid(_this.id,_this.crs,_this.longitude,_this.latitude)", lang = "javascript")
public class GeoCoordinateType {

	protected String id;
	
	protected Double longitude;
	
	protected Double latitude;
	
	protected Integer crs;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty("gid")
	public String getGid() {
		return id;
	}

	@JsonProperty("gid")
	public void setGid(String gid) {
		this.id = gid;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getCrs() {
		return crs;
	}

	public void setCrs(Integer crs) {
		this.crs = crs;
	}
	
	public boolean isInputValid(Object id, Object crs, Object longitude, Object latitude) {
		if (id != null) {
			return true;
		} else if (crs != null && longitude != null && latitude != null) {
			return true;
		}
		return false;
	}
}