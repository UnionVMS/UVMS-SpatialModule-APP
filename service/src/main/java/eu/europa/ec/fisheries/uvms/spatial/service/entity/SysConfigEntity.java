/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;

@Entity
@Table(name = "system_configurations", schema = "spatial")
@NamedQueries({
        @NamedQuery(name= SysConfigEntity.FIND_CONFIG_BY_NAME,
        query = "FROM SysConfigEntity config WHERE config.name = :name")
})
@EqualsAndHashCode(callSuper = true)
@Data
public class SysConfigEntity extends BaseEntity {

    public static final String FIND_CONFIG_BY_NAME = "sysConfig.findById";
    public static final String FIND_CONFIG = "sysConfig.find";

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="system_configurations_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long id;
	
	
    private String name;

    private String value;

}