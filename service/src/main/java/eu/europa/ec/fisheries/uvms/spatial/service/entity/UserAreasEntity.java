/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import static eu.europa.ec.fisheries.uvms.commons.date.DateUtils.DATE_TIME_UI_FORMAT;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson.UserAreaGeoJsonDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;


@Entity
@NamedQueries({
        @NamedQuery(name = UserAreasEntity.SEARCH_BY_CRITERIA,
                query = "SELECT area " +
                        "FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName)) " +
                        "AND (UPPER(area.name) LIKE UPPER(:searchCriteria) OR UPPER(area.areaDesc) LIKE UPPER(:searchCriteria))" +
                        "GROUP BY area.id"),
        @NamedQuery(name = UserAreasEntity.FIND_USER_AREA_BY_TYPE,
                query = "SELECT area " +
                        "FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE area.type = :type " +
                        "AND ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName)) " +
                        "GROUP BY area.id"),
        @NamedQuery(name = UserAreasEntity.FIND_BY_USER_NAME_AND_SCOPE_NAME,
                query = "SELECT area FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE area.userName = :userName OR scopeSelection.name = :scopeName"),
        @NamedQuery(name = UserAreasEntity.USER_AREA_DETAILS_BY_LOCATION,
                query = "FROM UserAreasEntity userArea WHERE intersects(userArea.geom, :shape) = true AND userArea.enabled = 'Y' GROUP BY userArea.id"),
        @NamedQuery(name = UserAreasEntity.USER_AREA_BY_COORDINATE,
                query = "FROM UserAreasEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = UserAreasEntity.FIND_USER_AREA_BY_ID,
                query = "SELECT area FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection WHERE area.id = :userAreaId AND ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName))"),
        @NamedQuery(name = UserAreasEntity.USERAREA_COLUMNS,
                query = "SELECT userArea.id as gid, userArea.name as name, userArea.areaDesc as desc FROM UserAreasEntity AS userArea WHERE userArea.id in (:ids)"),
        @NamedQuery(name = UserAreasEntity.FIND_ALL_USER_AREAS,
                query = "SELECT DISTINCT area.id as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity area " +
                        "LEFT JOIN area.scopeSelection scope WHERE area.userName = :userName OR scope.name = :scopeName"),
        @NamedQuery(name = UserAreasEntity.FIND_ALL_USER_AREAS_BY_GIDS,
                query = "SELECT area.id as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity area WHERE area.id IN (:gids)"),
        @NamedQuery(name = UserAreasEntity.FIND_USER_AREA_BY_USER,
                query = "SELECT DISTINCT area " +
                        "FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE area.type<>'' AND area.type <> NULL AND ((1=:isPowerUser) " +
                        "OR (area.userName=:userName OR scopeSelection.name=:scopeName))"), // TODO Test distinct (distinct can be deleted in this case)
        @NamedQuery(name = UserAreasEntity.FIND_ALL_USER_AREAS_GROUP,
                query = "SELECT DISTINCT area.type as name FROM UserAreasEntity area " +
                        "LEFT JOIN area.scopeSelection scope WHERE (area.userName = :userName OR (scope.name = :scopeName AND scope.userAreas = area))"),
        @NamedQuery(name = UserAreasEntity.FIND_GID_FOR_SHARED_AREA,
                query = "SELECT area.id FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection WHERE (area.userName <> :userName AND area.type = :type AND scopeSelection.name = :scopeName)"),
        @NamedQuery(name = UserAreasEntity.FIND_BY_USERNAME_AND_NAME,
                query = "FROM UserAreasEntity WHERE userName = :userName AND name = :name"),
        @NamedQuery(name = UserAreasEntity.DISABLE, query = "UPDATE UserAreasEntity SET enabled = 'N'"),
        @NamedQuery(name = UserAreasEntity.BY_INTERSECT, query = "FROM UserAreasEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = UserAreasEntity.SEARCH_USERAREA, query = "FROM UserAreasEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = UserAreasEntity.SEARCH_USERAREA_NAMES_BY_CODE, query = "From UserAreasEntity where code in (SELECT distinct(code) from UserAreasEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)"),
        @NamedQuery(name = UserAreasEntity.UPDATE_USERAREA_FORUSER_AND_SCOPE,
                query = "update UserAreasEntity userarea " +
                        "set userarea.startDate = :startDate, userarea.endDate = :endDate " +
                        "where userarea.type = :type"),
        @NamedQuery(name = UserAreasEntity.UPDATE_USERAREA_FORUSER,
                query = "update UserAreasEntity userarea " +
                        "set userarea.startDate = :startDate, userarea.endDate = :endDate " +
                        "where userarea.userName = :userName and userarea.type = :type")
})
@Where(clause = "enabled = 'Y'")
@Table(name = "user_areas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "user_name"})
})
@EqualsAndHashCode(callSuper = true, exclude = "scopeSelection")
@Data
@NoArgsConstructor
public class UserAreasEntity extends BaseAreaEntity {

