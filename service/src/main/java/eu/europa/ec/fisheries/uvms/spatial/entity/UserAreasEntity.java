package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "implicit.userarea", entities = @EntityResult(entityClass = UserAreasEntity.class))
})
@NamedQueries({
        @NamedQuery(name = QueryNameConstants.FIND_GID_BY_USER,
                query = "SELECT area.gid FROM UserAreasEntity area WHERE area.userName = :userName OR area.scopeName = :scopeName AND area.isShared = 'Y'"),
        @NamedQuery(name = QueryNameConstants.FIND_USER_AREA_BY_ID,
                query = "SELECT area FROM UserAreasEntity area WHERE area.userName = :userName AND area.scopeName = :scopeName AND area.gid = :userAreaId"),
        @NamedQuery(name = QueryNameConstants.USERAREA_COLUMNS,
				query = "select userArea.name as name, userArea.areaDesc as desc from UserAreasEntity as userArea where userArea.gid =:gid"),
		@NamedQuery(name = QueryNameConstants.FIND_ALL_USER_AREAS,
				query = "SELECT area.gid as gid, area.name as name, area.areaDesc as desc FROM UserAreasEntity area WHERE area.userName = :userName")
})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = QueryNameConstants.USER_AREA_DETAILS_WITH_EXTENT_BY_LOCATION,
                query = "select gid, name, area_desc as desc, CAST(st_astext(st_extent(geom))AS TEXT) as extent from spatial.user_areas"
                        + " WHERE (user_name=:userName OR (scope_name=:scopeName AND is_shared='Y'))"
                        + " AND st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) group by gid"),
        @NamedNativeQuery(
                name = QueryNameConstants.USER_AREA_DETAILS_BY_LOCATION,
                query = "select * from spatial.user_areas"
                        + " WHERE (user_name=:userName OR (scope_name=:scopeName AND is_shared='Y'))"
                        + " AND st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) group by gid", resultSetMapping = "implicit.userarea"),
        @NamedNativeQuery(
                name = QueryNameConstants.SEARCH_USER_AREA,
                query = "select gid, name, area_desc as desc, CAST(st_astext(st_extent(geom))AS TEXT) as extent from spatial.user_areas"
                        + " WHERE (user_name=:userName OR (scope_name=:scopeName AND is_shared='Y'))"
                        + " AND (UPPER(name) LIKE(UPPER(:name)) OR UPPER(area_desc) LIKE(UPPER(:desc))) group by gid"),
        @NamedNativeQuery(
                name = QueryNameConstants.USERAREA_BY_COORDINATE,
                query = "select * from user_areas where st_intersects(geom, st_geomfromtext(CAST(:wktPoint as text), :crs))", resultSetMapping = "implicit.userarea")

})
@Table(name = "user_areas", schema = "spatial")
public class UserAreasEntity implements Serializable {

    private static final long serialVersionUID = 6797853213499502873L;

    @Id
    @Column(name = "gid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnAliasName(aliasName ="gid")
    private long gid;

    @Column(name = "type", length = 255)
    @ColumnAliasName(aliasName ="type")
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

    @Column(name = "scope_name", length = 255)
    private String scopeName;

    @Column(name = "name", nullable = false, length = 255)
    @ColumnAliasName(aliasName ="name")
    private String name;

    @Column(columnDefinition = "text", name = "area_desc")
    @ColumnAliasName(aliasName ="areaDesc")
    private String areaDesc;

    @Basic
    @Column(name = "geom", nullable = false)
    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName ="geometry")
    private Geometry geom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false)
    @ColumnAliasName(aliasName ="createdOn")
    private Date createdOn;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_shared", nullable = false, length = 1)
    @ColumnAliasName(aliasName ="isShared")
    private Boolean isShared;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAreas", cascade = CascadeType.ALL)
    private Set<AreaStatusEntity> areaStatuses;

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

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean isShared) {
        this.isShared = isShared;
    }
}
