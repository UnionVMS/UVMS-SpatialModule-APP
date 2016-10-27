/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = PortAreasEntity.PORT_AREA_BY_COORDINATE,
                query = "FROM PortAreasEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = PortAreasEntity.DISABLE_PORT_AREAS, query = "UPDATE PortAreasEntity SET enabled = 'N'"),
        @NamedQuery(name = PortAreasEntity.SEARCH_PORTAREAS, query = "FROM PortAreasEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = PortAreasEntity.SEARCH_PORT_AREA_NAMES_BY_CODE, query = "From PortAreasEntity where code in (SELECT distinct(code) from PortAreasEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)"),
        @NamedQuery(name = PortAreasEntity.PORTAREA_COLUMNS, query = "SELECT portarea.id as gid, portarea.name AS name, portarea.code AS code FROM PortAreasEntity AS portarea WHERE portarea.id in (:ids)")})
@Table(name = "port_area")
public class PortAreasEntity extends BaseAreaEntity {

    public static final String PORT_AREA_BY_COORDINATE = "portEntity.PortAreaByCoordinate";
    public static final String DISABLE_PORT_AREAS = "portAreasEntity.disablePortAreas";
    public static final String SEARCH_PORTAREAS = "portAreaEntity.searchPortAreaByNameOrCode";
    public static final String SEARCH_PORT_AREA_NAMES_BY_CODE = "portAreaEntity.searchNamesByCode";
    public static final String PORTAREA_COLUMNS = "portAreaEntity.portAreaColumns";

    public PortAreasEntity() {
        // why JPA why
    }

    public PortAreasEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }
}