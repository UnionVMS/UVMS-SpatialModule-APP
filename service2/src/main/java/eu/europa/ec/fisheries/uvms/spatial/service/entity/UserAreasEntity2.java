/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;


@Entity
@NamedQueries({
        @NamedQuery(name = UserAreasEntity2.SEARCH_BY_CRITERIA,
                query = "SELECT area " +
                        "FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName)) " +
                        "AND (UPPER(area.name) LIKE UPPER(:searchCriteria) OR UPPER(area.areaDesc) LIKE UPPER(:searchCriteria))" +
                        "GROUP BY area.id"),
        @NamedQuery(name = UserAreasEntity2.FIND_USER_AREA_BY_TYPE,
                query = "SELECT area " +
                        "FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE area.type = :type " +
                        "AND ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName)) " +
                        "GROUP BY area.id"),
        @NamedQuery(name = UserAreasEntity2.FIND_BY_USER_NAME_AND_SCOPE_NAME,
                query = "SELECT area FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE area.userName = :userName OR scopeSelection.name = :scopeName"),
        @NamedQuery(name = UserAreasEntity2.USER_AREA_DETAILS_BY_LOCATION,
                query = "FROM UserAreasEntity2 userArea WHERE intersects(userArea.geom, :shape) = true AND userArea.enabled = true GROUP BY userArea.id"),
        @NamedQuery(name = UserAreasEntity2.USER_AREA_BY_COORDINATE,
                query = "FROM UserAreasEntity2 WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = UserAreasEntity2.FIND_USER_AREA_BY_ID,
                query = "SELECT area FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scopeSelection WHERE area.id = :userAreaId AND ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName))"),
        @NamedQuery(name = UserAreasEntity2.USERAREA_COLUMNS,
                query = "SELECT userArea.id as gid, userArea.name as name, userArea.areaDesc as desc FROM UserAreasEntity AS userArea WHERE userArea.id in (:ids)"),
        @NamedQuery(name = UserAreasEntity2.FIND_ALL_USER_AREAS,
                query = "SELECT DISTINCT area.id as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity2 area " +
                        "LEFT JOIN area.scopeSelection scope WHERE area.userName = :userName OR scope.name = :scopeName"),
        @NamedQuery(name = UserAreasEntity2.FIND_ALL_USER_AREAS_BY_GIDS,
                query = "SELECT area.id as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity2 area WHERE area.id IN (:gids)"),
        @NamedQuery(name = UserAreasEntity2.FIND_USER_AREA_BY_USER,
                query = "SELECT DISTINCT area " +
                        "FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE area.type<>'' AND area.type <> NULL AND ((1=:isPowerUser) " +
                        "OR (area.userName=:userName OR scopeSelection.name=:scopeName))"), // TODO Test distinct (distinct can be deleted in this case)
        @NamedQuery(name = UserAreasEntity2.FIND_ALL_USER_AREAS_GROUP,
                query = "SELECT DISTINCT area.type as name FROM UserAreasEntity2 area " +
                        "LEFT JOIN area.scopeSelection scope WHERE (area.userName = :userName OR (scope.name = :scopeName AND scope.userAreas = area))"),
        @NamedQuery(name = UserAreasEntity2.FIND_GID_FOR_SHARED_AREA,
                query = "SELECT area.id FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scopeSelection WHERE (area.userName <> :userName AND area.type = :type AND scopeSelection.name = :scopeName)"),
        @NamedQuery(name = UserAreasEntity2.FIND_BY_USERNAME_AND_NAME,
                query = "FROM UserAreasEntity WHERE userName = :userName AND name = :name"),
        @NamedQuery(name = UserAreasEntity2.DISABLE, query = "UPDATE UserAreasEntity2 SET enabled = false"),
        @NamedQuery(name = UserAreasEntity2.BY_INTERSECT, query = "FROM UserAreasEntity2 WHERE intersects(geom, :shape) = true AND enabled = true"),
        @NamedQuery(name = UserAreasEntity2.SEARCH_USERAREA, query = "FROM UserAreasEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = UserAreasEntity2.SEARCH_USERAREA_NAMES_BY_CODE, query = "From UserAreasEntity2 where code in (SELECT distinct(code) from UserAreasEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid)"),
        @NamedQuery(name = UserAreasEntity2.UPDATE_USERAREA_FORUSER_AND_SCOPE,
                query = "update UserAreasEntity2 userarea " +
                        "set userarea.startDate = :startDate, userarea.endDate = :endDate " +
                        "where userarea.type = :type"),
        @NamedQuery(name = UserAreasEntity2.UPDATE_USERAREA_FORUSER,
                query = "update UserAreasEntity2 userarea " +
                        "set userarea.startDate = :startDate, userarea.endDate = :endDate " +
                        "where userarea.userName = :userName and userarea.type = :type")
})
@Where(clause = "enabled = true")
@Table(name = "user_areas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "user_name"})
})
public class UserAreasEntity2 extends BaseAreaEntity2 {

