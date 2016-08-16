/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.BaseDAOTest;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

public abstract class BaseSpatialDaoTest extends BaseDAOTest {

    protected static final Operation DELETE_ALL = sequenceOf(
            deleteAllFrom("spatial.countries"),
            deleteAllFrom("spatial.eez"),
            deleteAllFrom("spatial.port_area"),
            deleteAllFrom("spatial.port"),
            deleteAllFrom("spatial.projection"),
            deleteAllFrom("spatial.report_connect_service_area"),
            deleteAllFrom("spatial.report_connect_spatial"),
            deleteAllFrom("spatial.system_configurations"),
            deleteAllFrom("spatial.service_layer"),
            deleteAllFrom("spatial.user_areas"),
            deleteAllFrom("spatial.user_scope"),
            deleteAllFrom("spatial.area_location_types")
    );

    protected static final Operation INSERT_COUNTRY_REFERENCE_DATA = sequenceOf(
            insertInto("spatial.countries")
                    .columns("GID", "NAME", "CODE", "GEOM", "ENABLED")
                    .values(1L, "Portugal", "PRT",
                            "MULTIPOLYGON(((-8.20401647999992 42.069552104,-6.20594722499993 41.5702802530002," +
                                    "-8.94603430899986 37.00787995,-8.20401647999992 42.069552104)))", "Y")
                    .values(2L, "Spain", "ESP",
                            "MULTIPOLYGON(((-7.69615637899992 43.731512762,3.3177189460001 42.322984117," +
                                    "-5.6117651029999 36.0064557960001,-7.69615637899992 43.731512762)))", "Y")
                    .build()
    );

    protected static final Operation INSERT_PORT_REFERENCE_DATA = sequenceOf(
            insertInto("spatial.port")
                    .columns("GID", "NAME", "CODE", "GEOM", "ENABLED")
                    .values(1L, "Luanda", "AGO", "MULTIPOINT(13.234 -8.838)", "Y")
                    .values(2L, "Lobito", "AOLOB", "MULTIPOINT(13.533 -12.367)", "Y")
                    .values(3L, "Namibe", "AOMSZ", "MULTIPOINT(12.158 -15.195)", "Y")
                    .values(4L, "San Pedro", "ARSPD", "MULTIPOINT(-59.167 -33.683)", "N")
                    .build()
    );


    protected static final Operation INSERT_PORT_AREA_REFERENCE_DATA = sequenceOf(
            insertInto("spatial.port_area")
                    .columns("GID", "NAME", "CODE", "GEOM", "ENABLED")
                    .values(1L, "Arrifana", "PTARF",
                            "MULTIPOLYGON(((-8.23435463579409 40.2167886586631,-8.267269511506 40.1919737058521," +
                                    "-8.29964570193888 40.2172021352306,-8.26673015076074 40.2420261841011," +
                                    "-8.23435463579409 40.2167886586631)))", "Y")
                    .values(2L, "Mira", "PTMIR",
                            "MULTIPOLYGON(((-8.70024797652114 40.4329197209197,-8.73309892535009 40.4079728634524," +
                                    "-8.76575214762078 40.4330710007415,-8.73290095050793 40.4580270276585," +
                                    "-8.70024797652114 40.4329197209197)))", "Y")
                    .values(3L, "Figueira da Foz", "PTFDF",
                            "MULTIPOLYGON(((-8.82538784978001 40.1419554181509,-8.85805207409343 40.116971405565," +
                                    "-8.89061221545117 40.1420353977151,-8.85794786067537 40.1670284858648," +
                                    "-8.82538784978001 40.1419554181509)))", "Y")
                    .build()
    );

    protected static final Operation INSERT_RFMO_REFERENCE_DATA = sequenceOf(
            insertInto("spatial.rfmo")
                    .columns("GID", "NAME", "CODE", "GEOM", "ENABLED")
                    .values(1L, "International Commission for the Conservation of Atlantic Tuna", "ICCAT",
                            "MULTIPOLYGON(((29.9996063400001 83.9331028899001,-70.00000002 -81.1501483101," +
                                    "-79.9999999199999 77.3216511699,29.9996063400001 83.9331028899001))))", "Y")
                    .values(2L, "North Atlantic Salmon Conservation Organization", "NASCO",
                            "MULTIPOLYGON(((29.9996063400001 83.9331028899001,-77.23222344 37.2963867699," +
                                    "-79.9999999199999 77.3216511699,29.9996063400001 83.9331028899001)))", "Y")
                    .values(3L, "North-East Atlantic Fisheries Commission", "NEAFC",
                            "MULTIPOLYGON(((50.9999990373697 84.466325737639,-42.0000008426303 35.9979663616391," +
                                    "-44.0000008226303 89.9000042416391,50.9999990373697 84.466325737639)))", "Y")
                    .build()
    );

