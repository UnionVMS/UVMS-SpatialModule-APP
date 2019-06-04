DROP SEQUENCE "area_location_types_id_seq";
DROP SEQUENCE "area_status_id_seq";
DROP SEQUENCE "bookmark_id_seq";
DROP SEQUENCE "countries_gid_seq";
DROP SEQUENCE "custom_area_properties_id_seq";
DROP SEQUENCE "custom_areas_gid_seq";
DROP SEQUENCE "eez_gid_seq";
DROP SEQUENCE "fao_id_seq";
DROP SEQUENCE "gfcm_gid_seq";
DROP SEQUENCE "port_area_gid_seq";
DROP SEQUENCE "projection_id_seq";
DROP SEQUENCE "port_gid_seq";
DROP SEQUENCE "provider_format_id_seq";
DROP SEQUENCE "rac_gid_seq";
DROP SEQUENCE "report_connect_service_id_seq";
DROP SEQUENCE "report_connect_spatial_id_seq";
DROP SEQUENCE "report_layer_config_id_seq";
DROP SEQUENCE "rfmo_gid_seq";
DROP SEQUENCE "service_layer_id_seq";
DROP SEQUENCE "stat_rect_gid_seq";
DROP SEQUENCE "system_configurations_id_seq";
DROP SEQUENCE "user_areas_gid_seq";
DROP SEQUENCE "user_scope_id_seq";


CREATE SEQUENCE "area_location_types_id_seq";
CREATE SEQUENCE "area_status_id_seq";
CREATE SEQUENCE "bookmark_id_seq";
CREATE SEQUENCE "countries_gid_seq";
CREATE SEQUENCE "custom_area_properties_id_seq";
CREATE SEQUENCE "custom_areas_gid_seq";
CREATE SEQUENCE "eez_gid_seq";
CREATE SEQUENCE "fao_id_seq";
CREATE SEQUENCE "gfcm_gid_seq";
CREATE SEQUENCE "port_area_gid_seq";
CREATE SEQUENCE "projection_id_seq";
CREATE SEQUENCE "port_gid_seq";
CREATE SEQUENCE "provider_format_id_seq";
CREATE SEQUENCE "rac_gid_seq";
CREATE SEQUENCE "report_connect_service_id_seq";
CREATE SEQUENCE "report_connect_spatial_id_seq";
CREATE SEQUENCE "report_layer_config_id_seq";
CREATE SEQUENCE "rfmo_gid_seq";
CREATE SEQUENCE "service_layer_id_seq";
CREATE SEQUENCE "stat_rect_gid_seq";
CREATE SEQUENCE "system_configurations_id_seq";
CREATE SEQUENCE "user_areas_gid_seq";
CREATE SEQUENCE "user_scope_id_seq";


DROP TABLE "report_layer_config";
DROP TABLE "fao";
DROP TABLE "area_status";
DROP TABLE "report_connect_service_areas";
DROP TABLE "bookmark";
DROP TABLE "eez";
DROP TABLE "gfcm";
DROP TABLE "port";
DROP TABLE "area_location_types";
DROP TABLE "port_area";
DROP TABLE "report_connect_spatial";
DROP TABLE "rfmo";
DROP TABLE "rac";
DROP TABLE "service_layer";
DROP TABLE "stat_rect";
DROP TABLE "custom_area_properties";
DROP TABLE "system_configurations";
DROP TABLE "user_scope";
DROP TABLE "countries";
DROP TABLE "provider_format";
DROP TABLE "projection";
DROP TABLE "custom_areas";
DROP TABLE "user_areas";

-- ----------------------------
--  Table structure for "report_layer_config"
-- ----------------------------

CREATE TABLE "report_layer_config" (   "id" NUMBER(20,0), "report_connect_service_id" NUMBER(20,0), "area_connect_group_id" NUMBER(20,0), "layer_order" NUMBER(20,0), "sld" NCLOB);

-- ----------------------------
--  Table structure for "fao"
-- ----------------------------

CREATE TABLE "fao" (   "id" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "name" NVARCHAR2(255), "code" NVARCHAR2(20), "enabled" NCHAR(1), "enabled_on" DATE);

-- ----------------------------
--  Table structure for "area_status"
-- ----------------------------
CREATE TABLE "area_status" (   "id" NUMBER(20,0), "user_area_id" NUMBER(20,0), "start_date" DATE, "end_date" DATE, "is_visible" NCHAR(1));