    public static final String FIND_ALL_USER_AREAS = "userArea2.findAllUserAreas";
    public static final String FIND_ALL_USER_AREAS_GROUP = "userArea2.findAllUserAreaGroup";
    public static final String FIND_ALL_USER_AREAS_BY_GIDS = "userAreas2.findAllUserAreasByGid";
    public static final String USER_AREA_DETAILS_BY_LOCATION = "UserArea2.findUserAreaDetailsByLocation";
    public static final String USER_AREA_BY_COORDINATE = "userAreasEntity2.ByCoordinate";
    public static final String FIND_GID_FOR_SHARED_AREA = "userAreasEntity2.findGidForSharedAreas";
    public static final String SEARCH_BY_CRITERIA = "userAreasEntity2.searchByCriteria";
    public static final String FIND_BY_USERNAME_AND_NAME = "userAreasEntity2.findByUserNameAndName";
    public static final String FIND_USER_AREA_BY_ID = "UserArea2.findUserAreaById";
    public static final String FIND_USER_AREA_BY_TYPE = "UserArea2.findUserAreaByType";
    public static final String FIND_USER_AREA_BY_USER = "UserArea2.findUserAreaTypes";
    public static final String FIND_BY_USER_NAME_AND_SCOPE_NAME = "UserArea2.findGidByUserNameOrScope";
    public static final String DISABLE = "userArea2.disable";
    public static final String BY_INTERSECT = "userArea2.byIntersect";
    public static final String SEARCH_USERAREA = "userAreaEntity2.searchUserAreaByNameOrCode";
    public static final String SEARCH_USERAREA_NAMES_BY_CODE = "userAreaEntity2.searchNamesByCode";
    public static final String UPDATE_USERAREA_FORUSER_AND_SCOPE = "userAreaEntity2.updateUserAreaForUserAndScope";
    public static final String UPDATE_USERAREA_FORUSER = "userAreaEntity2.updateUserAreaForUser";
    public static final String USERAREA_COLUMNS = "userAreasEntity2.findSelectedColumns";

    @Id
    @Column(name = "gid")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "user_areas_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @JsonProperty("gid")
    private Long id;

    @Column(name = "type")
    @JsonProperty("subType")
    private String type;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(columnDefinition = "text", name = "area_desc")
    private String areaDesc;

    @Column(columnDefinition = "text", name = "dataset_name")
    private String datasetName;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAreas", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<UserScopeEntity2> scopeSelection = new HashSet<>();

    @JsonProperty("scopeSelection")
    public List<String> getScopeSelectionAsString(){
        List<String> list = new ArrayList<>();
        if (isNotEmpty(scopeSelection)){
            for (UserScopeEntity2 entity :scopeSelection){
                list.add(entity.getName());
            }
        }
        return list;
    }

    public UserAreasEntity2(String type, Instant startDate, Instant endDate, String userName, String areaDesc,
                            String datasetName, Instant createdOn, Set<UserScopeEntity2> scopeSelection,
                            Geometry geom, String name, String code, boolean enabled, Instant enabledOn) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userName = userName;
        this.areaDesc = areaDesc;
        this.datasetName = datasetName;
        this.createdOn = createdOn;
        this.scopeSelection = scopeSelection;
        this.geom = geom;
        this.name = name;
        this.code = code;
        this.enabled = enabled;
        this.enabledOn = enabledOn;
    }


    public void addUserScope(UserScopeEntity2 scope) {
        scopeSelection.add(scope);
        scope.setUserAreas(this);
    }

    public void removeScope(UserScopeEntity2 scope) {
        scopeSelection.remove(scope);
        scope.setUserAreas(null);
    }

    public void setScopeSelection(Set<UserScopeEntity2> scopeSelection) {
        if (scopeSelection != null) {
            for (UserScopeEntity2 userScopeEntity : scopeSelection) {
                userScopeEntity.setUserAreas(this);
            }
            this.scopeSelection = scopeSelection;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAreaDesc() {
        return areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Set<UserScopeEntity2> getScopeSelection() {
        return scopeSelection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserAreasEntity2 that = (UserAreasEntity2) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(areaDesc, that.areaDesc) &&
                Objects.equals(datasetName, that.datasetName) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(scopeSelection, that.scopeSelection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, type, startDate, endDate, userName, areaDesc, datasetName, createdOn, scopeSelection);
    }
}