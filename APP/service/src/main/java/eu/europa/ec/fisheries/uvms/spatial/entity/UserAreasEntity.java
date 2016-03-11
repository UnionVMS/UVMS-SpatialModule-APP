package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
//@SqlResultSetMappings({
//        @SqlResultSetMapping(name = "implicit.userarea", entities = @EntityResult(entityClass = UserAreasEntity.class))
//})
@NamedQueries({
        @NamedQuery(name = UserAreasEntity.USER_AREA_DETAILS_BY_LOCATION, query = "FROM UserAreasEntity userArea WHERE userArea.userName = :userName AND intersects(userArea.geom, :shape) = true) AND userArea.enabled = 'Y' GROUP BY userArea.gid"),
        @NamedQuery(name = UserAreasEntity.USERAREA_BY_COORDINATE, query = "FROM UserAreasEntity WHERE intersects(geom, :shape) = true) AND enabled = 'Y'"),
        @NamedQuery(name = QueryNameConstants.FIND_GID_BY_USER,
                query = "SELECT area.gid FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection WHERE area.userName = :userName OR scopeSelection.name = :scopeName"),
        @NamedQuery(name = QueryNameConstants.FIND_USER_AREA_BY_ID,
                query = "SELECT area FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection WHERE area.gid = :userAreaId AND ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName))"),
        @NamedQuery(name = QueryNameConstants.USERAREA_COLUMNS,
				query = "select userArea.name as name, userArea.areaDesc as desc from UserAreasEntity as userArea where userArea.gid =:gid"),
		@NamedQuery(name = QueryNameConstants.FIND_ALL_USER_AREAS,
				query = "SELECT area.gid as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity area WHERE area.userName = :userName"),
        @NamedQuery(name = QueryNameConstants.FIND_ALL_USER_AREAS_BY_GIDS,
                query = "SELECT area.gid as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity area WHERE area.gid in (:gids)"),
        @NamedQuery(name = QueryNameConstants.FIND_USER_AREA_TYPES,
                query = "SELECT DISTINCT area.type FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection WHERE  area.type<>'' AND area.type <> null AND ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName))"),
        @NamedQuery(name = QueryNameConstants.FIND_USER_AREA_BY_TYPE,
                query = "SELECT area FROM UserAreasEntity area LEFT JOIN area.scopeSelection scopeSelection WHERE area.type = :type AND ((1=:isPowerUser) OR (area.userName=:userName OR scopeSelection.name=:scopeName)) GROUP BY area.gid"),
        @NamedQuery(name = QueryNameConstants.FIND_ALL_USER_AREAS_GROUP,
        query = "SELECT distinct(area.type) as name FROM UserAreasEntity area WHERE area.userName = :userName")

        })
@NamedNativeQueries({
//        @NamedNativeQuery(
//                name = QueryNameConstants.USER_AREA_DETAILS_WITH_EXTENT_BY_LOCATION,
//                query = "select gid, name, area_desc as desc, CAST(st_astext(st_extent(geom))AS TEXT) as extent from spatial.user_areas"
//                        + " WHERE user_name=:userName"
//                        + " AND st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) group by gid"),
//        @NamedNativeQuery(
//                name = QueryNameConstants.USER_AREA_DETAILS_BY_LOCATION,
//                query = "select * from spatial.user_areas"
//                        + " WHERE user_name=:userName"
//                        + " AND st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) group by gid", resultSetMapping = "implicit.userarea"),
        @NamedNativeQuery(
                name = QueryNameConstants.SEARCH_USER_AREA,
                query = "select gid, name, area_desc as desc, CAST(st_astext(st_extent(geom))AS TEXT) as extent FROM spatial.user_areas area LEFT JOIN spatial.user_scope scopeSelection"
                        + " ON area.gid = scopeSelection.user_area_id"
                        + " WHERE ((1=:isPowerUser) OR (area.user_name=:userName OR scopeSelection.scope_name=:scopeName))"
                        + " AND (UPPER(area.name) LIKE(UPPER(:name)) OR UPPER(area.area_desc) LIKE(UPPER(:desc))) group by area.gid"),
       // @NamedNativeQuery(
       //         name = UserAreasEntity.USERAREA_BY_COORDINATE,
       //         query = "select * from user_areas where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs))", resultSetMapping = "implicit.userarea")

})
@Where(clause = "enabled = 'Y'")
@Table(name = "user_areas", schema = "spatial")
public class UserAreasEntity implements Serializable {

    public static final String USER_AREA_DETAILS_BY_LOCATION = "UserArea.findUserAreaDetailsByLocation";
    public static final String USERAREA_BY_COORDINATE = "userAreasEntity.ByCoordinate";

    public static final String BUFFER = "UserAreas.buffer";

    @Id
    @Column(name = "gid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnAliasName(aliasName ="id")
    private long gid;

    @Column(name = "type", length = 255)
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

    @Column(name = "name", nullable = false, length = 255)
    @ColumnAliasName(aliasName ="name")
    private String name;

    @Column(columnDefinition = "text", name = "area_desc")
    @ColumnAliasName(aliasName ="areaDesc")
    private String areaDesc;

    @Column(columnDefinition = "text", name = "dataset_name", unique = true)
    @ColumnAliasName(aliasName ="datasetName")
    private String datasetName;

    @Basic
    @Column(name = "geom", nullable = false)
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName ="geometry")
    private Geometry geom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false)
    @ColumnAliasName(aliasName ="createdOn")
    private Date createdOn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAreas", cascade = CascadeType.ALL)
    private Set<AreaStatusEntity> areaStatuses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAreas", cascade = CascadeType.ALL, orphanRemoval = true)
    @ColumnAliasName(aliasName ="scopeSelection")
    private Set<UserScopeEntity> scopeSelection;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "enabled", nullable = false, length = 1)
    @ColumnAliasName(aliasName ="enabled")
    private Boolean enabled = true;

    public UserAreasEntity() {
    }

    public long getGid() {
        return this.gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaDesc() {
        return this.areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public Geometry getGeom() {
        return this.geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Set<AreaStatusEntity> getAreaStatuses() {
        return this.areaStatuses;
    }

    public void setAreaStatuses(Set<AreaStatusEntity> areaStatuses) {
        this.areaStatuses = areaStatuses;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
