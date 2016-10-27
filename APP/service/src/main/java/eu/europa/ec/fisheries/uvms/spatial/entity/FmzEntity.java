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
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fmz")
@NamedQueries({
        @NamedQuery(name = FmzEntity.DISABLE, query = "UPDATE FmzEntity SET enabled = 'N'"),
        @NamedQuery(name = FmzEntity.BY_INTERSECT,
                query = "FROM FmzEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = FmzEntity.SEARCH_FMZ, query = "FROM FmzEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = FmzEntity.SEARCH_FMZ_NAMES_BY_CODE, query = "From FmzEntity where code in (SELECT distinct(code) from FmzEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)"),
        @NamedQuery(name = FmzEntity.FMZ_COLUMNS, query = "SELECT fmz.id as gid, fmz.name AS name, fmz.code AS code FROM FmzEntity AS fmz WHERE fmz.id in (:ids)")
})
public class FmzEntity extends BaseAreaEntity {

    public static final String DISABLE = "fmzEntity.disable";
    public static final String BY_INTERSECT = "fmzEntity.byIntersect";
    public static final String SEARCH_FMZ = "fmzEntity.SearcgFmzByNameOrCode";
    public static final String SEARCH_FMZ_NAMES_BY_CODE = "fmzEntity.searchNamesByCode";
    public static final String FMZ_COLUMNS = "fmzEntity.fmzColumns";

    private static final String FMZ_ID = "fmz_id";
    private static final String EDITED = "edited";

    @Column(name = "fmz_id")
    @ColumnAliasName(aliasName = FMZ_ID)
    private Long fmzId;

    @Column(name = "edited")
    @ColumnAliasName(aliasName = EDITED)
    private String edited;

    public FmzEntity() {
        // why JPA why
    }

    public FmzEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }


    public Long getFmzId() {
        return fmzId;
    }

    public void setFmzId(Long fmzId) {
        this.fmzId = fmzId;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }
}