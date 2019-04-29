/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package service.bean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.service.interceptor.TracingInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialect;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DatabaseDialectFactory;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * This class groups all the spatial operations on the spatial database.
 */
@Stateless
@Transactional
@Slf4j
@Interceptors(TracingInterceptor.class)
public class SpatialServiceBean implements SpatialService {

    private EntityManager em;
	
    @PersistenceContext(unitName = "spatialPUpostgres")
    private EntityManager postgres;

    @PersistenceContext(unitName = "spatialPUoracle")
    private EntityManager oracle;	
	
    private @EJB SpatialRepository repository;
    private @EJB PropertiesBean properties;
    private DatabaseDialect dialect;

    public void initEntityManager() {
        String dbDialect = System.getProperty("db.dialect");
        if ("oracle".equalsIgnoreCase(dbDialect)) {
            em = oracle;
        } else {
            em = postgres;
        }
    }
	
    @PostConstruct
    public void init(){
		initEntityManager();
        dialect = new DatabaseDialectFactory(properties).getInstance();
    }

    @Override
    public String calculateBuffer(final Double latitude, final Double longitude, final Double buffer) {

        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new com.vividsolutions.jts.geom.Coordinate(longitude, latitude));
        Geometry geometry = point.buffer(buffer);
        return GeometryMapper.INSTANCE.geometryToWkt(geometry).getValue();

    }

}