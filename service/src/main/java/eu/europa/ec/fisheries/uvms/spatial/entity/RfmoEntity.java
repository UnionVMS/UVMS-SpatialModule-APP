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
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = RfmoEntity.RFMO_BY_COORDINATE,
                query = "FROM RfmoEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = RfmoEntity.RFMO_COLUMNS,
                query = "SELECT rfmo.id as gid, rfmo.name AS name, rfmo.code AS code FROM RfmoEntity AS rfmo WHERE rfmo.id in (:ids)"),
        @NamedQuery(name = RfmoEntity.DISABLE_RFMO_AREAS,
                query = "UPDATE RfmoEntity SET enabled = 'N'"),
        @NamedQuery(name = RfmoEntity.SEARCH_RFMO, query = "FROM RfmoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = RfmoEntity.SEARCH_RFMO_NAMES_BY_CODE, query = "From RfmoEntity where code in (SELECT distinct(code) from RfmoEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)")
})
@Table(name = "rfmo")
public class RfmoEntity extends BaseSpatialEntity {

    public static final String RFMO_BY_COORDINATE = "rfmoEntity.ByCoordinate";
    public static final String DISABLE_RFMO_AREAS = "rfmoEntity.disableRfmoAreas";
    public static final String SEARCH_RFMO = "rfmoEntity.searchRfmoByNameOrCode";
    public static final String SEARCH_RFMO_NAMES_BY_CODE = "rfmoEntity.searchNamesByCode";
    public static final String RFMO_COLUMNS = "rfmoEntity.findSelectedColumns";

    private static final String TUNA = "tuna";

    @Column(length = 10)
    @ColumnAliasName(aliasName = TUNA)
    private String tuna;

    public RfmoEntity() {
        // why JPA why
    }

    public RfmoEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }

    public String getTuna() {
        return this.tuna;
    }

    public void setTuna(String tuna) {
        this.tuna = tuna;
    }

}