-- ----------------------------
--  Table structure for "report_connect_service_areas"
-- ----------------------------
CREATE TABLE "report_connect_service_areas" (   "id" NUMBER(20,0), "report_connect_spatial_id" NUMBER(20,0), "service_layer_id" NUMBER(11,0), "sql_filter" NCLOB, "layer_order" NUMBER(11,0), "layer_type" NVARCHAR2(255), "area_type" NVARCHAR2(255));

-- ----------------------------
--  Table structure for "bookmark"
-- ----------------------------
CREATE TABLE "bookmark" (   "id" NUMBER(20,0), "srs" NUMBER(11,0), "name" NVARCHAR2(255), "extent" NVARCHAR2(255), "created_by" NVARCHAR2(255));

-- ----------------------------
--  Records of "bookmark"
-- ----------------------------
INSERT INTO "bookmark" ("id", "srs", "name", "extent", "created_by") VALUES ('1', '999', 'greg', 'ekfezmlklfezk', 'rep_power');
COMMIT;

-- ----------------------------
--  Table structure for "eez"
-- ----------------------------
CREATE TABLE "eez" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "name" NVARCHAR2(255), "country" NVARCHAR2(100), "sovereign" NVARCHAR2(100), "remarks" NVARCHAR2(150), "sov_id" NUMBER(11,0), "eez_id" NUMBER(11,0), "code" NVARCHAR2(20), "mrgid" NUMBER, "date_chang" NVARCHAR2(50), "area_m2" NUMBER, "longitude" NUMBER, "latitude" NUMBER, "mrgid_eez" NUMBER(11,0), "enabled" NCHAR(1), "enabled_on" DATE);

-- ----------------------------
--  Table structure for "gfcm"
-- ----------------------------
CREATE TABLE "gfcm" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "name" NVARCHAR2(255), "code" NVARCHAR2(20), "enabled" NCHAR(1), "enabled_on" DATE);

-- ----------------------------
--  Table structure for "port"
-- ----------------------------
CREATE TABLE "port" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "country_code" NVARCHAR2(3), "code" NVARCHAR2(10), "name" NVARCHAR2(100), "fishing_port" NVARCHAR2(1), "landing_place" NVARCHAR2(1), "commercial_port" NVARCHAR2(1), "enabled" NCHAR(1), "enabled_on" DATE);

-- ----------------------------
--  Table structure for "area_location_types"
-- ----------------------------
CREATE TABLE "area_location_types" (   "id" NUMBER(11,0), "service_layer_id" NUMBER(11,0), "type_name" NVARCHAR2(255), "area_type_desc" NVARCHAR2(255), "area_db_table" NVARCHAR2(255), "is_system_wide" NCHAR(1), "is_location" NCHAR(1));

-- ----------------------------
--  Table structure for "port_area"
-- ----------------------------
CREATE TABLE "port_area" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "code" NVARCHAR2(20), "name" NVARCHAR2(255), "enabled" NCHAR(1), "enabled_on" DATE);

-- ----------------------------
--  Table structure for "rac"
-- ----------------------------

CREATE TABLE "rac" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "enabled" NCHAR(1), "code" NVARCHAR2(20), "name" NVARCHAR2(255), "enabled_on" DATE);

-- ----------------------------
--  Table structure for "projection"
-- ----------------------------
CREATE TABLE "projection" (   "id" NUMBER(20,0), "name" NVARCHAR2(255), "srs_code" NUMBER(11,0), "proj_def" NCLOB, "formats" NVARCHAR2(255), "units" NVARCHAR2(255), "world" NCHAR(1), "extent" NVARCHAR2(255), "axis" NVARCHAR2(3));

-- ----------------------------
--  Records of "projection"
-- ----------------------------
INSERT INTO "projection" ("id", "name", "srs_code", "proj_def", "formats", "units", "world", "extent", "axis") VALUES ('1', 'Spherical Mercator', '3857', '+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs', 'm', 'm', true, '-20026376.39;-20048966.10;20026376.39;20048966.10', 'enu');
INSERT INTO "projection" ("id", "name", "srs_code", "proj_def", "formats", "units", "world", "extent", "axis") VALUES ('2', 'WGS 84', '4326', '+proj=longlat +datum=WGS84 +no_defs', 'dd;dms;ddm', 'degrees', true, '-180;-90;180;90', 'neu');
COMMIT;

-- ----------------------------
--  Table structure for "report_connect_spatial"
-- ----------------------------
CREATE TABLE "report_connect_spatial" (   "id" NUMBER(20,0), "report_id" NUMBER(20,0), "map_center" NVARCHAR2(255), "map_zoom" NUMBER(11,0), "map_proj_id" NUMBER(20,0), "display_proj_id" NUMBER(20,0), "display_format" NVARCHAR2(255), "measurement_units" NVARCHAR2(255), "scalebar_units" NVARCHAR2(255), "styles_settings" NCLOB, "app_version" NVARCHAR2(255), "visibility_settings" NCLOB);

