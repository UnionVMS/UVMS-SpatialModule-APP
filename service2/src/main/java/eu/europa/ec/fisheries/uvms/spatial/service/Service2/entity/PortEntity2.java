/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = PortEntity2.DISABLE, query = "UPDATE PortEntity2 SET enabled = false"),
        @NamedQuery(name = PortEntity2.LIST_ORDERED_BY_DISTANCE, query ="FROM PortEntity2 WHERE enabled = true ORDER BY distance(geom, :shape) ASC"), /// TODO create dao test
        @NamedQuery(name = PortEntity2.SEARCH_PORT, query = "FROM PortEntity2 where (upper(name) like :name OR upper(code) like :code) AND enabled=true GROUP BY gid"),
        @NamedQuery(name = PortEntity2.SEARCH_PORT_BY_AREA_CODES, query = "From PortEntity2 where code in :code AND enabled=true "),
        @NamedQuery(name = PortEntity2.CLOSEST_PORT, query = "select new eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.BaseAreaDto(type, gid, code, distance(geom, :point, true) from PortEntity2 where enabled=true AND dWithin(geom, :point, 22224, true) order by distance(geom :point, true) "),


        //(SELECT 'PORT' as type, gid, code, name, geom, _ST_DistanceUnCached(geom, ST_GeomFromText(CAST ('POINT(11.918000000000001 57.70016666666667)' AS TEXT), 4326),true) AS distance FROM spatial.port
        //WHERE enabled = 'Y' AND ST_DWithin(ST_GeomFromText(CAST ('POINT(11.918000000000001 57.70016666666667)' AS TEXT), 4326), geom, 22224) ORDER BY ST_GeomFromText(CAST ('POINT(11.918000000000001 57.70016666666667)' AS TEXT), 4326) <-> geom LIMIT 10 )

})
@Table(name = "port")
public class PortEntity2 extends BaseAreaEntity2 {

    public static final String DISABLE = "portsEntity2.disable";
    public static final String LIST_ORDERED_BY_DISTANCE = "portsEntity2.listOrderedByDistance";
    public static final String SEARCH_PORT = "portEntity2.searchPortByNameOrCode";
    public static final String SEARCH_PORT_BY_AREA_CODES = "portEntity2.searchPortsByAreaCode";
    public static final String CLOSEST_PORT = "portEntity2.closestPort";

	@Id
	@Column(name = "gid")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="port_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    @JsonProperty("gid")
    private Long id;
	
    @Column(name = "country_code", length = 3)
    private String countryCode;

    @Column(name = "fishing_port")
    private Boolean fishingPort;

    @Column(name = "landing_place")
    private Boolean landingPlace;

    @Column(name = "commercial_port")
    private Boolean commercialPort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Boolean getFishingPort() {
        return fishingPort;
    }

    public void setFishingPort(Boolean fishingPort) {
        this.fishingPort = fishingPort;
    }

    public Boolean getLandingPlace() {
        return landingPlace;
    }

    public void setLandingPlace(Boolean landingPlace) {
        this.landingPlace = landingPlace;
    }

    public Boolean getCommercialPort() {
        return commercialPort;
    }

    public void setCommercialPort(Boolean commercialPort) {
        this.commercialPort = commercialPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PortEntity2 that = (PortEntity2) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(fishingPort, that.fishingPort) &&
                Objects.equals(landingPlace, that.landingPlace) &&
                Objects.equals(commercialPort, that.commercialPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, countryCode, fishingPort, landingPlace, commercialPort);
    }
}