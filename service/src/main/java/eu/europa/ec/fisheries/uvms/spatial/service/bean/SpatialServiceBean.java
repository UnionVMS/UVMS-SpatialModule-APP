package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementListQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.dto.SpatialDto;
import eu.europa.ec.fisheries.uvms.spatial.entity.Country;
import eu.europa.ec.fisheries.uvms.spatial.entity.ExclusiveEconomicZone;
import eu.europa.ec.fisheries.uvms.spatial.message.constants.ModuleQueue;
import eu.europa.ec.fisheries.uvms.spatial.message.consumer.MessageConsumer;
import eu.europa.ec.fisheries.uvms.spatial.message.exception.MovementMessageException;
import eu.europa.ec.fisheries.uvms.spatial.message.producer.MessageProducer;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * //TODO create test
 */
@Stateless
@Local(SpatialService.class)
public class SpatialServiceBean implements SpatialService {

    @EJB
    private CrudService crudService;

    private MessageConsumer consumer;
    private MessageProducer producer;

    @Override
    public List<SpatialDto> getListAsRestDto(String spatialQuery) throws SpatialServiceException {

        MovementListQuery movementListQuery = new MovementListQuery();
        ListPagination listPagination = new ListPagination();
        listPagination.setListSize(BigInteger.valueOf(1000));
        listPagination.setPage(BigInteger.valueOf(0));
        movementListQuery.setPagination(listPagination);

        List<ListCriteria> listCriterias = new ArrayList<>();

        ListCriteria listCriteria = new ListCriteria();
        SearchKey fromDate = SearchKey.FROM_DATE;
        listCriteria.setKey(fromDate);
        listCriteria.setValue(new Date().toString());

        SearchKey toDate = SearchKey.TO_DATE;
        listCriteria.setKey(fromDate);
        listCriteria.setValue(new Date().toString());


        try {

            String query = MovementDataSourceRequestMapper.mapGetListByQuery(movementListQuery);
            String messageId = producer.sendModuleMessage(query, ModuleQueue.MOVEMENT);


        } catch (ModelMarshallException | MovementMessageException e) {
            e.printStackTrace();
        }

        return null;
    }

    // check integration test IT run locally with vagrant box and spatial data from liquibase
    // TODO gererates Caused by: java.lang.ClassNotFoundException: org.jvnet.jaxb2_commons.lang.Equals from [Module \"deployment.test.war:main\" from Service Module Loader]"}}
/*    @Override
    public GetAreaTypesSpatialRS getAreaTypes() {
        GetAreaTypesSpatialRS response = new GetAreaTypesSpatialRS();
        response.setAreaType(Arrays.asList("Portugal", "Belgium", "Poland", "Bulgaria"));
        return response;
    }*/

    public Country getCountryById(int id){ //TODO return dto instead we don't want dependency on entities in REST module
        return (Country) crudService.find(Country.class, id);
    }

    @Override
    public ExclusiveEconomicZone getExclusiveEconomicZoneById(int id) { //TODO return dto instead we don't want dependency on entities in REST module
        return (ExclusiveEconomicZone) crudService.find(ExclusiveEconomicZone.class, id);
    }
}
