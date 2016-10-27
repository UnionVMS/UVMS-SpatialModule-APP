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
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "gfcm")
@NamedQueries({
        @NamedQuery(name = GfcmEntity.DISABLE_GFMC_AREAS, query = "UPDATE GfcmEntity SET enabled = 'N'"),
        @NamedQuery(name = GfcmEntity.GFMC_BY_INTERSECT,
                query = "FROM GfcmEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = GfcmEntity.SEARCH_GFCM, query = "FROM GfcmEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = GfcmEntity.SEARCH_GFCM_NAMES_BY_CODE, query = "From GfcmEntity where code in (SELECT distinct(code) from GfcmEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)"),
        @NamedQuery(name = GfcmEntity.GFCM_COLUMNS, query = "SELECT gfcm.id as gid, gfcm.name AS name, gfcm.code AS code FROM GfcmEntity AS gfcm WHERE gfcm.id in (:ids)")})
public class GfcmEntity extends BaseAreaEntity {

    public static final String DISABLE_GFMC_AREAS = "gfmcEntity.disable";
    public static final String GFMC_BY_INTERSECT = "gfmcEntity.gfmcByIntersect";
    public static final String SEARCH_GFCM = "gfcmEntity.SearchgfcmByNameOrCode";
    public static final String SEARCH_GFCM_NAMES_BY_CODE = "gfcmEntity.searchNamesByCode";
    public static final String GFCM_COLUMNS = "gfcmEntity.gfcmColumns";

    public GfcmEntity() {
        // why JPA why
    }

    public GfcmEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }
}