-- ----------------------------
--  Table structure for "rfmo"
-- ----------------------------
CREATE TABLE "rfmo" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "code" NVARCHAR2(20), "name" NVARCHAR2(255), "tuna" NVARCHAR2(10), "enabled" NCHAR(1), "enabled_on" DATE);

-- ----------------------------
--  Table structure for "provider_format"
-- ----------------------------
CREATE TABLE "provider_format" (   "id" NUMBER(11,0), "service_type" NVARCHAR2(10));

-- ----------------------------
--  Records of "provider_format"
-- ----------------------------
INSERT INTO "provider_format" ("id", "service_type") VALUES ('1', 'OSM');
INSERT INTO "provider_format" ("id", "service_type") VALUES ('2', 'WMS');
INSERT INTO "provider_format" ("id", "service_type") VALUES ('3', 'OSEA');
INSERT INTO "provider_format" ("id", "service_type") VALUES ('4', 'TMS');
INSERT INTO "provider_format" ("id", "service_type") VALUES ('5', 'BING');
COMMIT;

-- ----------------------------
--  Table structure for "service_layer"
-- ----------------------------
CREATE TABLE "service_layer" (   "id" NUMBER(11,0), "user_id" NUMBER(20,0), "name" NVARCHAR2(255), "layer_desc" NVARCHAR2(255), "provider_format_id" NUMBER(11,0), "service_url" NCLOB, "geo_name" NVARCHAR2(255), "srs_code" NUMBER(11,0), "short_copyright" NVARCHAR2(255), "long_copyright" NCLOB, "is_internal" NCHAR(1), "style_geom" NVARCHAR2(255), "style_label" NVARCHAR2(255), "style_label_geom" NVARCHAR2(255), "subtype" NVARCHAR2(255));

-- ----------------------------
--  Records of "service_layer"
-- ----------------------------
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('1', null, 'EEZ', 'Exclusive Economic Zone', '2', null, 'uvms:eez', '4326', 'EEZ by UnionVMS', 'Long copyright EEZ by UnionVMS', true, 'eez', 'eez_label', 'eez_label_geom', 'sysarea');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('2', null, 'RFMO', 'Regional Fisheries Management Organisation', '2', null, 'uvms:rfmo', '4326', 'RFMO by UnionVMS', 'Long copyright RFMO by UnionVMS', true, 'rfmo', 'rfmo_label', 'rfmo_label_geom', 'sysarea');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('3', null, 'Countries', 'Countries', '2', null, 'uvms:countries', '4326', 'COUNTRIES by UnionVMS', 'Long copyright COUNTRIES by UnionVMS', true, null, null, null, 'others');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('4', null, 'Ports', 'Ports', '2', null, 'uvms:port', '4326', 'PORTS by UnionVMS', 'Long copyright PORTS by UnionVMS', true, 'port', 'port_label', 'port_label_geom', 'port');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('5', null, 'USERAREA', 'User Areas', '2', null, 'uvms:userareas', '4326', 'USER AREAS by UnionVMS', 'Long copyright USER AREAS by UnionVMS', true, 'userareas', null, 'userareas_label_geom', 'userarea');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('6', null, 'OpenStreetMap', 'OpenStreetMap', '1', null, null, '3857', null, null, false, null, null, null, 'background');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('7', null, 'OpenSeaMap', 'OpenSeaMap', '3', null, null, '3857', null, null, false, null, null, null, 'additional');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('8', null, 'bing_roads', null, '5', null, 'Road', '3857', null, null, false, null, null, null, 'background');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('9', null, 'bing_aerial', null, '5', null, 'Aerial', '3857', null, null, false, null, null, null, 'background');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('10', null, 'bing_aerial_labels', null, '5', null, 'AerialWithLabels', '3857', null, null, false, null, null, null, 'background');
INSERT INTO "service_layer" ("id", "user_id", "name", "layer_desc", "provider_format_id", "service_url", "geo_name", "srs_code", "short_copyright", "long_copyright", "is_internal", "style_geom", "style_label", "style_label_geom", "subtype") VALUES ('11', null, 'Ports', 'A new Description', '2', null, 'uvms:port_area', '4326', 'PORTS by UnionVMS', 'Long copyright PORTS by UnionVMS', true, 'port_area', 'port_area_label', 'port_area_label_geom', 'portarea');
COMMIT;

