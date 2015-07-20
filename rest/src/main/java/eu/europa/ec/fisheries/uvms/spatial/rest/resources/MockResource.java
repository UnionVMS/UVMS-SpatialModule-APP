package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.schema.movement.mobileterminal.v1.MobileTerminalId;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementListQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.MessageType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;

import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.VmsDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.MovementMapper;
import eu.europa.ec.fisheries.uvms.spatial.rest.mapper.VesselMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.ResponseCode;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Path("/mock")
public class MockResource {

    final static Logger LOG = LoggerFactory.getLogger(MockResource.class);

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/vms")
    public ResponseDto getMockVmsData() {
        try {
            LOG.info("Getting vms data...");

            List<VmsDto> vmsDtoList = new ArrayList<>();
            List<MovementBaseType> movementBaseTypeList = MockData.getDtoList(100);
            List<Vessel> vesselDtoList = eu.europa.ec.fisheries.uvms.vessel.model.mock.MockData.getVesselDtoList(100);

            for (Vessel vessel : vesselDtoList){
                VmsDto vmsDto = new VmsDto();
                vmsDto.setVessel(VesselMapper.INSTANCE.vesselToVesselDto(vessel));
                vmsDto.setMovement(MovementMapper.INSTANCE.movementBaseTypeToMovementDto(movementBaseTypeList.get(randInt(0,99))));
                vmsDtoList.add(vmsDto);
            }

            return new ResponseDto(vmsDtoList, ResponseCode.OK);

        } catch (Exception e) {
            LOG.error("[ Error when getting vessel list. ] ", e);
            return null; //ErrorHandler.getFault(e);
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static class MockData {

        /**
         * Get mocked data sigle object
         *
         * @param id
         * @return
         */
        public static MovementBaseType getDto(Long id) {
            MovementBaseType dto = new MovementBaseType();
            dto.setId(id.toString());
            dto.setConnectId(UUID.randomUUID().toString());
            dto.setCalculatedSpeed(BigDecimal.valueOf(3.2 + id));
            dto.setCourse(12);
            dto.setMeasuredSpeed(BigDecimal.valueOf(0.2 + id));
            dto.setMessageType(MessageType.ENT);
            dto.setMobileTerminal(getMobTermId());
            dto.setPosition(getMovementPoint());
            dto.setSource(MovementSourceType.INMARSAT_C);
            dto.setStatus("God like");
            return dto;
        }

        /**
         *
         * @return
         */
        public static MovementPoint getMovementPoint() {
            MovementPoint point = new MovementPoint();
            point.setLatitude(12);
            point.setLongitude(69);
            return point;
        }

        /**
         *
         * @return
         */
        public static MobileTerminalId getMobTermId() {
            MobileTerminalId id = new MobileTerminalId();
            id.setId("ABC-80+");
            return id;
        }

        /**
         *
         * @return
         */
        public static MovementListQuery getQuery() {
            MovementListQuery query = new MovementListQuery();
            query.getMovementSearchCriteria().add(getListCtieria());
            query.setPagination(getListPagination());
            return query;
        }

        /**
         *
         * @return
         */
        public static ListPagination getListPagination() {
            ListPagination criteria = new ListPagination();
            criteria.setListSize(BigInteger.valueOf(10L));
            criteria.setPage(BigInteger.valueOf(1L));
            return criteria;
        }

        /**
         *
         * @return
         */
        public static ListCriteria getListCtieria() {
            ListCriteria criteria = new ListCriteria();
            criteria.setKey(SearchKey.CONNECT_ID);
            criteria.setValue("value");
            return criteria;
        }

        /**
         * Get mocked data as a list
         *
         * @param amount
         * @return
         */
        public static List<MovementBaseType> getDtoList(Integer amount) {
            List<MovementBaseType> dtoList = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                dtoList.add(getDto(Long.valueOf(i)));
            }
            return dtoList;
        }

    }

}
