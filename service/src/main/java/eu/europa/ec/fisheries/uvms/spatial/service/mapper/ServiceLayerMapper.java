/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.layer.ServiceLayer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceLayerMapper {

    ServiceLayerMapper INSTANCE = Mappers.getMapper(ServiceLayerMapper.class);

    ServiceLayer serviceLayerEntityToServiceLayer(ServiceLayerEntity serviceLayer);

    void merge(ServiceLayerEntity source, @MappingTarget ServiceLayerEntity target);

    void merge(ServiceLayerEntity source, @MappingTarget ServiceLayer target);

    void merge(ServiceLayer source, @MappingTarget ServiceLayerEntity target);

}