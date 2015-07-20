package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.movement.model.mock.MockData;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
}
