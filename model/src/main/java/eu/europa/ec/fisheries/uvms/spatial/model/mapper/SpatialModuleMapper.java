/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import javax.xml.bind.JAXBException;

import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ObjectFactory;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleMethod;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.StringWrapper;

public class SpatialModuleMapper {

    private final ObjectFactory objectFactory = new ObjectFactory();

    public SpatialSaveOrUpdateMapConfigurationRQ buildSaveOrUpdateRequest(MapConfigurationType config, SpatialModuleMethod method) {
        if ( config == null && method == null ) {
            return null;
        }

        SpatialSaveOrUpdateMapConfigurationRQ spatialSaveOrUpdateMapConfigurationRQ = objectFactory.createSpatialSaveOrUpdateMapConfigurationRQ();

        if ( config != null ) {
            spatialSaveOrUpdateMapConfigurationRQ.setMapConfiguration( config );
        }
        if ( method != null ) {
            spatialSaveOrUpdateMapConfigurationRQ.setMethod( method );
        }

        return spatialSaveOrUpdateMapConfigurationRQ;
    }

    public StringWrapper marshal(SpatialModuleRequest request) throws SpatialModelMarshallException {
        if ( request == null ) {
            return null;
        }

        StringWrapper stringWrapper = objectFactory.createStringWrapper();

        try {
            stringWrapper.setValue( JAXBUtils.marshallJaxBObjectToString(request) );
        } catch (JAXBException e) {
            throw new SpatialModelMarshallException("ERROR IN MAPPER", e);
        }

        return stringWrapper;
    }

    public StringWrapper marshal(SpatialDeleteMapConfigurationRS response) throws SpatialModelMarshallException {
        if ( response == null ) {
            return null;
        }

        StringWrapper stringWrapper = objectFactory.createStringWrapper();

        try {
            stringWrapper.setValue( JAXBUtils.marshallJaxBObjectToString(response) );
        } catch (JAXBException e) {
            throw new SpatialModelMarshallException("ERROR IN MAPPER", e);
        }

        return stringWrapper;
    }
}
