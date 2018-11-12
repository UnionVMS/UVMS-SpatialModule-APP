
/* Drop Tables */

DROP TABLE IF EXISTS spatial.report_layer_config;
DROP TABLE IF EXISTS spatial.area_connect_group;
DROP TABLE IF EXISTS spatial.report_connect_service_areas;
DROP TABLE IF EXISTS spatial.area_group;
DROP TABLE IF EXISTS spatial.area_status;
DROP TABLE IF EXISTS spatial.area_types;
DROP TABLE IF EXISTS spatial.bookmark;
DROP TABLE IF EXISTS spatial.eez;
DROP TABLE IF EXISTS spatial.fao;
DROP TABLE IF EXISTS spatial.gfcm;
DROP TABLE IF EXISTS spatial.ports;
DROP TABLE IF EXISTS spatial.report_connect_spatial;
DROP TABLE IF EXISTS spatial.projection;
DROP TABLE IF EXISTS spatial.service_layer;
DROP TABLE IF EXISTS spatial.provider_format;
DROP TABLE IF EXISTS spatial.rac;
DROP TABLE IF EXISTS spatial.rfmo;
DROP TABLE IF EXISTS spatial.stat_rect;
DROP TABLE IF EXISTS spatial.user_areas;
DROP TABLE IF EXISTS spatial.custom_areas;
DROP TABLE IF EXISTS spatial.custom_area_properties;


/* Drop Sequences */

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




/* Create Sequences */

CREATE SEQUENCE spatial.area_group_mapping_area_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.gfcm_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.layer_group_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.layer_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.rac_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.report_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.report_layer_mapping_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.rfmo_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.spatial_configuration_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.stat_rect_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial.user_areas_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;



/* Create Tables */

