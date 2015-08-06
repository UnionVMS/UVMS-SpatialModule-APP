package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementListQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import eu.europa.ec.fisheries.uvms.spatial.dto.SpatialDto;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.message.constants.ModuleQueue;
import eu.europa.ec.fisheries.uvms.spatial.message.consumer.MessageConsumer;
import eu.europa.ec.fisheries.uvms.spatial.message.exception.MovementMessageException;
import eu.europa.ec.fisheries.uvms.spatial.message.producer.MessageProducer;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Stateless
@Local(SpatialService.class)
public class SpatialServiceBean implements SpatialService {

    @EJB
    private CrudDao crudDao;

    private MessageConsumer consumer;
    private MessageProducer producer;

    @Override
    public List<SpatialDto> getListAsRestDto(String spatialQuery) throws SpatialServiceException {

        MovementListQuery movementListQuery = new MovementListQuery();
        movementListQuery.setPagination(createPagination());

        ListCriteria listCriteria = createListCriteria();

        try {
            String query = MovementDataSourceRequestMapper.mapGetListByQuery(movementListQuery);
            String messageId = producer.sendModuleMessage(query, ModuleQueue.MOVEMENT);
        } catch (ModelMarshallException | MovementMessageException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ListCriteria createListCriteria() {
        ListCriteria listCriteria = new ListCriteria();
        listCriteria.setKey(SearchKey.FROM_DATE);
        listCriteria.setValue(new Date().toString());

        listCriteria.setKey(SearchKey.TO_DATE);
        listCriteria.setValue(new Date().toString());
        return listCriteria;
    }

    private ListPagination createPagination() {
        ListPagination listPagination = new ListPagination();
        listPagination.setListSize(BigInteger.valueOf(1000));
        listPagination.setPage(BigInteger.valueOf(0));
        return listPagination;
    }

    @Override
    public Object getAreasByLocation(double lat, double lon, int crs) {
        throw new NotImplementedException("Not implemented, yet");
    }
}
