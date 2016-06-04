package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import java.util.Map;
import org.hibernate.annotations.Where;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.util.Date;
import java.util.Set;

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
                query = "FROM UserAreasEntity userArea WHERE userArea.userName = :userName AND intersects(userArea.geom, :shape) = true AND userArea.enabled = 'Y' GROUP BY userArea.id"),
        @NamedQuery(name = UserAreasEntity.USER_AREA_BY_COORDINATE,
                query = "FROM UserAreasEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = UserAreasEntity.FIND_USER_AREA_BY_ID,
                query = "SELECT area FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection WHERE area.id = :userAreaId AND ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName))"),
        @NamedQuery(name = QueryNameConstants.USERAREA_COLUMNS,
                query = "SELECT userArea.name as name, userArea.areaDesc as desc FROM UserAreasEntity AS userArea WHERE userArea.id =:gid"),
        @NamedQuery(name = QueryNameConstants.FIND_ALL_USER_AREAS,
                query = "SELECT DISTINCT area.id as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity area " +
                        "LEFT JOIN area.scopeSelection scope WHERE area.userName = :userName OR scope.name = :scopeName"),
        @NamedQuery(name = QueryNameConstants.FIND_ALL_USER_AREAS_BY_GIDS,
                query = "SELECT area.id as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity area WHERE area.id IN (:gids)"),
        @NamedQuery(name = UserAreasEntity.FIND_USER_AREA_BY_USER,
                query = "SELECT DISTINCT area " +
                        "FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection " +
                        "WHERE area.type<>'' AND area.type <> null AND ((1=:isPowerUser) " +
                        "OR (area.userName=:userName OR scopeSelection.name=:scopeName))"), // TODO Test distinct (distinct can be deleted in this case)
        @NamedQuery(name = QueryNameConstants.FIND_ALL_USER_AREAS_GROUP,
                query = "SELECT DISTINCT area.type as name FROM UserAreasEntity area " +
                        "LEFT JOIN area.scopeSelection scope WHERE (area.userName = :userName OR (scope.name = :scopeName AND scope.userAreas = area))"),
        @NamedQuery(name = UserAreasEntity.FIND_GID_FOR_SHARED_AREA,
                query = "SELECT area.id FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection WHERE (area.userName <> :userName AND area.type = :type AND scopeSelection.name = :scopeName)"),
        @NamedQuery(name = UserAreasEntity.FIND_BY_USERNAME_AND_NAME,
                query = "FROM UserAreasEntity WHERE userName = :userName AND name = :name)"),
        @NamedQuery(name = UserAreasEntity.DISABLE, query = "UPDATE UserAreasEntity SET enabled = 'N'"),
        @NamedQuery(name = UserAreasEntity.BY_INTERSECT, query = "FROM UserAreasEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = UserAreasEntity.SEARCH_USERAREA, query = "FROM UserAreasEntity where upper(name) like :name OR upper(code) like :code AND enabled='Y' GROUP BY gid")
})
@Where(clause = "enabled = 'Y'")
@Table(name="user_areas", uniqueConstraints = {
        @UniqueConstraint(columnNames={"name", "user_name"})
})
public class UserAreasEntity extends BaseSpatialEntity {

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

    @Column(length = 255)
    @ColumnAliasName(aliasName ="subType")
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    @ColumnAliasName(aliasName ="startDate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    @ColumnAliasName(aliasName ="endDate")
    private Date endDate;

    @Column(name = "user_name", nullable = false, length = 255)
    private String userName;

    @Column(columnDefinition = "text", name = "area_desc")
    @ColumnAliasName(aliasName ="areaDesc")
    private String areaDesc;

    @Column(columnDefinition = "text", name = "dataset_name", unique = true)
    @ColumnAliasName(aliasName ="datasetName")
    private String datasetName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false)
    @ColumnAliasName(aliasName ="createdOn")
    private Date createdOn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAreas", cascade = CascadeType.MERGE, orphanRemoval = true)
    @ColumnAliasName(aliasName ="scopeSelection")
    private Set<UserScopeEntity> scopeSelection;

    public UserAreasEntity() {
        // why JPA why
    }

    public UserAreasEntity(Map<String, Object> values) throws ServiceException {
        super(values, null);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAreaDesc() {
        return this.areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<UserScopeEntity> getScopeSelection() {
        return scopeSelection;
    }

    public void setScopeSelection(Set<UserScopeEntity> scopeSelection) {
        if (scopeSelection != null) {
            for (UserScopeEntity userScopeEntity : scopeSelection) {
                userScopeEntity.setUserAreas(this);
            }
            this.scopeSelection = scopeSelection;
        }
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

}
