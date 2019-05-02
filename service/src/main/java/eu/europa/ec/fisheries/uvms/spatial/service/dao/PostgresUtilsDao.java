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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPoint;
import org.hibernate.SQLQuery;
import org.hibernate.spatial.JTSGeometryType;
import org.hibernate.spatial.dialect.postgis.PGGeometryTypeDescriptor;

public class PostgresUtilsDao extends UtilsDao {

    private String GENERATE_POINTS =  "";

    private EntityManager em;

    public PostgresUtilsDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public Integer mapDefaultSRIDToEPSG(Integer srid) {
        return srid;
    }

    @Override
    public Integer mapEPSGtoDefaultSRID(Integer epsg) {
        return epsg;
    }

    @Override public MultiPoint generatePoints(String wkt, Integer numberOfPoints) {

        Query nativeQuery = em.createNativeQuery("SELECT ST_GeneratePoints(ST_GeomFromText(:wkt), :nbrPoints);");

        nativeQuery.setParameter("wkt", wkt);
        nativeQuery.setParameter("nbrPoints", numberOfPoints);

        SQLQuery unwrap = nativeQuery.unwrap(SQLQuery.class);

        unwrap.addScalar("st_generatepoints", new JTSGeometryType(PGGeometryTypeDescriptor.INSTANCE));

        Object singleResult = nativeQuery.getSingleResult();

        Geometry geometry = (Geometry) singleResult;
        return (MultiPoint) geometry;
    }
}