-- ----------------------------
--  Table structure for "stat_rect"
-- ----------------------------
CREATE TABLE "stat_rect" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "name" NVARCHAR2(255), "code" NVARCHAR2(20), "enabled" NCHAR(1), "enabled_on" DATE);

-- ----------------------------
--  Table structure for "custom_areas"
-- ----------------------------
CREATE TABLE "custom_areas" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY");

-- ----------------------------
--  Table structure for "custom_area_properties"
-- ----------------------------
CREATE TABLE "custom_area_properties" (   "id" NUMBER(11,0), "prop_name" NVARCHAR2(255), "prop_value" NVARCHAR2(255), "custom_areas_id" NUMBER(11,0), "view_ref" NVARCHAR2(255));

-- ----------------------------
--  Table structure for "system_configurations"
-- ----------------------------
CREATE TABLE "system_configurations" (   "id" NUMBER(11,0), "name" NVARCHAR2(255), "value" NCLOB);

-- ----------------------------
--  Records of "system_configurations"
-- ----------------------------
INSERT INTO "system_configurations" ("id", "name", "value") VALUES ('1', 'geo_server_url', 'http://localhost:8080/geoserver/');
INSERT INTO "system_configurations" ("id", "name", "value") VALUES ('2', 'bing_api_key', null);
COMMIT;

-- ----------------------------
--  Table structure for "user_areas"
-- ----------------------------
CREATE TABLE "user_areas" (   "gid" NUMBER(20,0), "user_name" NVARCHAR2(255), "name" NVARCHAR2(255), "code" NVARCHAR2(20), "type" NVARCHAR2(255), "area_desc" NCLOB, "geom" "MDSYS"."SDO_GEOMETRY", "enabled_on" DATE, "created_on" DATE, "start_date" DATE, "end_date" DATE, "dataset_name" NVARCHAR2(255), "enabled" NCHAR(1));

-- ----------------------------
--  Table structure for "user_scope"
-- ----------------------------
CREATE TABLE "user_scope" (   "scope_name" NVARCHAR2(255), "user_area_id" NUMBER(20,0), "id" NUMBER(20,0));

-- ----------------------------
--  Table structure for "countries"
-- ----------------------------
CREATE TABLE "countries" (   "gid" NUMBER(11,0), "geom" "MDSYS"."SDO_GEOMETRY", "sovereignt" NVARCHAR2(32), "sov_a3" NVARCHAR2(3), "type" NVARCHAR2(17), "admin" NVARCHAR2(40), "code" NVARCHAR2(20), "name" NVARCHAR2(200), "name_long" NVARCHAR2(40), "pop_est" NUMBER, "gdp_md_est" NUMBER, "income_grp" NVARCHAR2(23), "continent" NVARCHAR2(23), "region_un" NVARCHAR2(23), "subregion" NVARCHAR2(25), "region_wb" NVARCHAR2(26), "enabled_on" DATE, "enabled" NCHAR(1));

-- ----------------------------
--  Primary key structure for table "report_layer_config"
-- ----------------------------
ALTER TABLE "report_layer_config" ADD CONSTRAINT "report_layer_config_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "fao"
-- ----------------------------
ALTER TABLE "fao" ADD CONSTRAINT "fao_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "area_status"
-- ----------------------------
ALTER TABLE "area_status" ADD CONSTRAINT "area_status_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "report_connect_service_areas"
-- ----------------------------
ALTER TABLE "report_connect_service_areas" ADD CONSTRAINT "report_layer_mapping_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "bookmark"
-- ----------------------------
ALTER TABLE "bookmark" ADD CONSTRAINT "bookmark_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "eez"
-- ----------------------------
ALTER TABLE "eez" ADD CONSTRAINT "eez_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "gfcm"
-- ----------------------------
ALTER TABLE "gfcm" ADD CONSTRAINT "gfcm_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "port"
-- ----------------------------
ALTER TABLE "port" ADD CONSTRAINT "port_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "area_location_types"
-- ----------------------------
ALTER TABLE "area_location_types" ADD CONSTRAINT "area_types_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "port_area"
-- ----------------------------
ALTER TABLE "port_area" ADD CONSTRAINT "port_area_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "rac"
-- ----------------------------
ALTER TABLE "rac" ADD CONSTRAINT "rac_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "projection"
-- ----------------------------
ALTER TABLE "projection" ADD CONSTRAINT "projection_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "report_connect_spatial"
-- ----------------------------
ALTER TABLE "report_connect_spatial" ADD CONSTRAINT "report_connect_spatial_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "rfmo"
-- ----------------------------
ALTER TABLE "rfmo" ADD CONSTRAINT "rfmo_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "provider_format"
-- ----------------------------
ALTER TABLE "provider_format" ADD CONSTRAINT "provider_format_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "service_layer"
-- ----------------------------
ALTER TABLE "service_layer" ADD CONSTRAINT "layer_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "stat_rect"
-- ----------------------------
ALTER TABLE "stat_rect" ADD CONSTRAINT "stat_rect_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "custom_areas"
-- ----------------------------
ALTER TABLE "custom_areas" ADD CONSTRAINT "custom_areas_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "custom_area_properties"
-- ----------------------------
ALTER TABLE "custom_area_properties" ADD CONSTRAINT "custom_area_properties_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "system_configurations"
-- ----------------------------
ALTER TABLE "system_configurations" ADD CONSTRAINT "system_configurations_pkey" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "user_areas"
-- ----------------------------
ALTER TABLE "user_areas" ADD CONSTRAINT "user_areas_pkey" PRIMARY KEY("gid");

