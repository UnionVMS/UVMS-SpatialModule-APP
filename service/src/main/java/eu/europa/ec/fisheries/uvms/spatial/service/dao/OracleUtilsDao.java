/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.locationtech.jts.geom.MultiPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;

@Slf4j
public class OracleUtilsDao extends UtilsDao {

    private EntityManager em;

    private static final String MAP_DEFAULT_SRID_TO_EPSG =  "SELECT SDO_CS.MAP_ORACLE_SRID_TO_EPSG(:srid) FROM DUAL";
    private static final String MAP_EPSG_TO_DEFAULT_SRID =  "SELECT SDO_CS.MAP_EPSG_SRID_TO_ORACLE(:epsg) FROM DUAL";

    private static Query mapDefaultSridToEpsg;
    private static Query mapEpsgToDefaultSrid;

    public OracleUtilsDao(EntityManager em) {
        this.em = em;
        mapDefaultSridToEpsg = em.createNativeQuery(MAP_DEFAULT_SRID_TO_EPSG);
        mapDefaultSridToEpsg.setMaxResults(1);
        mapEpsgToDefaultSrid = em.createNamedQuery(MAP_EPSG_TO_DEFAULT_SRID);
        mapEpsgToDefaultSrid.setMaxResults(1);
    }

    @Override
    public Integer mapDefaultSRIDToEPSG(Integer srid){

        Integer epsg = SRID_EPSG_MAP.get(srid);
        if(epsg == null){
            mapDefaultSridToEpsg.setParameter("srid", srid);
            epsg = (Integer) mapDefaultSridToEpsg.getSingleResult();
            SRID_EPSG_MAP.put(srid, epsg);
        }
        return epsg;
    }

    @Override
    public Integer mapEPSGtoDefaultSRID(Integer epsg) {
        mapEpsgToDefaultSrid.setParameter("epsg", epsg);
        return (Integer) mapEpsgToDefaultSrid.getSingleResult();
    }

    @Override public MultiPoint generatePoints(String wkt, Integer numberOfPoints) {
        throw new NotImplementedException();
    }

}
