/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.entity.config;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "system_configurations", schema = "spatial")
@EqualsAndHashCode(callSuper = true)
@NamedQueries({
        @NamedQuery(name= SysConfigEntity.FIND_CONFIG_BY_NAME,
        query = "FROM SysConfigEntity config WHERE config.name = :name")
})
public class SysConfigEntity extends BaseEntity {

    public static final String FIND_CONFIG_BY_NAME = "SysConfig.findConfigById";
    public static final String FIND_CONFIG = "SysConfig.findConfig";

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}