    protected static final Operation INSERT_EEZ_REFERENCE_DATA = sequenceOf(
            insertInto("spatial.eez")
                    .columns("GID", "NAME", "COUNTRY", "SOVEREIGN", "GEOM", "ENABLED")
                    .values(1L, "Eez with empty geometry", "Belgium", "Belgium", "MULTIPOLYGON EMPTY", "Y")
                    .values(2L, "Portuguese Exclusive Economic Zone", "Portugal", "Portugal",
                            "MULTIPOLYGON(((-8.72722170899993 40.722410075, -12.211019511 34.9460901480001," +
                                    "-13.3017399999999 41.46626, -8.72722170899993 40.722410075)))", "Y")
                    .values(3L, "Christmas Island", "Australia", "Australia",
                            "MULTIPOLYGON(((106.867924148 -9.16467987999994,108.036593601 -12.9679006599999," +
                                    "103.079231596 -12.82837266, 102.56917584 -8.87249927999994," +
                                    "106.867924148 -9.16467987999994)))", "Y")
                    .build()
    );

    protected static final Operation INSERT_USER_AREA_REFERENCE_DATA = sequenceOf(
            insertInto("spatial.user_areas")
                    .columns("GID", "USER_NAME", "NAME", "TYPE", "AREA_DESC", "GEOM", "ENABLED", "CREATED_ON")
                    .values(1L, "userDaoTest", "MyArea", "EEZ", "a simple description", "MULTIPOLYGON(((-109.028 36.985,-109.028 40.979,-102.062 40.979,-102.062 37.002,-109.028 36.985)))" +
                            "106.867924148 -9.16467987999994)))", "Y", "2015-10-11 13:02:23.0")
                    .build(),
            insertInto("spatial.user_scope")
                    .columns("ID", "SCOPE_NAME", "USER_AREA_ID")
                    .values(1L, "EC", 1L)
                    .build()
    );

    protected static final Operation INSERT_REFERENCE_DATA = sequenceOf(
            insertInto("spatial.projection")
                    .columns("ID", "NAME", "SRS_CODE", "PROJ_DEF", "FORMATS", "UNITS", "WORLD", "EXTENT", "AXIS", "WORLD_EXTENT")
                    .values(1L, "Spherical Mercator", 3857, "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs", "m", "m", 'Y', "20026376.39;-20048966.10;20026376.39;20048966.10", "enu", "20026376.39;-20048966.10;20026376.39;20048966.10")
                    .values(2L, "WGS 84", 4326, "+proj=longlat +datum=WGS84 +no_defs", "dd;dms;ddm;m", "degrees", 'Y', "-180;-90;180;90", "neu", "-180;-90;180;90")
                    .build(),
            insertInto("spatial.report_connect_spatial")
                    .columns("ID", "APP_VERSION", "REPORT_ID")
                    .values(1L, "1", 123)
                    .values(2L, "2", 1234)
                    .build(),
            insertInto("spatial.provider_format")
                    .columns("ID", "SERVICE_TYPE")
                    .values(1L, "WMS")
                    .build(),
            insertInto("spatial.service_layer")
                    .columns("ID", "NAME", "IS_INTERNAL", "PROVIDER_FORMAT_ID")
                    .values(1L, "EEZ", 'Y', 1)
                    .values(2L, "RFMO", 'Y', 1)
                    .values(3L, "Countries", 'Y', 1)
                    .values(4L, "Ports", 'Y', 1)
                    .values(5L, "UserAreas", 'Y', 1)
                    .values(6L, "PortAreas", 'Y', 1)
                    .build(),
            insertInto("spatial.area_location_types")
                    .columns("ID", "TYPE_NAME", "AREA_DB_TABLE", "IS_LOCATION", "IS_SYSTEM_WIDE", "SERVICE_LAYER_ID")
                    .values(1L, "EEZ", "eez", 'N', 'Y', 1)
                    .values(2L, "RFMO", "rfmo", 'N', 'Y', 2)
                    .values(3L, "COUNTRY", "countries", 'N', 'N', 3)
                    .values(4L, "PORT", "port", 'Y', 'Y', 4)
                    .values(5L, "USERAREA", "user_areas", 'N', 'Y', 5)
                    .values(6L, "PORTAREA", "port_area", 'N', 'Y', 6)
                    .build()
    );

    @Override
    protected String getSchema() {
        return "spatial";
    }

    @Override
    protected String getPersistenceUnitName() {
        return "testPU";
    }
}