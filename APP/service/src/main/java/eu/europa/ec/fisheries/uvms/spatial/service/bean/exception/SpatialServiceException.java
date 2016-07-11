/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean.exception;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SpatialServiceException extends RuntimeException {

    private final SpatialServiceErrors error;
    private Object[] params;

    public SpatialServiceException(SpatialServiceErrors error, Object... params) {
        super(error.formatMessage(params));
        this.error = error;
        this.params = params;
    }

    public SpatialServiceException(SpatialServiceErrors error, Throwable cause, Object... params) {
        super(error.formatMessage(params), cause);
        this.params = params;
        this.error = error;
    }

    public SpatialServiceException(SpatialServiceErrors error, SpatialServiceException cause) {
        super(join(getMessage(error), cause.getMessage()), cause);
        this.error = cause.getError();
    }

    public SpatialServiceException(SpatialServiceErrors error, SpatialServiceException cause, Object... arguments) {
        super(join(error.formatMessage(arguments), cause.getMessage()), cause);
        this.error = cause.getError();
    }

    private static String join(String... params) {
        return StringUtils.join(params, ": ");
    }

    private static String getMessage(SpatialServiceErrors error) {
        return error.getMessagePattern();
    }

    public Integer getErrorCode() {
        return this.error.getErrorCode();
    }
    
    public String getErrorMessageCode() {
        return this.error.getErrorMessageCode();
    }

    public SpatialServiceErrors getError() {
        return this.error;
    }

    public Object[] getParameters() {
        return this.params;
    }

    public int hashCode() {
        return (new HashCodeBuilder()).append(this.error).toHashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SpatialServiceException that = (SpatialServiceException) o;
            return (new EqualsBuilder()).append(this.error, that.error).isEquals();
        } else {
            return false;
        }
    }
}