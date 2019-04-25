/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.CountryEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CountryDao extends AbstractDAO<CountryEntity> {

    @PersistenceContext
    private EntityManager em;


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CountryEntity> findEntitiesByNamedQuery(final String queryName) {
        Query query = getEntityManager().createNamedQuery(queryName);
        List<Object[]> resultList = query.getResultList();
        List<CountryEntity> countryEntities = new ArrayList<>();
        for (Object[] obj : resultList) {
            CountryEntity country = new CountryEntity();
            country.setName((String)obj[0]);
            country.setCode((String)obj[1]);
            countryEntities.add(country);
        }
        return countryEntities;
    }
}