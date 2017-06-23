/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dto.layer;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.area.AreaLayerDto;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class UserAreaLayerDto extends AreaLayerDto {

    @Builder
    public UserAreaLayerDto(String typeName, String areaTypeDesc, String geoName, String serviceUrl, String serviceType, String style, Boolean isInternal, Boolean isLocation, List<Long> idList) {
        super(typeName, areaTypeDesc, geoName, serviceUrl, serviceType, style, isInternal, isLocation);
        this.idList = idList;
    }

	@JsonInclude
	private List<Long> idList = new ArrayList<>();
	
	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
}