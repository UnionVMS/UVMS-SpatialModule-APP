/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ObjectFactory;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleMethod;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.StringWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = JAXBMarshaller.class, uses = ObjectFactory.class)
public interface SpatialModuleMapper {

    SpatialModuleMapper INSTANCE = Mappers.getMapper(SpatialModuleMapper.class);

    @Mappings({
            @Mapping(target = "mapConfiguration", source = "config")
    })
    SpatialSaveOrUpdateMapConfigurationRQ buildSaveOrUpdateRequest(MapConfigurationType config, SpatialModuleMethod method);

    @Mappings({
            @Mapping(target = "value", expression = "java(JAXBMarshaller.marshall(request))")
    })
    StringWrapper marshal(SpatialModuleRequest request) throws SpatialModelMarshallException;

    @Mappings({
            @Mapping(target = "value", expression = "java(JAXBMarshaller.marshall(response))")
    })
    StringWrapper marshal(SpatialDeleteMapConfigurationRS response) throws SpatialModelMarshallException;

}