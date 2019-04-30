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
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.GeometryMapper;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
@AttributeOverride(name = "id", column = @Column(name = "GID"))
public class BaseAreaEntity2 {

    private static final String ISO_8859_1 = "ISO-8859-1";
    private static final String UTF_8 = "UTF-8";

    @JsonIgnore
    protected Geometry geom;

    protected String name;

    @Column(length = 20)
    protected String code;

    @Column(nullable = false)
    protected Boolean enabled = true;

    @Column(name = "enabled_on")
    protected Instant enabledOn;

    public String getGeometry(){
        return GeometryMapper.geometryToWkt(geom);
    }

    @JsonIgnore
    public String getGeometryType(){
        String geometryType = null;
        if (geom !=null){
            geometryType = geom.getGeometryType();
        }
        return geometryType;
    }

    public String getExtent() {
        String extent = null;
        if (geom != null) {
            extent = new WKTWriter().write(geom.getEnvelope());
        }
        return extent;
    }

    public String getCentroid(){
        String centroid = null;
        if (geom != null) {
            centroid = new WKTWriter().write(geom.getCentroid());
        }
        return centroid;
    }

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getEnabledOn() {
        return enabledOn;
    }

    public void setEnabledOn(Instant enabledOn) {
        this.enabledOn = enabledOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseAreaEntity2 that = (BaseAreaEntity2) o;
        return Objects.equals(geom, that.geom) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(enabledOn, that.enabledOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geom, name, code, enabled, enabledOn);
    }
}