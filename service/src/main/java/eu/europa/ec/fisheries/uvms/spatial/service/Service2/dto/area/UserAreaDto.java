/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.area;

import java.io.Serializable;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.SpatialTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAreaDto implements Serializable {

	private String areaType = SpatialTypeEnum.USERAREA.getType();
	private Number gid;
	private String name;
    private String desc;
	private String extent;
    private String owner;

    public UserAreaDto(Number gid, String name, String desc, String extent, String owner) {
        this.owner = owner;
        this.gid = gid;
        this.name = name;
        this.desc = desc;
        this.extent = extent;
    }

}