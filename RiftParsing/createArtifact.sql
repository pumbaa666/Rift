drop table if exists rift_artifact_difficulty;
drop table if exists rift_artifact_location;
drop table if exists rift_artifact_multilanguage_text;
drop table if exists rift_artifact_list;
drop table if exists rift_artifact_item_quantity;
drop table if exists rift_artifact_artifact;

CREATE TABLE rift_artifact_difficulty (
pk_difficulty_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_artifact_difficulty (name) values ('EASY');
insert into rift_artifact_difficulty (name) values ('NORMAL');
insert into rift_artifact_difficulty (name) values ('HARD');
insert into rift_artifact_difficulty (name) values ('VERY_HARD');

CREATE TABLE rift_artifact_location (
pk_location_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_artifact_location (name) values ('SILVERWOOD');
insert into rift_artifact_location (name) values ('FREEMARCH');
insert into rift_artifact_location (name) values ('GLOAMWOOD');
insert into rift_artifact_location (name) values ('STONEFIELD');
insert into rift_artifact_location (name) values ('SCARLET_GORGE');
insert into rift_artifact_location (name) values ('SCARWOOD_REACH');
insert into rift_artifact_location (name) values ('DROUGHTLANDS');
insert into rift_artifact_location (name) values ('MOONSHADE_HIGHLANDS');
insert into rift_artifact_location (name) values ('IRON_PINE_PEAK');
insert into rift_artifact_location (name) values ('SHIMMERSAND');
insert into rift_artifact_location (name) values ('STILLMOOR');
insert into rift_artifact_location (name) values ('SANCTUM');
insert into rift_artifact_location (name) values ('MERIDIAN');
insert into rift_artifact_location (name) values ('WORLD');

CREATE TABLE rift_artifact_multilanguage_text (
pk_multilanguage_text_id INT NOT NULL PRIMARY KEY ,
english VARCHAR( 200 ) NULL ,
french VARCHAR( 200 ) NULL ,
german VARCHAR( 200 ) NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;


CREATE TABLE rift_artifact_list (
pk_list_id INT NOT NULL PRIMARY KEY ,
fk_group_id INT NOT NULL ,
fk_enum_id VARCHAR ( 50 ) NOT NULL ,
fk_artifact_id INT NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;


CREATE TABLE rift_artifact_item_quantity (
pk_item_quantity_id INT NOT NULL PRIMARY KEY ,
fk_item_key VARCHAR ( 50 ) NOT NULL ,
quantity INT NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;


CREATE TABLE rift_artifact_artifact (
pk_artifact_id INT NOT NULL PRIMARY KEY ,
artifact_key INT NOT NULL ,
fk_ml_name INT NOT NULL ,
fk_ml_description INT NULL ,
level INT NULL ,
fk_difficulty_id INT NULL ,
fk_location_id INT NULL ,
fk_l_item_id INT NOT NULL ,
fk_l_guaranteed_id INT NOT NULL ,
fk_l_pick_one_id INT NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;
