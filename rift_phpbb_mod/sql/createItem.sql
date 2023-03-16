drop table if exists rift_item_armor_type;
drop table if exists rift_item_calling;
drop table if exists rift_item_collectible;
drop table if exists rift_item_damage_type;
drop table if exists rift_item_item;
drop table if exists rift_item_list;
drop table if exists rift_item_multilanguage_text;
drop table if exists rift_item_on_equip;
drop table if exists rift_item_on_use;
drop table if exists rift_item_rarity;
drop table if exists rift_item_rift_gem;
drop table if exists rift_item_rune_allowed_slots;
drop table if exists rift_item_salvage_skill;
drop table if exists rift_item_slot;
drop table if exists rift_item_soulbound_trigger;
drop table if exists rift_item_weapon_type;

CREATE TABLE rift_item_armor_type (
pk_armor_type_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_armor_type (name) values ('CLOTH');
insert into rift_item_armor_type (name) values ('LEATHER');
insert into rift_item_armor_type (name) values ('CHAIN');
insert into rift_item_armor_type (name) values ('PLATE');


CREATE TABLE rift_item_calling (
pk_calling_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_calling (name) values ('ROGUE');
insert into rift_item_calling (name) values ('CLERIC');
insert into rift_item_calling (name) values ('MAGE');
insert into rift_item_calling (name) values ('WARRIOR');


CREATE TABLE rift_item_collectible (
pk_collectible_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_collectible (name) values ('BOOKS');
insert into rift_item_collectible (name) values ('MOUNT');
insert into rift_item_collectible (name) values ('PETS');


CREATE TABLE rift_item_damage_type (
pk_damage_type_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_damage_type (name) values ('AIR');
insert into rift_item_damage_type (name) values ('DEATH');
insert into rift_item_damage_type (name) values ('LIFE');
insert into rift_item_damage_type (name) values ('WATER');
insert into rift_item_damage_type (name) values ('FIRE');
insert into rift_item_damage_type (name) values ('EARTH');


CREATE TABLE rift_item_rarity (
pk_rarity_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_rarity (name) values ('TRASH');
insert into rift_item_rarity (name) values ('COMMON');
insert into rift_item_rarity (name) values ('UNCOMMON');
insert into rift_item_rarity (name) values ('RARE');
insert into rift_item_rarity (name) values ('EPIC');
insert into rift_item_rarity (name) values ('RELIC');
insert into rift_item_rarity (name) values ('QUEST_ITEM');


CREATE TABLE rift_item_rift_gem (
pk_rift_gem_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_rift_gem (name) values ('LESSER');
insert into rift_item_rift_gem (name) values ('GREATER');


CREATE TABLE rift_item_rune_allowed_slots (
pk_rune_allowed_slots_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_rune_allowed_slots (name) values ('GLOVES');
insert into rift_item_rune_allowed_slots (name) values ('WEAPON_2H');
insert into rift_item_rune_allowed_slots (name) values ('FEET');
insert into rift_item_rune_allowed_slots (name) values ('CHEST');
insert into rift_item_rune_allowed_slots (name) values ('SHOULDERS');
insert into rift_item_rune_allowed_slots (name) values ('PANTS');
insert into rift_item_rune_allowed_slots (name) values ('BELT');
insert into rift_item_rune_allowed_slots (name) values ('LEGS');
insert into rift_item_rune_allowed_slots (name) values ('WEAPON_MAIN');
insert into rift_item_rune_allowed_slots (name) values ('HELMET');
insert into rift_item_rune_allowed_slots (name) values ('WEAPON_RANGED');


CREATE TABLE rift_item_salvage_skill (
pk_salvage_skill_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_salvage_skill (name) values ('ARTIFICER');


CREATE TABLE rift_item_slot (
pk_slot_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_slot (name) values ('HELMET');
insert into rift_item_slot (name) values ('CHEST');
insert into rift_item_slot (name) values ('ONE_HAND');
insert into rift_item_slot (name) values ('NECK');
insert into rift_item_slot (name) values ('TWO_HANDED');
insert into rift_item_slot (name) values ('LEGS');
insert into rift_item_slot (name) values ('FEET');
insert into rift_item_slot (name) values ('WEAPON_RANGED');
insert into rift_item_slot (name) values ('BELT');
insert into rift_item_slot (name) values ('OFF_HAND');
insert into rift_item_slot (name) values ('RING');
insert into rift_item_slot (name) values ('TRINKET_2');
insert into rift_item_slot (name) values ('SHOULDERS');
insert into rift_item_slot (name) values ('GLOVES');
insert into rift_item_slot (name) values ('MAIN_HAND');
insert into rift_item_slot (name) values ('MOUNT');
insert into rift_item_slot (name) values ('WEAPON_OFF');
insert into rift_item_slot (name) values ('RIFT_VESSEL');
insert into rift_item_slot (name) values ('WEAPON_2H');


CREATE TABLE rift_item_soulbound_trigger (
pk_soulbound_trigger_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 20 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_soulbound_trigger (name) values ('BIND_ON_EQUIP');
insert into rift_item_soulbound_trigger (name) values ('BIND_ON_PICKUP');
insert into rift_item_soulbound_trigger (name) values ('BIND_ON_USE');


CREATE TABLE rift_item_weapon_type (
pk_weapon_type_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
name VARCHAR( 30 ) NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

insert into rift_item_weapon_type (name) values ('TOTEM');
insert into rift_item_weapon_type (name) values ('SHIELD');
insert into rift_item_weapon_type (name) values ('ONE_H_DAGGER');
insert into rift_item_weapon_type (name) values ('ONE_H_SWORD');
insert into rift_item_weapon_type (name) values ('ONE_H_MACE');
insert into rift_item_weapon_type (name) values ('ONE_H_AXE');
insert into rift_item_weapon_type (name) values ('ONE_H_WAND');
insert into rift_item_weapon_type (name) values ('ONE_H_WAND_AIR');
insert into rift_item_weapon_type (name) values ('ONE_H_WAND_LIFE');
insert into rift_item_weapon_type (name) values ('ONE_H_WAND_FIRE');
insert into rift_item_weapon_type (name) values ('ONE_H_WAND_WATER');
insert into rift_item_weapon_type (name) values ('ONE_H_WAND_EARTH');
insert into rift_item_weapon_type (name) values ('ONE_H_WAND_DEATH_EPIC');
insert into rift_item_weapon_type (name) values ('ONE_H_WAND_DEATH_EPIC_A');
insert into rift_item_weapon_type (name) values ('TWO_H_RANGED_GUN');
insert into rift_item_weapon_type (name) values ('TWO_H_RANGED_BOW');
insert into rift_item_weapon_type (name) values ('TWO_H_SWORD');
insert into rift_item_weapon_type (name) values ('TWO_H_HAMMER');
insert into rift_item_weapon_type (name) values ('TWO_H_STAFF');
insert into rift_item_weapon_type (name) values ('TWO_H_AXE');
insert into rift_item_weapon_type (name) values ('TWO_H_POLEARM');


CREATE TABLE rift_item_multilanguage_text (
pk_multilanguage_text_id INT NOT NULL PRIMARY KEY ,
english VARCHAR( 200 ) NULL ,
french VARCHAR( 200 ) NULL ,
german VARCHAR( 200 ) NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

CREATE TABLE rift_item_list (
pk_list_id INT NOT NULL PRIMARY KEY ,
fk_group_id INT NOT NULL ,
fk_enum_id INT NOT NULL ,
fk_item_id INT NOT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

CREATE TABLE rift_item_on_equip (
pk_on_equip_id INT NOT NULL PRIMARY KEY ,
fk_ml_ability INT NULL ,
intelligence INT NULL ,
wisdom INT NULL ,
endurance INT NULL ,
dexterity INT NULL ,
strength INT NULL ,
attackPower INT NULL ,
weaponParry INT NULL ,
shieldBlock INT NULL ,
spellCriticalHit INT NULL ,
criticalHit INT NULL ,
dodge INT NULL ,
spellFocus INT NULL ,
spellPower INT NULL ,
hitBonus INT NULL ,
resistanceFire INT NULL ,
resistanceLife INT NULL ,
resistanceWater INT NULL ,
resistanceAir INT NULL ,
resistanceDeath INT NULL ,
resistanceEarth INT NULL ,
toughness INT NULL ,
valor INT NULL ,
blockPercent INT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

CREATE TABLE rift_item_on_use (
pk_on_use_id INT NOT NULL PRIMARY KEY ,
fk_ml_ability INT NULL ,
fk_ml_tooltip INT NULL ,
required_item_level INT NULL ,
fk_l_rune_allowed_slot INT NULL ,
fk_l_rune_allowed_weapon_type INT NULL
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;

CREATE TABLE rift_item_item (
pk_item_id INT NOT NULL PRIMARY KEY ,
item_key VARCHAR( 50 ) NOT NULL ,
fk_ml_name INT NOT NULL ,
max_stack_size INT NULL ,
fk_ml_description INT NULL ,
fk_ml_flavor_text INT NULL ,
value INT NULL ,
fk_rarity_id INT NULL ,
fk_soulbound_trigger_id INT NULL ,
icon VARCHAR( 50 ) NULL ,
consumable INT(1) NULL ,
fk_on_use_id INT NULL ,
fk_rift_gem_id INT NULL ,
is_quest_item INT(1) NULL ,
fk_slot_id INT NULL ,
fk_armor_type_id INT NULL ,
armor INT NULL ,
fk_weapon_type_id INT NULL ,
minimum_damage INT NULL ,
maximum_damage INT NULL ,
speed DOUBLE NULL ,
weapon_range DOUBLE NULL ,
spell_damage INT NULL ,
fk_on_equip_id INT NULL ,
fk_apply_id INT NULL,
cooldown DOUBLE NULL ,
required_level INT NULL ,
required_pvp_rank INT NULL ,
runebreak_skill_level INT NULL ,
required_secondary_skill VARCHAR ( 50 ) NULL ,
required_secondary_skill_level INT NULL ,
fk_damage_type_id INT NULL ,
required_faction VARCHAR ( 100 ) NULL ,
required_faction_level INT NULL ,
fk_collectible_id INT NULL ,
is_currency INT(1) NULL ,
container_slots INT NULL ,
charges INT NULL ,
required_guild_level INT NULL ,
greater_slots INT NULL ,
lesser_slots INT NULL ,
fk_salvage_skill_id INT NULL ,
salvage_skill_level INT NULL ,
fk_l_callings_id INT NULL ,
INDEX ( item_key ) 
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;
CREATE TABLE rift_item_apply (
  pk_apply_id INT NOT NULL,
  intelligence INT NULL,
  wisdom INT NULL,
  endurance INT NULL,
  dexterity INT NULL,
  strength INT NULL,
  attackPower INT NULL,
  weaponParry INT NULL,
  shieldBlock INT NULL,
  spellCriticalHit INT NULL,
  criticalHit INT NULL,
  dodge INT NULL,
  spellFocus INT NULL,
  hitBonus INT NULL,
  spellPower INT NULL,
  resistanceFire INT NULL,
  resistanceLife INT NULL,
  resistanceWater INT NULL,
  resistanceAir INT NULL,
  resistanceDeath INT NULL,
  resistanceEarth INT NULL,
  toughness INT NULL,
  valor INT NULL,
  blockPercent INT NULL,
  movementSpeedMultiplierUnmounted INT NULL,
  movementSpeedMultiplierMounted INT NULL,
  weaponDamage INT NULL,
  mageManaPointsMax INT NULL,
  resistancePhysical INT NULL,
  weaponDPS INT NULL,
  PRIMARY KEY  (pk_apply_id)
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_bin;