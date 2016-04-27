package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_scope")
@EqualsAndHashCode(of = {"name"}, callSuper = true)
public class UserScopeEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_area_id", nullable = false)
    private UserAreasEntity userAreas;

    @Column(name = "scope_name", nullable = false, length = 255)
    @ColumnAliasName(aliasName ="scope_name")
    private String name;

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
