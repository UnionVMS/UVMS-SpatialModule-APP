package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialException;

/**
 * Created by padhyad on 3/21/2016.
 */
public interface ReportingService {

    ReportGetStartAndEndDateRS getReportDates(Integer reportId, String userName, String scopeName, String timeStamp) throws ServiceException;
}
