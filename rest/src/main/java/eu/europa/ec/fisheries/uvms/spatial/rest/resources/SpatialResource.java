package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.movement.model.mock.MockData;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.ResponseCode;
import eu.europa.ec.fisheries.wsdl.vessel.types.CarrierSource;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Path("/spatial")
public class SpatialResource {

    final static Logger LOG = LoggerFactory.getLogger(SpatialResource.class);

    @GET
    //@Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/list")
    public ResponseDto getVesselList() {
        try {
            LOG.info("Getting vessel list.");

            return new ResponseDto(MockData.getDtoList(100), ResponseCode.OK);
        } catch (Exception e) {
            LOG.error("[ Error when getting vessel list. ] ", e);
            return null; //ErrorHandler.getFault(e);
        }
    }

    public static Vessel getVesselDto(Integer id) {
        Vessel dto = new Vessel();

        dto.setCfr("CFR" + id);
        dto.setCountryCode("SWE" + id);
        dto.setExternalMarking("MARKING" + 1);
        dto.setGrossTonnage(BigDecimal.valueOf(1.2));
        dto.setHasIrcs(true);
        dto.setHasLicense(true);
        dto.setHomePort("PORT" + id);

        VesselId vesselId = new VesselId();
        vesselId.setValue(id.toString());
        dto.setVesselId(vesselId);
        dto.setIrcs("IRCS-" + id);
        dto.setLengthBetweenPerpendiculars(BigDecimal.valueOf(0.5 + id));
        dto.setLengthOverAll(BigDecimal.valueOf(2.5 + id));
        dto.setName("VESSEL-" + id);
        dto.setOtherGrossTonnage(BigDecimal.valueOf(11.5 + id));
        dto.setPowerAux(BigDecimal.valueOf(123.4 + id));
        dto.setPowerMain(BigDecimal.valueOf(586.2 + id));
        dto.setSafetyGrossTonnage(BigDecimal.valueOf(54.3 + id));
        dto.setSource(CarrierSource.LOCAL);
        dto.setActive(true);

        /*if (id % 3 == 0) {
         dto.setSource(CarrierSource.EU);
         dto.setActive(true);
         }
         if (id % 2 == 0) {
         dto.setSource(CarrierSource.NATIONAL);
         dto.setActive(false);
         }
         if (id % 5 == 0) {
         dto.setSource(CarrierSource.THIRD_COUNTRY);
         dto.setActive(true);

         dto.setVesselType("VESSEL-TYPE: " + id);
         }*/
        dto.setVesselType("VESSEL-TYPE: " + id);
        return dto;
    }

    public static List<Vessel> getVesselDtoList(Integer amount) {
        List<Vessel> dtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtoList.add(getVesselDto(i));
        }
        return dtoList;
    }
}