CREATE TABLE spatial.area_connect_group
(
	id bigserial NOT NULL,
	area_type_id int NOT NULL,
	area_group_id int NOT NULL,
	-- It is not nullable because we are not sure that selection within the predefine areas will be supported (for instance EEZ feature 1)
	area_id bigint NOT NULL,
	PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.area_group
(
	id serial NOT NULL,
	user_id bigint NOT NULL,
	scope_id bigint NOT NULL,
	bookmark_definition text NOT NULL,
	description text,
	created_on timestamp NOT NULL,
	CONSTRAINT area_group_pkey PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.area_status
(
	id bigserial NOT NULL,
	-- Unique primary key of the user defined areas
	user_area_id bigint NOT NULL,
	-- Starting date/time of the active/inactive status.
	start_date timestamp NOT NULL,
	-- End date/time of the active/inactive status.
	end_date timestamp NOT NULL,
	-- If the table is visible to other users or not (true for visible)
	is_visible char(1) NOT NULL,
	CONSTRAINT area_status_pkey PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.area_types
(
	id serial NOT NULL,
	service_layer_id int NOT NULL,
	-- Area type
	type_name varchar(255) NOT NULL UNIQUE,
	area_type_desc text,
	-- This will keep the name of the table of the particular area type. For instance, user defined maps are stored in USER_AREA.
	area_db_table varchar(255) NOT NULL,
	is_system_area char(1) NOT NULL,
	CONSTRAINT area_types_pkey PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.bookmark
(
	id bigserial NOT NULL,
	name varchar(255) NOT NULL,
	srs int NOT NULL,
	extent varchar(255) NOT NULL,
	created_by varchar(255) NOT NULL
	PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.eez
(
	gid serial NOT NULL,
	geom geometry(Multipolygon, 4326),
	eez varchar(200),
	country varchar(100),
	sovereign varchar(100),
	remarks varchar(150),
	sov_id int,
	eez_id int,
	iso_3digit varchar(5),
	mrgid numeric,
	date_chang varchar(50),
	area_m2 float,
	longitude float,
	latitude float,
	mrgid_eez int,
	CONSTRAINT eez_pkey PRIMARY KEY (gid)
) WITHOUT OIDS;

CREATE TABLE spatial.fao
(
	id serial NOT NULL,
	CONSTRAINT fao_pkey PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.gfcm
(
	-- ID
	gid serial NOT NULL,
	CONSTRAINT gfcm_pkey PRIMARY KEY (gid)
) WITHOUT OIDS;


CREATE TABLE spatial.ports
(
	gid bigserial NOT NULL,
	PRIMARY KEY (gid)
) WITHOUT OIDS;


CREATE TABLE spatial.projection
(
	id bigint NOT NULL,
	name varchar(255) NOT NULL UNIQUE,
	-- epsg code
	srs_code int NOT NULL UNIQUE,
	-- proj4js definition
	proj_def text NOT NULL,
	-- dd;dms;ddm
	-- m
	formats varchar(255) NOT NULL,
	units varchar(255) NOT NULL,
	world char(1) NOT NULL,
	PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.provider_format
(
	id serial NOT NULL,
	service_type varchar(10) NOT NULL,
	PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.rac
(
	-- ID
	gid serial NOT NULL,
	CONSTRAINT rac_pkey PRIMARY KEY (gid)
) WITHOUT OIDS;


CREATE TABLE spatial.report_connect_service_areas
(
	id bigserial NOT NULL,
	report_id bigint NOT NULL,
	-- soft link to the Layers table
	service_layer_id int,
	area_group_id int,
	-- this will contain the actual area ID in a particular area table
	sql_filter text,
	-- The order of the layer group
	layer_order int DEFAULT 0 NOT NULL,
	CONSTRAINT report_layer_mapping_pkey PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.report_connect_spatial
(
	id bigserial NOT NULL,
	report_id bigint NOT NULL,
	map_center varchar(255) NOT NULL,
	map_zoom int NOT NULL,
	-- it might become not nullable field
	map_extent varchar,
	map_proj_id bigint,
	display_proj_id bigint,
	display_format varchar(255),
	measurement_units varchar(255),
	scalebar_units varchar(255),
	vector_styles text,
	app_version varchar(255) NOT NULL,
	PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.report_layer_config
(
	id bigserial NOT NULL,
	report_connect_service_areas_id bigint,
	area_connect_group_id bigint NOT NULL,
	layer_order bigint NOT NULL,
	sld text,
	PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.rfmo
(
	-- ID
	gid serial NOT NULL,
	geom geometry(Multipolygon, 4326),
	rfmo varchar(10),
	name varchar(125),
	tuna varchar(10),
	CONSTRAINT rfmo_pkey PRIMARY KEY (gid)
) WITHOUT OIDS;


CREATE TABLE spatial.service_layer
(
	id serial NOT NULL,
	user_id bigint,
	-- Name of the layer to be displayed at the front-end
	name varchar(255) NOT NULL,
	-- Description of the layer
	layer_desc text,
	-- The type should be PROVIDER_FORMAT
	-- 
	-- to create it:
	-- CREATE TYPE PROVIDER_FORMAT AS ENUM ('OSM', 'WMS', 'OSEA','TMS')
	provider_format_id int NOT NULL,
	-- if resource type is WMS than this field is mandatory
	service_url varchar,
	-- GeoServer layer name
	geo_name varchar(255),
	-- Spatial reference system which might be prefixed with EPSG or SRID for the front-end.
	-- 
	-- This is only to be used if the layer is limited to one and only one EPSG code
	srs_code int,
	short_copyright varchar(255) NOT NULL,
	long_copyright text,
	-- false if it is thematic layer, true for background
	is_background char(1) NOT NULL,
	is_internal char(1) NOT NULL,
	style_geom varchar(255),
	style_label varchar(255),
	style_label_geom varchar(255),
	CONSTRAINT layer_pkey PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE spatial.stat_rect
(
	-- ID
	gid serial NOT NULL,
	CONSTRAINT stat_rect_pkey PRIMARY KEY (gid)
) WITHOUT OIDS;


CREATE TABLE spatial.user_areas
(
	-- Unique primary key of the user defined areas
	gid bigserial NOT NULL,
	user_id bigint NOT NULL,
	scope_id bigint,
	-- Area name
	name varchar(255) NOT NULL,
	-- Description of the area
	area_desc text,
	-- Geometry of the area
	geom geometry(Multipolygon, 4326) NOT NULL,
	created_on timestamp NOT NULL,
	CONSTRAINT user_areas_pkey PRIMARY KEY (gid)
) WITHOUT OIDS;


CREATE TABLE spatial.custom_areas
(
	gid serial NOT NULL,
	geom geometry (Multipolygon, 4326),
	CONSTRAINT custom_areas_pkey PRIMARY KEY (gid)
) WITHOUT OIDS;


CREATE TABLE spatial.custom_area_properties
(
	id serial NOT NULL,
	prop_name varchar(255) NOT NULL,
	prop_value varchar(255) NOT NULL,
	custom_areas_id int NOT NULL,
	view_ref varchar(255) NOT NULL,
	CONSTRAINT custom_area_properties_pkey PRIMARY KEY (id)
) WITHOUT OIDS;


/* Create Foreign Keys */

ALTER TABLE spatial.report_layer_config
	ADD FOREIGN KEY (area_connect_group_id)
	REFERENCES spatial.area_connect_group (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.area_connect_group
	ADD FOREIGN KEY (area_group_id)
	REFERENCES spatial.area_group (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.report_connect_service_areas
	ADD FOREIGN KEY (area_group_id)
	REFERENCES spatial.area_group (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.area_connect_group
	ADD FOREIGN KEY (area_type_id)
	REFERENCES spatial.area_types (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.report_connect_spatial
	ADD FOREIGN KEY (map_proj_id)
	REFERENCES spatial.projection (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.report_connect_spatial
	ADD FOREIGN KEY (display_proj_id)
	REFERENCES spatial.projection (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.service_layer
	ADD FOREIGN KEY (provider_format_id)
	REFERENCES spatial.provider_format (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.report_layer_config
	ADD FOREIGN KEY (report_connect_service_areas_id)
	REFERENCES spatial.report_connect_service_areas (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.report_connect_service_areas
	ADD FOREIGN KEY (report_id)
	REFERENCES spatial.report_connect_spatial (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.area_types
	ADD FOREIGN KEY (service_layer_id)
	REFERENCES spatial.service_layer (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.report_connect_service_areas
	ADD FOREIGN KEY (service_layer_id)
	REFERENCES spatial.service_layer (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE spatial.area_status
	ADD FOREIGN KEY (user_area_id)
	REFERENCES spatial.user_areas (gid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

ALTER TABLE spatial.custom_area_properties
	ADD FOREIGN KEY (custom_areas_id)
	REFERENCES spatial.custom_areas (gid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

/* Comments */

COMMENT ON COLUMN spatial.area_connect_group.area_id IS 'It is not nullable because we are not sure that selection within the predefine areas will be supported (for instance EEZ feature 1)';
COMMENT ON COLUMN spatial.area_status.user_area_id IS 'Unique primary key of the user defined areas';
COMMENT ON COLUMN spatial.area_status.start_date IS 'Starting date/time of the active/inactive status.';
COMMENT ON COLUMN spatial.area_status.end_date IS 'End date/time of the active/inactive status.';
COMMENT ON COLUMN spatial.area_status.is_visible IS 'If the table is visible to other users or not (true for visible)';
COMMENT ON COLUMN spatial.area_types.type_name IS 'Area type';
COMMENT ON COLUMN spatial.area_types.area_db_table IS 'This will keep the name of the table of the particular area type. For instance, user defined maps are stored in USER_AREA.';
COMMENT ON COLUMN spatial.gfcm.gid IS 'ID';
COMMENT ON COLUMN spatial.projection.srs_code IS 'epsg code';
COMMENT ON COLUMN spatial.projection.proj_def IS 'proj4js definition';
COMMENT ON COLUMN spatial.projection.formats IS 'dd;dms;ddm
m';
COMMENT ON COLUMN spatial.rac.gid IS 'ID';
COMMENT ON COLUMN spatial.report_connect_service_areas.service_layer_id IS 'soft link to the Layers table';
COMMENT ON COLUMN spatial.report_connect_service_areas.sql_filter IS 'this will contain the actual area ID in a particular area table';
COMMENT ON COLUMN spatial.report_connect_service_areas.layer_order IS 'The order of the layer group';
COMMENT ON COLUMN spatial.report_connect_spatial.map_extent IS 'it might become not nullable field';
COMMENT ON COLUMN spatial.rfmo.gid IS 'ID';
COMMENT ON COLUMN spatial.service_layer.name IS 'Name of the layer to be displayed at the front-end';
COMMENT ON COLUMN spatial.service_layer.layer_desc IS 'Description of the layer';
COMMENT ON COLUMN spatial.service_layer.provider_format_id IS 'link to PROVIDER_FORMAT table';
COMMENT ON COLUMN spatial.service_layer.service_url IS 'if service type is WMS than this field is mandatory';
COMMENT ON COLUMN spatial.service_layer.geo_name IS 'GeoServer layer name';
COMMENT ON COLUMN spatial.service_layer.srs_code IS 'Spatial reference system which might be prefixed with EPSG or SRID for the front-end.

This is only to be used if the layer is limited to one and only one EPSG code';
COMMENT ON COLUMN spatial.service_layer.is_background IS 'false if it is thematic layer, true for background';
COMMENT ON COLUMN spatial.stat_rect.gid IS 'ID';
COMMENT ON COLUMN spatial.user_areas.gid IS 'Unique primary key of the user defined areas';
COMMENT ON COLUMN spatial.user_areas.name IS 'Area name';
COMMENT ON COLUMN spatial.user_areas.area_desc IS 'Description of the area';
COMMENT ON COLUMN spatial.user_areas.geom IS 'Geometry of the area';


/* Insert into provider format table */

INSERT INTO TABLE spatial.provider_format VALUES (1, 'OSM');
INSERT INTO TABLE spatial.provider_format VALUES (2, 'WSM');
INSERT INTO TABLE spatial.provider_format VALUES (3, 'OSEA');
INSERT INTO TABLE spatial.provider_format VALUES (4, 'TMS');