    public static final String FIND_ALL_USER_AREAS = "userArea.findAllUserAreas";
    public static final String FIND_ALL_USER_AREAS_GROUP = "userArea.findAllUserAreaGroup";
    public static final String FIND_ALL_USER_AREAS_BY_GIDS = "userAreas.findAllUserAreasByGid";
    public static final String USER_AREA_DETAILS_BY_LOCATION = "UserArea.findUserAreaDetailsByLocation";
    public static final String USER_AREA_BY_COORDINATE = "userAreasEntity.ByCoordinate";
    public static final String FIND_GID_FOR_SHARED_AREA = "userAreasEntity.findGidForSharedAreas";
    public static final String SEARCH_BY_CRITERIA = "userAreasEntity.searchByCriteria";
    public static final String FIND_BY_USERNAME_AND_NAME = "userAreasEntity.findByUserNameAndName";
    public static final String FIND_USER_AREA_BY_ID = "UserArea.findUserAreaById";
    public static final String FIND_USER_AREA_BY_TYPE = "UserArea.findUserAreaByType";
    public static final String FIND_USER_AREA_BY_USER = "UserArea.findUserAreaTypes";
    public static final String FIND_BY_USER_NAME_AND_SCOPE_NAME = "UserArea.findGidByUserNameOrScope";
    public static final String DISABLE = "userArea.disable";
    public static final String BY_INTERSECT = "userArea.byIntersect";
    public static final String SEARCH_USERAREA = "userAreaEntity.searchUserAreaByNameOrCode";
    public static final String SEARCH_USERAREA_NAMES_BY_CODE = "userAreaEntity.searchNamesByCode";
    public static final String UPDATE_USERAREA_FORUSER_AND_SCOPE = "userAreaEntity.updateUserAreaForUserAndScope";
    public static final String UPDATE_USERAREA_FORUSER = "userAreaEntity.updateUserAreaForUser";
    public static final String USERAREA_COLUMNS = "userAreasEntity.findSelectedColumns";

    @Id
    @Column(name = "gid")
    @SequenceGenerator(name = "SEQ_GEN_UserAreasEntity", sequenceName = "user_areas_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN_UserAreasEntity")
    @JsonProperty("gid")
    private Long id;

    @Column(name = "type")
    @JsonProperty("subType")
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_TIME_UI_FORMAT)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_TIME_UI_FORMAT)
    private Date endDate;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(columnDefinition = "text", name = "area_desc")
    private String areaDesc;

    @Column(columnDefinition = "text", name = "dataset_name")
    private String datasetName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_TIME_UI_FORMAT)
    private Date createdOn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAreas", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<UserScopeEntity> scopeSelection = new HashSet<>();

    @JsonProperty("scopeSelection")
    public List<String> getScopeSelectionAsString(){
        List<String> list = new ArrayList<>();
        if (isNotEmpty(scopeSelection)){
            for (UserScopeEntity entity :scopeSelection){
                list.add(entity.getName());
            }
        }
        return list;
    }

    @Builder
    private UserAreasEntity(String type, Date startDate, Date endDate, String userName, String areaDesc,
                            String datasetName, Date createdOn, Set<UserScopeEntity> scopeSelection,
                            Geometry geom, String name, String code, boolean enabled, Date enabledOn) {
        super(geom, name, code, enabled, enabledOn);
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userName = userName;
        this.areaDesc = areaDesc;
        this.datasetName = datasetName;
        this.createdOn = createdOn;
        this.scopeSelection = scopeSelection;
    }

    public UserAreasEntity(Map<String, Object> values) throws ServiceException {
        super(values, null);
    }

    public void addUserScope(UserScopeEntity scope) {
        scopeSelection.add(scope);
        scope.setUserAreas(this);
    }

    public void removeScope(UserScopeEntity scope) {
        scopeSelection.remove(scope);
        scope.setUserAreas(null);
    }

    public void setScopeSelection(Set<UserScopeEntity> scopeSelection) {
        if (scopeSelection != null) {
            for (UserScopeEntity userScopeEntity : scopeSelection) {
                userScopeEntity.setUserAreas(this);
            }
            this.scopeSelection = scopeSelection;
        }
    }

    public void merge(UserAreaGeoJsonDto dto) {
        if (dto == null) {
            return;
        }
        this.setAreaDesc(dto.getDesc());
        this.setType(dto.getSubType());
        this.setGeom(dto.getGeometry());
        this.setName(dto.getName());
        this.setId(dto.getId());
        List<String> scopeSelectionDto = dto.getScopeSelection();

        scopeSelection.clear();

        if (isNotEmpty(scopeSelectionDto)){
            for (String scope : scopeSelectionDto){
                UserScopeEntity userScopeEntity = new UserScopeEntity();
                userScopeEntity.setName(scope);
                this.addUserScope(userScopeEntity);
            }
        }

        try {
            if (dto.getStartDate() != null) {
                dto.setStartDate(new SimpleDateFormat(DATE_TIME_UI_FORMAT).parse(dto.getStartDate()));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            if (dto.getEndDate() != null) {

                dto.setEndDate(new SimpleDateFormat(DATE_TIME_UI_FORMAT).parse(dto.getEndDate()));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.setDatasetName(dto.getDatasetName());
    }
}