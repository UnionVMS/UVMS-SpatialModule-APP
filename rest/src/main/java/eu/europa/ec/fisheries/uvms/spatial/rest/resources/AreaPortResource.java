package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.PortAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/ports")
@Slf4j
@Stateless
public class AreaPortResource {

    @EJB
    private PortAreaService portAreaService;


}
