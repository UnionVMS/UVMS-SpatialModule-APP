package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "stat_rect")
@NamedQueries({
        @NamedQuery(name = StatRectEntity.DISABLE, query = "UPDATE StatRectEntity SET enabled = 'N'"),
        @NamedQuery(name = StatRectEntity.BY_INTERSECT,
                query = "FROM StatRectEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = StatRectEntity.SEARCH_STATRECT, query = "FROM StatRectEntity where upper(name) like :name OR upper(code) like :code AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = StatRectEntity.SEARCH_STATRECT_NAMES_BY_CODE, query = "From StatRectEntity where code in (SELECT distinct(code) from StatRectEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)")
})
public class StatRectEntity extends BaseSpatialEntity {

    public static final String BY_INTERSECT = "statRectEntity.byIntersect";
    public static final String DISABLE = "statRectEntity.disable";
    public static final String SEARCH_STATRECT = "statrectEntity.searchStatrectByNameOrCode";
    public static final String SEARCH_STATRECT_NAMES_BY_CODE = "statrectEntity.searchNamesByCode";

    public static final String NORTH = "north";
    public static final String SOUTH = "south";
    public static final String EAST = "east";
    public static final String WEST = "west";

    @Column(name = "north")
    @ColumnAliasName(aliasName = NORTH)
    private Double north;

    @Column(name = "south")
    @ColumnAliasName(aliasName = SOUTH)
    private Double south;

    @Column(name = "east")
    @ColumnAliasName(aliasName = EAST)
    private Double east;

    @Column(name = "west")
    @ColumnAliasName(aliasName = WEST)
    private Double west;

    public StatRectEntity() {
        // why JPA why
    }

    public StatRectEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
       super(values, mapping);
       // for (UploadMappingProperty property : mapping){
       //     String source = property.getSource();
       //     String target = property.getTarget();
       //     try {
       //         BeanUtils.setProperty(this, target, values.get(source));
       //     } catch (IllegalAccessException | InvocationTargetException e) {
       //         throw new ServiceException("ERROR WHILE MAPPING ENTITY", e);
       //     }
       // }
    }

    public Double getNorth() {
        return north;
    }

    public void setNorth(Double north) {
        this.north = north;
    }

    public Double getSouth() {
        return south;
    }

    public void setSouth(Double south) {
        this.south = south;
    }

    public Double getEast() {
        return east;
    }

    public void setEast(Double east) {
        this.east = east;
    }

    public Double getWest() {
        return west;
    }

    public void setWest(Double west) {
        this.west = west;
    }
}
