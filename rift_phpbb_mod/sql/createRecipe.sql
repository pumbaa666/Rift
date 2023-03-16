drop table if exists rift_recipe_required_skill;
drop table if exists rift_recipe_multilanguage_text;
drop table if exists rift_recipe_list;
drop table if exists rift_recipe_item_quantity;
drop table if exists rift_recipe_recipe;

CREATE TABLE rift_recipe_required_skill (
pk_required_skill_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_recipe_required_skill (name) values ('LIGHT_WEAPONSMITH');
insert into rift_recipe_required_skill (name) values ('OUTFITTER');
insert into rift_recipe_required_skill (name) values ('RUNECRAFTER');
insert into rift_recipe_required_skill (name) values ('ARMORSMITH');
insert into rift_recipe_required_skill (name) values ('ALCHEMIST');
insert into rift_recipe_required_skill (name) values ('ARTIFICER');
insert into rift_recipe_required_skill (name) values ('MINING');
insert into rift_recipe_required_skill (name) values ('BUTCHERING');


CREATE TABLE rift_recipe_multilanguage_text (
pk_multilanguage_text_id INT NOT NULL PRIMARY KEY ,
english VARCHAR( 200 ) NULL ,
french VARCHAR( 200 ) NULL ,
german VARCHAR( 200 ) NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;


CREATE TABLE rift_recipe_list (
pk_list_id INT NOT NULL PRIMARY KEY ,
fk_group_id INT NOT NULL ,
fk_enum_id VARCHAR ( 50 ) NOT NULL ,
fk_recipe_id INT NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;


CREATE TABLE rift_recipe_item_quantity (
pk_item_quantity_id INT NOT NULL PRIMARY KEY ,
fk_item_key VARCHAR ( 50 ) NOT NULL ,
quantity INT NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;


CREATE TABLE rift_recipe_recipe (
pk_recipe_id INT NOT NULL PRIMARY KEY ,
recipe_key VARCHAR( 50 ) NOT NULL ,
fk_ml_name INT NOT NULL ,
fk_ml_description INT NULL ,
fk_item_quantity INT NOT NULL ,
fk_l_ingredients INT NOT NULL ,
trainer_cost INT NULL ,
fk_required_skill INT NOT NULL ,
required_skill_point INT NOT NULL ,
high_until INT NOT NULL ,
medium_until INT NOT NULL ,
low_until INT NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;
