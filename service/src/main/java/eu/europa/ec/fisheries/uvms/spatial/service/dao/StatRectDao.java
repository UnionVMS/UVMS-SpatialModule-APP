/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.StatRectEntity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.service.entity.StatRectEntity.*;

@Slf4j
public class StatRectDao extends AbstractAreaDao<StatRectEntity> {

    private EntityManager em;

    public StatRectDao(EntityManager em) {
        this.em = em;
    }

    @Override
    protected String getIntersectNamedQuery() {
        return BY_INTERSECT;
    }

    @Override
    protected String getSearchNamedQuery() {
        return SEARCH_STATRECT;
    }

    @Override
    protected String getSearchNameByCodeQuery() {
        return SEARCH_STATRECT_NAMES_BY_CODE;
    }

    @Override
    protected Class<StatRectEntity> getClazz() {
        return StatRectEntity.class;
    }

    @Override
    protected StatRectEntity createEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {
        return new StatRectEntity(values, mapping);
    }

    @Override
    protected String getDisableAreaNamedQuery() {
        return DISABLE;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}