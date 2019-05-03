/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.rest.constants;

public final class RestConstants {

    public static final String REST_URL = "/rest";
    public static final String MODULE_REST = "/rest";

    public static final String IMAGE_URL = "/image";
    public static final String SERVICE_LAYER_PATH = "/servicelayer";

    public static final String MODULE_NAME = "spatial";

    public static final String VIEW = "view";
    public static final String SYSTEM_AREA_TYPE = "systemAreaType";
    public static final String PUBLIC = "PUBLIC";


    public static final String DELETE_FAILED = "error_delete_failed";
    public static final String ENTRY_NOT_FOUND = "error_entry_not_found";
    public static final String INPUT_NOT_SUPPORTED = "INPUT_NOT_SUPPORTED";
    public static final String UPDATE_FAILED = "UPDATE_FAILED";
    public static final String NOT_AUTHORIZED = "error_user_not_authorized";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String INPUT_VALIDATION_FAILED = "INPUT_VALIDATION_FAILED";
    public static final String USER_SCOPE_MISSING = "USER_SCOPE_MISSING";
    public static final String CREATE_ENTITY_ERROR = "error_create_report";


    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS_ALL = "*";

    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOWED_METHODS = "GET, POST, DELETE, PUT, OPTIONS";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS_ALL = "Content-Type";

    private RestConstants() {
    }

}