------Drop Sequences ----
DROP SEQUENCE IF EXISTS spatial.area_group_id_seq;
DROP SEQUENCE IF EXISTS spatial.area_group_mapping_area_id_seq;
DROP SEQUENCE IF EXISTS spatial.area_status_id_seq;
DROP SEQUENCE IF EXISTS spatial.area_types_id_seq;
DROP SEQUENCE IF EXISTS spatial.eez_gid_seq;
DROP SEQUENCE IF EXISTS spatial.fao_id_seq;
DROP SEQUENCE IF EXISTS spatial.gfcm_id_seq;
DROP SEQUENCE IF EXISTS spatial.layer_group_id_seq;
DROP SEQUENCE IF EXISTS spatial.layer_id_seq;
DROP SEQUENCE IF EXISTS spatial.rac_id_seq;
DROP SEQUENCE IF EXISTS spatial.report_id_seq;
DROP SEQUENCE IF EXISTS spatial.report_layer_mapping_id_seq;
DROP SEQUENCE IF EXISTS spatial.rfmo_id_seq;
DROP SEQUENCE IF EXISTS spatial.spatial_configuration_id_seq;
DROP SEQUENCE IF EXISTS spatial.stat_rect_id_seq;
DROP SEQUENCE IF EXISTS spatial.user_areas_id_seq;

------Drop tables ----
DROP TABLE IF EXISTS spatial.area_connect_group;
DROP TABLE IF EXISTS spatial.area_group;
DROP TABLE IF EXISTS spatial.area_status;
DROP TABLE IF EXISTS spatial.area_location_types;
DROP TABLE IF EXISTS spatial.bookmark;
DROP TABLE IF EXISTS spatial.countries; ---Only for tests---
DROP TABLE IF EXISTS spatial.eez;
DROP TABLE IF EXISTS spatial.fao;
DROP TABLE IF EXISTS spatial.gfcm;
DROP TABLE IF EXISTS spatial.ports;
DROP TABLE IF EXISTS spatial.projection;
DROP TABLE IF EXISTS spatial.provider_format;
DROP TABLE IF EXISTS spatial.rac;
DROP TABLE IF EXISTS spatial.report_connect_service_areas;
DROP TABLE IF EXISTS spatial.report_connect_spatial;
DROP TABLE IF EXISTS spatial.report_layer_config;
DROP TABLE IF EXISTS spatial.rfmo;
DROP TABLE IF EXISTS spatial.service_layer;
DROP TABLE IF EXISTS spatial.stat_rect;
DROP TABLE IF EXISTS spatial.user_areas;

------Drop liquibase changelog tables ----
DROP TABLE IF EXISTS spatial.databasechangelog;	
DROP TABLE IF EXISTS spatial.databasechangeloglock;