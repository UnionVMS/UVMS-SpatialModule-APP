package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;

import java.util.Map;
import javax.persistence.*;

@Entity
@Table(name = "stat_rect")
@NamedQueries({
        @NamedQuery(name = StatRectEntity.DISABLE, query = "UPDATE StatRectEntity SET enabled = 'N'"),
        @NamedQuery(name = StatRectEntity.BY_INTERSECT,
                query = "FROM StatRectEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'")
})
public class StatRectEntity extends BaseSpatialEntity {

    public static final String BY_INTERSECT = "statRectEntity.byIntersect";
    public static final String DISABLE = "statRectEntity.disable";
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

    public StatRectEntity(Map<String, Object> values) throws ServiceException {
        super(values);
        setNorth((Double) values.get(NORTH));
        setSouth((Double) values.get(SOUTH));
        setEast((Double) values.get(EAST));
        setWest((Double) values.get(WEST));
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
