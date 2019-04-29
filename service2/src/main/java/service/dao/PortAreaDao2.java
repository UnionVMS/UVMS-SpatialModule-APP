/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package service.dao;

import com.vividsolutions.jts.geom.Point;
import service.entity.BaseAreaEntity2;
import service.entity.PortAreaEntity2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


public class PortAreaDao2 {
    @PersistenceContext
    private EntityManager em;




    public PortAreaEntity2 createEntity(PortAreaEntity2 entity)  {

        em.persist(entity);
        return entity;
    }

    public List<PortAreaEntity2> getPortAreasByPoint(Point point) {

        TypedQuery<PortAreaEntity2> query = em.createNamedQuery(PortAreaEntity2.PORT_AREA_BY_POINT, PortAreaEntity2.class);
        query.setParameter("point", point);
        return query.getResultList();
    }

}