-- ----------------------------
--  Primary key structure for table "user_scope"
-- ----------------------------
ALTER TABLE "user_scope" ADD CONSTRAINT "user_scope_id_pk" PRIMARY KEY("id");

-- ----------------------------
--  Primary key structure for table "countries"
-- ----------------------------
ALTER TABLE "countries" ADD CONSTRAINT "countries_pkey" PRIMARY KEY("gid");


-- ----------------------------
--  Unique key structure for table "projection"
-- ----------------------------
ALTER TABLE "projection" ADD CONSTRAINT "srs_code_ekey" UNIQUE("srs_code");

ALTER TABLE "projection" ADD CONSTRAINT "projection_name_ekey" UNIQUE("name");

-- ----------------------------
--  Unique key structure for table "user_areas"
-- ----------------------------
ALTER TABLE "user_areas" ADD CONSTRAINT "dataset_name_ekey" UNIQUE("dataset_name");

-- ----------------------------
--  Foreign key structure for table "report_layer_config"
-- ----------------------------
alter table "report_layer_config" add constraint "fk_report_connect_areas" FOREIGN KEY ("report_connect_service_id") references "report_connect_service_areas" ("id")
initially deferred deferrable;

-- ----------------------------
--  Foreign key structure for table "area_status"
-- ----------------------------
alter table "area_status" add constraint "fk_user_area" FOREIGN KEY ("user_area_id") references "user_areas" ("gid")
initially deferred deferrable;

-- ----------------------------
--  Foreign key structure for table "report_connect_service_areas"
-- ----------------------------
alter table "report_connect_service_areas" add constraint "fk_report_connect_spatial" FOREIGN KEY ("report_connect_spatial_id") references "report_connect_spatial" ("id")
initially deferred deferrable;

alter table "report_connect_service_areas" add constraint "fk_service_layer" FOREIGN KEY ("service_layer_id") references "service_layer" ("id")
initially deferred deferrable;

-- ----------------------------
--  Foreign key structure for table "area_location_types"
-- ----------------------------
alter table "area_location_types" add constraint "fk_service_layer_id" FOREIGN KEY ("service_layer_id") references "service_layer" ("id")
initially deferred deferrable;

-- ----------------------------
--  Foreign key structure for table "report_connect_spatial"
-- ----------------------------
alter table "report_connect_spatial" add constraint "fk_display_proj" FOREIGN KEY ("display_proj_id") references "projection" ("id")
initially deferred deferrable;

alter table "report_connect_spatial" add constraint "fk_map_proj" FOREIGN KEY ("map_proj_id") references "projection" ("id")
initially deferred deferrable;

-- ----------------------------
--  Foreign key structure for table "service_layer"
-- ----------------------------
alter table "service_layer" add constraint "fk_provider_format" FOREIGN KEY ("provider_format_id") references "provider_format" ("id")
initially deferred deferrable;

-- ----------------------------
--  Foreign key structure for table "custom_area_properties"
-- ----------------------------
alter table "custom_area_properties" add constraint "fk_custom_areas" FOREIGN KEY ("custom_areas_id") references "custom_areas" ("gid")
initially deferred deferrable;

-- ----------------------------
--  Foreign key structure for table "user_scope"
-- ----------------------------
alter table "user_scope" add constraint "fk_user_area_scope" FOREIGN KEY ("user_area_id") references "user_areas" ("gid")
initially deferred deferrable;