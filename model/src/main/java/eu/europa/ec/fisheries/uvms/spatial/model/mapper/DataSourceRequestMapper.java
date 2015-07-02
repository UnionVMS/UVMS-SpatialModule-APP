/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.spatial.search.v1.MovementListQuery;
import eu.europa.ec.fisheries.schema.spatial.search.v1.MovementSearchGroup;
import eu.europa.ec.fisheries.schema.spatial.source.v1.CreateMovementRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.CreateMovementSearchGroupRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.CreateTempMovementRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.DeleteMovementSearchGroupRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.GetMovementListByQueryRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.GetMovementSearchGroupRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.GetMovementSearchGroupsByUserRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.GetTempMovementListRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.MovementDataSourceMethod;
import eu.europa.ec.fisheries.schema.spatial.source.v1.SendTempMovementRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.SetStatusMovementRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.UpdateMovementSearchGroupRequest;
import eu.europa.ec.fisheries.schema.spatial.source.v1.UpdateTempMovementRequest;
import eu.europa.ec.fisheries.schema.spatial.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.spatial.v1.TempMovementType;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.ModelMarshallException;

/**
 *
 * @author jojoha
 */
public class DataSourceRequestMapper {

    final static Logger LOG = LoggerFactory.getLogger(DataSourceRequestMapper.class);

    public static String mapGetListByQuery(MovementListQuery query) throws ModelMarshallException {
        GetMovementListByQueryRequest request = new GetMovementListByQueryRequest();
        request.setMethod(MovementDataSourceMethod.MOVEMENT_LIST);
        request.setQuery(query);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapCreateMovement(MovementBaseType spatial) throws ModelMarshallException {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setMovement(spatial);
        request.setMethod(MovementDataSourceMethod.CREATE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToCreateMovementSearchGroupRequest(MovementSearchGroup searchGroup) throws ModelMarshallException {
        CreateMovementSearchGroupRequest request = new CreateMovementSearchGroupRequest();
        request.setSearchGroup(searchGroup);
        request.setMethod(MovementDataSourceMethod.GROUP_CREATE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToGetMovementSearchGroupRequest(Long id) throws ModelMarshallException {
        GetMovementSearchGroupRequest request = new GetMovementSearchGroupRequest();
        request.setId(BigInteger.valueOf(id));
        request.setMethod(MovementDataSourceMethod.GROUP_GET);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToGetMovementSearchGroupsByUserRequest(String user) throws ModelMarshallException {
        GetMovementSearchGroupsByUserRequest request = new GetMovementSearchGroupsByUserRequest();
        request.setUser(user);
        request.setMethod(MovementDataSourceMethod.GROUP_LIST);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToUpdateMovementSearchGroup(MovementSearchGroup searchGroup) throws ModelMarshallException {
        UpdateMovementSearchGroupRequest request = new UpdateMovementSearchGroupRequest();
        request.setSearchGroup(searchGroup);
        request.setMethod(MovementDataSourceMethod.GROUP_UPDATE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToDeleteMovementSearchGroupRequest(Long id) throws ModelMarshallException {
        DeleteMovementSearchGroupRequest request = new DeleteMovementSearchGroupRequest();
        request.setId(BigInteger.valueOf(id));
        request.setMethod(MovementDataSourceMethod.GROUP_DELETE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToCreateTempMovementRequest(TempMovementType tempMovementType) throws ModelMarshallException {
        CreateTempMovementRequest request = new CreateTempMovementRequest();
        request.setMovement(tempMovementType);
        request.setMethod(MovementDataSourceMethod.CREATE_TEMP);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToSetStatusMovementRequest(String guid) throws ModelMarshallException {
        SetStatusMovementRequest request = new SetStatusMovementRequest();
        request.setGuid(guid);
        request.setMethod(MovementDataSourceMethod.TEMP_DELETE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToUpdateTempMovementRequest(TempMovementType tempMovementType) throws ModelMarshallException {
        UpdateTempMovementRequest request = new UpdateTempMovementRequest();
        request.setMovement(tempMovementType);
        request.setMethod(MovementDataSourceMethod.TEMP_UPDATE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToGetTempMovementListRequest(MovementListQuery query) throws ModelMarshallException {
        GetTempMovementListRequest request = new GetTempMovementListRequest();
        request.setQuery(query);
        request.setMethod(MovementDataSourceMethod.TEMP_LIST_BY_QUERY);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapToSendTempMovementRequest(String guid) throws ModelMarshallException {
        SendTempMovementRequest request = new SendTempMovementRequest();
        request.setGuid(guid);
        request.setMethod(MovementDataSourceMethod.TEMP_SEND);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

}
