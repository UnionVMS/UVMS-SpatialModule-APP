/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = PortEntity.DISABLE, query = "UPDATE PortEntity SET enabled = 'N'"),
        @NamedQuery(name = PortEntity.LIST_ORDERED_BY_DISTANCE, query ="FROM PortEntity WHERE enabled = 'Y' ORDER BY distance(geom, :shape) ASC"), /// TODO create dao test
        @NamedQuery(name = PortEntity.SEARCH_PORT, query = "FROM PortEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid"),
        @NamedQuery(name = PortEntity.SEARCH_PORT_NAMES_BY_CODE, query = "From PortEntity where code in (SELECT distinct(code) from PortEntity where (upper(name) like :name OR upper(code) like :code) AND enabled='Y' GROUP BY gid)")
})
@Table(name = "port")
public class PortEntity extends BaseAreaEntity {

    public static final String PORT_BY_COORDINATE = "portEntity.ByCoordinate";
    public static final String DISABLE = "portsEntity.disable";
    public static final String LIST_ORDERED_BY_DISTANCE = "portsEntity.listOrderedByDistance";
    public static final String SEARCH_PORT = "portEntity.searchPortByNameOrCode";
    public static final String SEARCH_PORT_NAMES_BY_CODE = "portEntity.searchNamesByCode";

    private static final String COUNTRY_CO = "country_co";
    private static final String FISHING_PO = "fishing_po";
    private static final String LANDING_PL = "landing_pl";
    private static final String COMMERCIAL = "commercial";

    @Column(name = "country_code", length = 3)
    @ColumnAliasName(aliasName = "countrycode")
    private String countryCode;

    @Column(name = "fishing_port", length = 1)
    @ColumnAliasName(aliasName = "fishingport")
    private String fishingPort;

    @Column(name = "landing_place")
    @ColumnAliasName(aliasName = "landingplace")
    private String landingPlace;

    @Column(name = "commercial_port")
    @ColumnAliasName(aliasName = "commercialport")
    private String commercialPort;

    public PortEntity() {
        // why JPA why
    }

    public PortEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        super(values, mapping);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFishingPort() {
        return fishingPort;
    }

    public void setFishingPort(String fishingPort) {
        this.fishingPort = fishingPort;
    }

    public String getLandingPlace() {
        return landingPlace;
    }

    public void setLandingPlace(String landingPlace) {
        this.landingPlace = landingPlace;
    }

    public String getCommercialPort() {
        return commercialPort;
    }

    public void setCommercialPort(String commercialPort) {
        this.commercialPort = commercialPort;
    }

}