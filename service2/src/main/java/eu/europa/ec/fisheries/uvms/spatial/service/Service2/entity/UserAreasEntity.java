/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity;

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
        @NamedQuery(name = UserAreasEntity.FIND_USER_AREA_BY_USERNAME_SCOPE_AND_POWERUSER,
                query = "SELECT distinct area FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE ((true=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName)) " +
                        "GROUP BY area.id"),
        @NamedQuery(name = UserAreasEntity.FIND_BY_USER_NAME_AND_SCOPE_NAME,
                query = "SELECT distinct area FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE area.userName = :userName OR scopeSelection.name = :scopeName"),
        @NamedQuery(name = UserAreasEntity.USER_AREA_DETAILS_BY_LOCATION,
                query = "FROM UserAreasEntity2 userArea WHERE intersects(userArea.geom, :shape) = true AND userArea.enabled = true GROUP BY userArea.id"),
        @NamedQuery(name = UserAreasEntity.FIND_ALL_USER_AREAS,
                query = "SELECT DISTINCT area.id as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity2 area LEFT JOIN area.scopeSelection scope " +
                        "WHERE area.userName = :userName OR scope.name = :scopeName"),
        @NamedQuery(name = UserAreasEntity.AREA_BY_AREA_CODES, query = "From UserAreasEntity2 where code in :code AND enabled=true ")
})
@Where(clause = "enabled = true")
@Table(name = "user_areas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "user_name"})
})
public class UserAreasEntity extends BaseAreaEntity {

    public static final String FIND_ALL_USER_AREAS = "userArea2.findAllUserAreas";
    public static final String USER_AREA_DETAILS_BY_LOCATION = "UserArea2.findUserAreaDetailsByLocation";
    public static final String FIND_USER_AREA_BY_USERNAME_SCOPE_AND_POWERUSER = "UserArea2.findUserAreaByUsernameScopeAndPowerUser";
    public static final String FIND_BY_USER_NAME_AND_SCOPE_NAME = "UserArea2.findGidByUserNameOrScope";
    public static final String AREA_BY_AREA_CODES = "userAreasEntity2.areaByAreaCodes";

    @Id
    @Column(name = "gid")
    @SequenceGenerator(name = "SEQ_USERAREA_GEN", sequenceName = "user_areas_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERAREA_GEN")
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

    public UserAreasEntity(String type, Instant startDate, Instant endDate, String userName, String areaDesc,
                           String datasetName, Instant createdOn, Set<UserScopeEntity> scopeSelection,
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

    public Set<UserScopeEntity> getScopeSelection() {
        return scopeSelection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserAreasEntity that = (UserAreasEntity) o;
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