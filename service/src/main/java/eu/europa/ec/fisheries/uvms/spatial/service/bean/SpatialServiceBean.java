package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementListQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.dto.SpatialDto;
import eu.europa.ec.fisheries.uvms.spatial.message.constants.ModuleQueue;
import eu.europa.ec.fisheries.uvms.spatial.message.consumer.MessageConsumer;
import eu.europa.ec.fisheries.uvms.spatial.message.exception.MovementMessageException;
import eu.europa.ec.fisheries.uvms.spatial.message.producer.MessageProducer;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.entity.Eez;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * //TODO create test
 */
@Stateless
public class SpatialServiceBean implements SpatialService {

    //private static EntityManagerFactory factory;

    @PersistenceContext(unitName = "entityManager")
    private EntityManager em;

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


        } catch (ModelMarshallException e) {
            e.printStackTrace();
        } catch (MovementMessageException e) {
            e.printStackTrace();
        }


        return null;
    }

    public Eez getEezById(int eezId) {

        // factory = Persistence.createEntityManagerFactory("entityManager");
        // EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();
        Eez eez = (Eez) em.find(Eez.class, eezId);

        em.getTransaction().commit();
        em.close();

        return eez;
    }

}
