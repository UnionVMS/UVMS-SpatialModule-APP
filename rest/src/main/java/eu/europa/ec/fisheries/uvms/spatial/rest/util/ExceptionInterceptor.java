/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;


@Interceptor
public class ExceptionInterceptor {

	Logger LOG = LoggerFactory.getLogger(ExceptionInterceptor.class);


	
	@AroundInvoke
	public Object createResponse(final InvocationContext ic) {
		LOG.info("ExceptionInterceptor received");
		try {
			return ic.proceed();
		} catch (IllegalArgumentException e) {
			LOG.error(e.getMessage(), e);
    		return createErrorResponse(RestConstants.INPUT_NOT_SUPPORTED);
    	} catch (Exception e) {
			LOG.error(e.getMessage(), e);
    		/*if (e.getCause() instanceof SpatialServiceException) {
				return createErrorResponse(((SpatialServiceException)e.getCause()).getErrorMessageCode());
			}*/
			if (e.getCause() instanceof RuntimeException) {
				return createErrorResponse(((RuntimeException)e.getCause()).getMessage());
			}
			return createErrorResponse(RestConstants.INTERNAL_SERVER_ERROR);
		}
	}

	public Response createErrorResponse(String errorMsgCode) {
		Response response = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(errorMsgCode).build();
		return response;
	}

}