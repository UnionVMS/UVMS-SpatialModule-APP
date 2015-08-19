package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAOBean;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SuccessType;
import eu.europa.ec.fisheries.uvms.util.exception.ExceptionMapper;

import javax.ejb.EJB;

/**
 * Created by kopyczmi on 14-Aug-15.
 */
public abstract class AbstractServiceBean {

    @EJB
    protected CommonGenericDAOBean commonDao;

    @EJB
    ExceptionMapper exceptionMapper;

    protected ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }

}
