package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;

import javax.persistence.*;

/**
 * Created by georgige on 2/9/2016.
 */
@Entity
@Table(name = "user_scope", schema = "spatial")
public class UserScopeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_area_id", nullable = false)
    private UserAreasEntity userAreas;

    @Column(name = "scope_name", nullable = false, length = 255)
    @ColumnAliasName(aliasName ="scope_name")
    private String name;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserAreasEntity getUserAreas() {
        return userAreas;
    }

    public void setUserAreas(UserAreasEntity userAreas) {
        this.userAreas = userAreas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
