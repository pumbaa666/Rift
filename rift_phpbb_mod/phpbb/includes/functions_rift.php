<?php

function returnError($error)
{
	$resultTab = array();
	$resultTab[0] = utf8_encode($error);
	return $resultTab;
}

function getRecipeByName($itemName, $language = 'french')
{
	if(trim($itemName) == "")
		return returnError("<i>Veuillez spécifier un nom d'objet</i>");
	global $db;
	
	$str = "select pk_recipe_id, recipe_key, 
	(select ml.".escapeQuoteSql($language)." from rift_recipe_multilanguage_text ml where ml.pk_multilanguage_text_id = r.fk_ml_description limit 0,1) as description,
	iq.fk_item_key as item_key, iq.quantity as item_quantity, trainer_cost, rs.name as required_skill, 
	required_skill_point, high_until, medium_until, low_until,
	ir.name as rarity, fk_l_ingredients
	
	from rift_recipe_recipe r
	left join rift_recipe_item_quantity iq on iq.pk_item_quantity_id = r.fk_item_quantity
	left join rift_recipe_required_skill rs on rs.pk_required_skill_id = r.fk_required_skill
	left join rift_item_item ii on ii.item_key = iq.fk_item_key
	left join rift_item_rarity ir on ir.pk_rarity_id = ii.fk_rarity_id
	where r.fk_ml_name = 
	(
		select ml.pk_multilanguage_text_id
		from rift_recipe_multilanguage_text ml
		where (ml.".escapeQuoteSql($language)." = '".escapeQuoteSql($itemName)."')
		limit 0,1
	)
	limit 0,1;";
	
	$db->sql_query($str);
	$result = $db->sql_query($str);

	$row = $db->sql_fetchrow($result);
	if($row === null)
		return returnError("<i>recette " . utf8_decode(escapeQuote($itemName)) . " non trouvé</i>");
	
	$recipeId = $row['pk_recipe_id'];
	$itemNameInit = $itemName;
	$itemName = "Recette : " . $itemName;
	$itemName = formatRarity($itemName, $row['rarity']);
	$itemName = "<b>".$itemName."</b>";
	$itemNameOriginal = $itemName;
	$itemName = escapeQuote($itemName);

	$recipeResult = "<div id=recipe >";
	$recipeResult .= "<table class=riftTable width = 100%>";
		$recipeResult .= "<tr>";
			$recipeResult .= "<td class=riftTd colspan=2>".$itemName." (id ".$recipeId.")"."</td>";
		$recipeResult .= "</tr>";
		
		$recipeResult .= "<tr>";
			$recipeResult .= "<td class=riftTd>".escapeQuote(getFormatedSkill($row['required_skill']))."</td>";
			$recipeResult .= "<td class=riftTd>".$row['required_skill_point']."</td>";
		$recipeResult .= "</tr>";
		
		$recipeResult .= "<tr>";
			$recipeResult .= "<td class=riftTd colspan=2><center>".getFormatedCraftValue($row['required_skill_point'], $row['high_until'], $row['medium_until'], $row['low_until'])."</center></td>";
		$recipeResult .= "</tr>";
	
	$str = "select ml.".escapeQuoteSql($language)." as name, iq.quantity as quantity, ir.name as rarity, ii.icon
	from rift_recipe_recipe rr
	left join rift_recipe_list rl on rl.fk_group_id = rr.fk_l_ingredients
	left join rift_recipe_item_quantity iq on iq.pk_item_quantity_id = rl.fk_enum_id
	left join rift_item_item ii on ii.item_key = iq.fk_item_key
	left join rift_item_multilanguage_text ml on ml.pk_multilanguage_text_id = ii.fk_ml_name
	left join rift_item_rarity ir on ir.pk_rarity_id = ii.fk_rarity_id
	
	where rr.pk_recipe_id = " . $recipeId . ";";
	
	$db->sql_query($str);
	$result = $db->sql_query($str);

	while($rowIngredient = $db->sql_fetchrow($result))
	{
		$recipeResult .= "<tr>";
		$recipeResult .= 
			"<td class=riftTd>" . 
				"<table class=riftTable>" .
					"<tr>" . 
						"<td class=riftTd><img src = ./images/" . $rowIngredient['icon'] . ".png width = 20 height = 20/></td>" .
						"<td class=riftTd>" . escapeQuote(formatRarity($rowIngredient['name'], $rowIngredient['rarity'])) . "&nbsp;</td>" .
					"</tr>" .
				"</table>" .
			"</td>";
		$recipeResult .= "<td class=riftTd>" . $rowIngredient['quantity'] . "</td>";
		$recipeResult .= "</tr>";
	}

	if($row['description'] !== null)
	{
		$recipeResult .= "<tr>";
			$recipeResult .= "<td class=riftTd colspan=2>".formatTextWidth(escapeQuote($row['description']))."</td>";
		$recipeResult .= "</tr>";
	}

	$recipeResult .= "</table>";
	$recipeResult .= "</div>";
	$itemResult = getItemByName($itemNameInit, $language, $row['item_quantity']);
	$recipeResult .= $itemResult[0];

	$recipeResultTab[0] = $recipeResult;
	$recipeResultTab[1] = $itemNameOriginal;
	return $recipeResultTab;
}

function getFormatedCraftValue($requiredLvl, $high, $medium, $low)
{
	return
		"<font color = orange>" . $requiredLvl . "</font> - " .
		"<font color = yellow>" . $high . "</font> - " .
		"<font color = green>" . $medium . "</font> - " .
		"<font color = grey>" . $low . "</font>";
}

function getFormatedSkill($value)
{
	$ret = "";
	switch($value)
	{
		case "LIGHT_WEAPONSMITH": $ret = "Fabricant d'arme";break;
		case "OUTFITTER": $ret = "Couturier";break;
		case "RUNECRAFTER": $ret = "Fabricant de rune";break;
		case "ARMORSMITH": $ret = "Fabricant d'armure";break;
		case "ALCHEMIST": $ret = "Alchimiste";break;
		case "ARTIFICER": $ret = "Apothicaire";break;
		case "MINING": $ret = "Mineur";break;
		case "BUTCHERING": $ret = "Boucher";break;
		default: $ret = $value;
	}
	
	return utf8_encode($ret);
}
function getItemByName($itemName, $language = 'french', $quantity = null)
{
	if(trim($itemName) == "")
		return returnError("<i>Veuillez spécifier un nom d'objet</i>");
	global $db;

	$str = "select pk_item_id, fk_on_equip_id, fk_apply_id, max_stack_size,
	(select ml.".$language." from rift_item_multilanguage_text ml where ml.pk_multilanguage_text_id = i.fk_ml_description limit 0,1) as description,
	(select ml.".$language." from rift_item_multilanguage_text ml where ml.pk_multilanguage_text_id = i.fk_ml_flavor_text limit 0,1) as flavor_text,
	value, r.name as rarity, st.name as soulbound, icon, consumable,
	fk_on_use_id, rg.name as rift_gem, is_quest_item, s.name as slot, at.name as armor_type,
	wt.name as weapon_type, armor, minimum_damage, maximum_damage, speed, weapon_range, spell_damage,
	spell_damage, cooldown, required_level, required_pvp_rank, runebreak_skill_level, 
	required_secondary_skill, required_secondary_skill_level, required_faction, required_faction_level,
	fk_l_callings_id
		
	from rift_item_item i
	left join rift_item_rarity r on r.pk_rarity_id = i.fk_rarity_id
	left join rift_item_soulbound_trigger st on st.pk_soulbound_trigger_id = i.fk_soulbound_trigger_id
	left join rift_item_rift_gem rg on rg.pk_rift_gem_id = i.fk_rift_gem_id
	left join rift_item_slot s on s.pk_slot_id = i.fk_slot_id
	left join rift_item_armor_type at on at.pk_armor_type_id = i.fk_armor_type_id
	left join rift_item_weapon_type wt on wt.pk_weapon_type_id = i.fk_weapon_type_id
	
	where i.fk_ml_name = 
	(
		select ml.pk_multilanguage_text_id
		from rift_item_multilanguage_text ml
		where (ml.".escapeQuoteSql($language)." = '".escapeQuoteSql($itemName)."')
		limit 0,1
	)
	limit 0,1;";

	$db->sql_query($str);
	$result = $db->sql_query($str);

	$row = $db->sql_fetchrow($result);

	if($row === null)
		return returnError("<i>item " . utf8_decode(escapeQuote($itemName)) . " non trouvé</i>");
	
	$itemId = $row['pk_item_id'];
	$itemName = formatRarity($itemName, $row['rarity']);
	$itemName = "<b>".$itemName."</b>";
	$itemNameOriginal = $itemName;
	$itemName = escapeQuote($itemName);

	$quantityStr = "";
	if($quantity !== null)
		$quantityStr = $quantity."x ";
		
	$itemResult = "<div id = item>";
	$itemResult .= "<table>";
		$itemResult .= "<tr>";
			$itemResult .= "<td valign=top>";
				$itemResult .= "<img src = ./images/".$row['icon'].".png />&nbsp;";
			$itemResult .= "</td>";

			$itemResult .= "<td>";
				$itemResult .= "<table class=riftTable>";
				$itemResult .= "<tr>";
				$itemResult .= "<td class=riftTd colspan=2>".$quantityStr.$itemName." (id ".$itemId.")"."</td>";
				$itemResult .= "</tr>";

				$itemResult .= "<tr>";

				$itemResult .= "<tr>";
				if($row['consumable'] == 1)
					$itemResult .= "<td class=riftTd>Consommable</td>";
				else
					$itemResult .= "<td class=riftTd></td>";

				if($row['max_stack_size'] !== null)
					$itemResult .= "<td class=riftTd>S\'empile par ".$row['max_stack_size']."</td>";
				else
					$itemResult .= "<td class=riftTd></td>";
				$itemResult .= "</tr>";
				
				if($row['soulbound'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd colspan = 2>".getFormatedSoulbound($row['soulbound'])."</td>";
					$itemResult .= "</tr>";
				}
				
				if($row['armor_type'] !== null && $row['slot'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd>".getFormatedSlot($row['slot'])."</td>";
					$itemResult .= "<td class=riftTd>".getFormatedArmorType($row['armor_type'])."</td>";
					$itemResult .= "</tr>";
				}
				
				if($row['weapon_type'] !== null && $row['slot'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd>".getFormatedSlot($row['slot'])."</td>";
					$itemResult .= "<td class=riftTd>".getFormatedWeaponType($row['weapon_type'])."</td>";
					$itemResult .= "</tr>";
				}
				
				if($row['minimum_damage'] !== null && $row['maximum_damage'] !== null && $row['speed'] !== null)
				{
					$dps = ($row['maximum_damage'] + $row['minimum_damage']) / (2* $row['speed']);
					$dps = substr($dps, 0, strpos($dps, ".") + 2);
					$itemResult .= "<tr>";
						$itemResult .= "<td class=riftTd colspan=2>" . $row['minimum_damage'] . "-" . $row['maximum_damage'] . utf8_encode(" dégâts toutes les ") . $row['speed'] . " sec (" . $dps . " dps)</td>";
						
						//$itemResult .= "<td class=riftTd>" . $row['minimum_damage'] . "-" . $row['maximum_damage'] . utf8_encode(" dégâts toutes les ") . $row['speed'] . " sec</td>";
						//$itemResult .= "<td class=riftTd>" . $dps . " dps</td>";
						
						//$itemResult .= "<td class=riftTd>" . $dps . " dps (" . $row['minimum_damage'] . " - " . $row['maximum_damage'] . ")</td>";
						//$itemResult .= "<td class=riftTd>" . $row['speed'] . " sec</td>";
					$itemResult .= "</tr>";
				}

				if($row['spell_damage'] !== null)
				{
					$damageType = "";
					if($row['fk_damage_type_id'] !== null)
						$damageType = " (".$row['fk_damage_type_id'].")";
					$itemResult .= "<tr>";
					$itemResult .= utf8_encode("<td class=riftTd>Dégâts des sorts").$damageType."</td>";
					$itemResult .= "<td class=riftTd>".$row['spell_damage']."</td>";
					$itemResult .= "</tr>";
				}

				if($row['cooldown'] !== null && $row['cooldown'] >= 0.001)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd>Cooldown</td>";
					$itemResult .= "<td class=riftTd>".$row['cooldown']." secondes</td>";
					$itemResult .= "</tr>";
				}
				
				if($row['required_pvp_rank'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd>Niveau PVP requis</td>";
					$itemResult .= "<td class=riftTd>".$row['required_pvp_rank']."</td>";
					$itemResult .= "</tr>";
				}

				if($row['required_secondary_skill'] !== null && $row['required_secondary_skill_level'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= utf8_encode("<td class=riftTd>Nécessite").$row['required_secondary_skill']."</td>";
					$itemResult .= "<td class=riftTd>(".$row['required_secondary_skill_level'].")</td>";
					$itemResult .= "</tr>";
				}
				
				if($row['required_faction'] !== null && $row['required_faction_level'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= utf8_encode("<td class=riftTd>Nécessite d\'être ").getRequiredFaction($row['required_faction_level'])."</td>";
					$itemResult .= "<td class=riftTd>chez ".escapeQuote($row['required_faction'])."</td>";
					$itemResult .= "</tr>";
				}
				
				if($row['rift_gem'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd>Gemme</td>";
					$itemResult .= "<td class=riftTd>".getFormatedRiftGem($row['rift_gem'])."</td>";
					$itemResult .= "</tr>";
				}
				
				if($row['is_quest_item'] == 1)
				{
					$itemResult .= "<tr>";
					$itemResult .= utf8_encode("<td class=riftTd colspan = 2>Objet de quête</td>");
					$itemResult .= "</tr>";
				}
				
				if($row['description'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd colspan=2>".formatTextWidth(escapeQuote($row['description']))."</td>";
					$itemResult .= "</tr>";
				}

				if($row['flavor_text'] !== null)
				{
					$itemResult .= hr($itemResult);
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd colspan=2>".formatTextWidth(escapeQuote($row['flavor_text']))."</td>";
					$itemResult .= "</tr>";
				}

				$itemResult .= hr($itemResult);
				
				if($row['armor'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd>Armure :</td>";
					$itemResult .= "<td class=riftTd>".$row['armor']."</td>";
					$itemResult .= "</tr>";
				}
				
				$itemResult = addOnEquip($itemResult, $row['fk_on_equip_id'], $language);
				$itemResult = addApply($itemResult, $row['fk_apply_id'], $language);
				$itemResult = addOnUse($itemResult, $row['fk_on_use_id'], $language);
				$itemResult .= hr($itemResult);

				if($row['required_level'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd colspan=2>Niveau " . $row['required_level'] . " requis</td>";
					$itemResult .= "</tr>";
				}
				
				$itemResult .= getCalling($row['fk_l_callings_id']);
				
				if($row['runebreak_skill_level'] !== null)
				{
					$itemResult .= "<tr>";
					$itemResult .= "<td class=riftTd colspan=2><font color=green>Exploitable par brise-rune (".$row['runebreak_skill_level'].")</font></td>";
					$itemResult .= "</tr>";
				}

				$itemResult .= "<tr>";
					$itemResult .= "<td></td>";
					$itemResult .= "<td class=riftTd>" . getFormatedValue($row['value']) . "</td>";
				$itemResult .= "</tr>";
				
				$itemResult .= "</table>";
			$itemResult .= "</td>";
		$itemResult .= "</tr>";
	$itemResult .= "</table>";
	$itemResult .= "</div>";
	
	$resultTab = array();
	$resultTab[0] = $itemResult;
	$resultTab[1] = $itemNameOriginal;
	return $resultTab;
}

function getRequiredFaction($value)
{
	$ret = "";
	switch($value)
	{
		case 1: $ret = "Neutre";break; // 3k
		case 2: $ret = "Ami";break; // 10k
		case 3: $ret = "Décoré";break; // 20k
		case 4: $ret = "Éstimé";break; // 35k
		case 5: $ret = "Révéré";break; // 60k
		case 6: $ret = "Glorifié";break; // ?k
	}
	return utf8_encode($ret);
}

function getCalling($fk_l_calling)
{
	if($fk_l_calling === null)
		return "";
	
	global $db;
	$str = "select ic.name
			from rift_item_calling ic
			left join rift_item_list il on ic.pk_calling_id = il.fk_enum_id
			where il.fk_group_id = " . $fk_l_calling . ";";
			
	$db->sql_query($str);
	$result = $db->sql_query($str);

	$res = "";
	while($row = $db->sql_fetchrow($result))
		$res .= getFormatedCalling($row['name']) . ", ";
	$res = substr($res, 0, strlen($res) - 2);
	if($res == "")
		return "";
		
	$itemResult = "<tr>";
		$itemResult .= "<td class=riftTd>Vocation :</td>";
		$itemResult .= "<td class=riftTd>" . $res . "</td>";
	$itemResult .= "</tr>";
	
	return $itemResult;
}

function getFormatedCalling($value)
{
	$ret = "";
	switch($value)
	{
		case "ROGUE": $ret = "Voleur";break;
		case "CLERIC": $ret = "Clerc";break;
		case "MAGE": $ret = "Mage";break;
		case "WARRIOR": $ret = "Guerrier";break;
		default : $ret = $value;break;
	}
	return utf8_encode($ret);
}

function hr($itemResult)
{
	if(strpos(substr($itemResult, -50), "/images/my_pictures/hr") !== false)
		return "";
		
	$itemResult = "<tr>";
		$itemResult .= "<td class=riftTd colspan=2><center><img src = ./images/my_pictures/hr.png /></center></td>";
	$itemResult .= "</tr>";
	
	return $itemResult;
}

function getJSFunction($result, $name)
{
	return "<div id=\"curseur\" class=\"infobulle\"></div><a href=\"javascript:;\" onmouseover=\"montre('".$result."');\" onmouseout=\"cache();\" onclick=\"pin();\" >".$name."</a>"; 
}

function addOnEquip($itemResult, $on_equip_id, $language = 'french')
{
	global $db;

	if($on_equip_id === null)
		return $itemResult;
		
	$str = "select oe.*, ml.".$language." as ability
	from rift_item_on_equip oe
	left join rift_item_multilanguage_text ml on ml.pk_multilanguage_text_id = oe.fk_ml_ability
	where pk_on_equip_id = '".$on_equip_id."';";
	
	$db->sql_query($str);
	$result = $db->sql_query($sql);

	$row = $db->sql_fetchrow($result);
	if($row === null)
		return $itemResult;
	
	//$itemResult .= hr($itemResult);
	$itemResult .= "<tr>";

	if($row['strength'] !== null)
		$itemResult .= "<tr><td class=riftTd>Force</td><td class=riftTd>+" . $row['strength'] . "</td></tr>";
	if($row['dexterity'] !== null)
		$itemResult .= utf8_encode("<tr><td class=riftTd>Dexterité</td><td class=riftTd>+") . $row['dexterity'] . "</td></tr>";
	if($row['endurance'] !== null)
		$itemResult .= "<tr><td class=riftTd>Endurance</td><td class=riftTd>+" . $row['endurance'] . "</td></tr>";
	if($row['attackPower'] !== null)
		$itemResult .= "<tr><td class=riftTd>Puissance d\'attaque</td><td class=riftTd>+" . $row['attackPower'] . "</td></tr>";
	if($row['hitBonus'] !== null)
		$itemResult .= utf8_encode("<tr><td class=riftTd>Précision</td><td class=riftTd>+") . $row['hitBonus'] . "</td></tr>";
	if($row['criticalHit'] !== null)
		$itemResult .= "<tr><td class=riftTd>Critique</td><td class=riftTd>+" . $row['criticalHit'] . "</td></tr>"; // vérifier
	if($row['dodge'] !== null)
		$itemResult .= "<tr><td class=riftTd>Esquive</td><td class=riftTd>+" . $row['dodge'] . "</td></tr>";
	if($row['weaponParry'] !== null)
		$itemResult .= "<tr><td class=riftTd>Parade</td><td class=riftTd>+" . $row['weaponParry'] . "</td></tr>";
	if($row['shieldBlock'] !== null)
		$itemResult .= "<tr><td class=riftTd>Blocage</td><td class=riftTd>+" . $row['shieldBlock'] . "</td></tr>";
	if($row['intelligence'] !== null)
		$itemResult .= "<tr><td class=riftTd>Intelligence</td><td class=riftTd>+" . $row['intelligence'] . "</td></tr>";
	if($row['wisdom'] !== null)
		$itemResult .= "<tr><td class=riftTd>Sagesse</td><td class=riftTd>+" . $row['wisdom'] . "</td></tr>";
	if($row['spellCriticalHit'] !== null)
		$itemResult .= "<tr><td class=riftTd>Sort critique</td><td class=riftTd>+" . $row['spellCriticalHit'] . "</td></tr>"; // vérifier
	if($row['spellFocus'] !== null)
		$itemResult .= "<tr><td class=riftTd>Concentration</td><td class=riftTd>+" . $row['spellFocus'] . "</td></tr>"; // vérifier
	if($row['spellPower'] !== null)
		$itemResult .= "<tr><td class=riftTd>Puissance des sorts</td><td class=riftTd>+" . $row['spellPower'] . "</td></tr>";
	if($row['toughness'] !== null)
		$itemResult .= "<tr><td class=riftTd>Robustesse</td><td class=riftTd>+" . $row['toughness'] . "</td></tr>";
	if($row['valor'] !== null)
		$itemResult .= "<tr><td class=riftTd>Valeur</td><td class=riftTd>+" . $row['valor'] . "</td></tr>"; // vérifier
	if($row['blockPercent'] !== null)
		$itemResult .= "<tr><td class=riftTd>Blocage </td><td class=riftTd>+" . $row['blockPercent'] . "%</td></tr>"; // vérifier
	if($row['resistanceFire'] !== null)
		$itemResult .= utf8_encode("<tr><td class=riftTd>Résistance au feu</td><td class=riftTd>+") . $row['resistanceFire'] . "</td></tr>";
	if($row['resistanceLife'] !== null)
		$itemResult .= utf8_encode("<tr><td class=riftTd>Résistance à la vie</td><td class=riftTd>+") . $row['resistanceLife'] . "</td></tr>";
	if($row['resistanceWater'] !== null)
		$itemResult .= utf8_encode("<tr><td class=riftTd>Résistance à l'eau</td><td class=riftTd>+") . $row['resistanceWater'] . "</td></tr>";
	if($row['resistanceAir'] !== null)
		$itemResult .= utf8_encode("<tr><td class=riftTd>Résistance à l'air</td><td class=riftTd>+") . $row['resistanceAir'] . "</td></tr>";
	if($row['resistanceDeath'] !== null)
		$itemResult .= utf8_encode("<tr><td class=riftTd>Résistance à la mort</td><td class=riftTd>+") . $row['resistanceDeath'] . "</td></tr>";
	if($row['resistanceEarth'] !== null)
		$itemResult .= utf8_encode("<tr><td class=riftTd>Résistance à la terre</td><td class=riftTd>+") . $row['resistanceEarth'] . "</td></tr>";
	if($row['ability'] !== null)
	{
		$itemResult .= hr($itemResult);
		$itemResult .= "<tr><td class=riftTd colspan=2><font color = green>" . utf8_encode("Équipé : ") . formatTextWidth(escapeQuote($row['ability'])) . "</font></td></tr>";
	}

	$itemResult .= "</tr>";

	return $itemResult;
}

function addOnUse($itemResult, $on_use_id, $language = 'french')
{
	global $db;

	if($on_use_id === null)
		return $itemResult;
		
	$str = "select
	(select ml.".$language." from rift_item_multilanguage_text ml where ml.pk_multilanguage_text_id = ou.fk_ml_ability) as ability,
	(select ml.".$language." from rift_item_multilanguage_text ml where ml.pk_multilanguage_text_id = ou.fk_ml_tooltip) as tooltip,
	required_item_level, fk_l_rune_allowed_slot, fk_l_rune_allowed_weapon_type
	from rift_item_on_use ou
	where pk_on_use_id = ".$on_use_id.";";
	
	$db->sql_query($str);
	$result = $db->sql_query($sql);

	$row = $db->sql_fetchrow($result);
	if($row === null)
		return $itemResult;
	
	$itemResult .= hr($itemResult);
	$itemResult .= "<tr>";

	if($row['ability'] !== null)
		$itemResult .= "<tr><td class=riftTd colspan = 2>Utilisation : " . escapeQuote(formatTextWidth($row['ability'])) . "</td></tr>";
	if($row['tooltip'] !== null)
		$itemResult .= "<tr><td class=riftTd colspan = 2><i>" . escapeQuote(removeDoubleQuote(formatTextWidth($row['tooltip']))) . "</i></td></tr>";
	
	$itemResult .= getRuneAllowedSlot($row['fk_l_rune_allowed_slot']);
	$itemResult .= getRuneAllowedWeapon($row['fk_l_rune_allowed_weapon_type']);

	if($row['required_item_level'] !== null)
		$itemResult .= "<tr><td class=riftTd colspan = 2>Niveau minimal de l\'objet : " . $row['required_item_level'] . "</td></tr>";

	$itemResult .= "</tr>";

	return $itemResult;

}

function addApply($itemResult, $apply_id, $language = 'french')
{
	global $db;

	if($apply_id === null)
		return $itemResult;
		
	$str = "select *	
	from rift_item_apply a
	where pk_apply_id = ".$apply_id.";";
	
	$db->sql_query($str);
	$result = $db->sql_query($sql);

	$row = $db->sql_fetchrow($result);
	if($row === null)
		return $itemResult;
	
	$itemResult .= hr($itemResult);
	$itemResult .= "<tr>";

	if($row['intelligence'] !== null)
		$itemResult .= "<tr><td class=riftTd>Intelligence</td><td class=riftTd>" . $row['intelligence'] . "</td></tr>";
	if($row['wisdom'] !== null)
		$itemResult .= "<tr><td class=riftTd>Sagesse</td><td class=riftTd>" . $row['wisdom'] . "</td></tr>";
	if($row['endurance'] !== null)
		$itemResult .= "<tr><td class=riftTd>Endurance</td><td class=riftTd>" . $row['endurance'] . "</td></tr>";
	if($row['dexterity'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Déxterité")."</td><td class=riftTd>" . $row['dexterity'] . "</td></tr>";
	if($row['strength'] !== null)
		$itemResult .= "<tr><td class=riftTd>Force</td><td class=riftTd>" . $row['strength'] . "</td></tr>";
	if($row['attackPower'] !== null)
		$itemResult .= "<tr><td class=riftTd>Puissance d\'attaque</td><td class=riftTd>" . $row['attackPower'] . "</td></tr>";
	if($row['weaponParry'] !== null)
		$itemResult .= "<tr><td class=riftTd>Parade</td><td class=riftTd>" . $row['weaponParry'] . "</td></tr>";
	if($row['shieldBlock'] !== null)
		$itemResult .= "<tr><td class=riftTd>Blocage</td><td class=riftTd>" . $row['shieldBlock'] . "</td></tr>";
	if($row['spellCriticalHit'] !== null)
		$itemResult .= "<tr><td class=riftTd>Sort critique</td><td class=riftTd>" . $row['spellCriticalHit'] . "</td></tr>";
	if($row['criticalHit'] !== null)
		$itemResult .= "<tr><td class=riftTd>Critique</td><td class=riftTd>" . $row['criticalHit'] . "</td></tr>";
	if($row['dodge'] !== null)
		$itemResult .= "<tr><td class=riftTd>Esquive</td><td class=riftTd>" . $row['dodge'] . "</td></tr>";
	if($row['spellFocus'] !== null)
		$itemResult .= "<tr><td class=riftTd>Concentration</td><td class=riftTd>" . $row['spellFocus'] . "</td></tr>"; // Vérifier
	if($row['hitBonus'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Touché") . "</td><td class=riftTd>" . $row['hitBonus'] . "</td></tr>";
	if($row['spellPower'] !== null)
		$itemResult .= "<tr><td class=riftTd>Puissance des sorts</td><td class=riftTd>" . $row['spellPower'] . "</td></tr>";
	if($row['resistanceFire'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Résistance au feu") . "</td><td class=riftTd>" . $row['resistanceFire'] . "</td></tr>";
	if($row['resistanceLife'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Résistance à la vie") . "</td><td class=riftTd>" . $row['resistanceLife'] . "</td></tr>";
	if($row['resistanceWater'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Résistance à l\'eau") . "</td><td class=riftTd>" . $row['resistanceWater'] . "</td></tr>";
	if($row['resistanceAir'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Résistance à l\'air") . "</td><td class=riftTd>" . $row['resistanceAir'] . "</td></tr>";
	if($row['resistanceDeath'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Résistance à la mort") . "</td><td class=riftTd>" . $row['resistanceDeath'] . "</td></tr>";
	if($row['resistanceEarth'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Résistance à la terre") . "</td><td class=riftTd>" . $row['resistanceEarth'] . "</td></tr>";
	if($row['toughness'] !== null)
		$itemResult .= "<tr><td class=riftTd>Robustesse</td><td class=riftTd>" . $row['toughness'] . "</td></tr>";
	if($row['valor'] !== null)
		$itemResult .= "<tr><td class=riftTd>Valeure</td><td class=riftTd>" . $row['valor'] . "</td></tr>";
	if($row['blockPercent'] !== null)
		$itemResult .= "<tr><td class=riftTd>Blocage</td><td class=riftTd>" . $row['blockPercent'] . "%</td></tr>";
	if($row['movementSpeedMultiplierUnmounted'] !== null)
		$itemResult .= "<tr><td class=riftTd>Augmentation de la vitesse à pied</td><td class=riftTd>" . $row['movementSpeedMultiplierUnmounted'] . "</td></tr>";
	if($row['movementSpeedMultiplierMounted'] !== null)
		$itemResult .= "<tr><td class=riftTd>Augmentation de la vitesse de la monture</td><td class=riftTd>" . $row['movementSpeedMultiplierMounted'] . "</td></tr>";
	if($row['weaponDamage'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Dégâts de l\'arme") . "</td><td class=riftTd>" . $row['weaponDamage'] . "</td></tr>";
	if($row['mageManaPointsMax'] !== null)
		$itemResult .= "<tr><td class=riftTd>Mana</td><td class=riftTd>" . $row['mageManaPointsMax'] . "</td></tr>";
	if($row['resistancePhysical'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("Résistance physique") . "</td><td class=riftTd>" . $row['resistancePhysical'] . "</td></tr>"; // Vérifier
	if($row['weaponDPS'] !== null)
		$itemResult .= "<tr><td class=riftTd>".utf8_encode("DPS de l\'arme") . "</td><td class=riftTd>" . $row['weaponDPS'] . "</td></tr>"; // Vérifier
	
	$itemResult .= "</tr>";

	return $itemResult;
}

function formatRarity($itemName, $rarity)
{
	switch($rarity)
	{
		case "TRASH": return "<font color = #888888>".$itemName."</font>";
		case "COMMON": return "<font color = #FFFFFF>".$itemName."</font>";
		case "UNCOMMON": return "<font color = #0FE006>".$itemName."</font>";
		case "RARE": return "<font color = #2040FA>".$itemName."</font>";
		case "EPIC": return "<font color = #D114AB>".$itemName."</font>";
		case "RELIC": return "<font color = #DD6309>".$itemName."</font>";
		case "QUEST_ITEM": return "<font color = #DBF523>".$itemName."</font>";
		default: return "<font color = #000000>".$itemName."</font>";
	}
}

function getRuneAllowedSlot($fk_l_rune)
{
	if($fk_l_rune === null)
		return "";

	$str = "select ras.name
	from rift_item_rune_allowed_slots ras
	left join rift_item_list il on il.fk_enum_id = ras.pk_rune_allowed_slots_id
	where il.fk_group_id = " . $fk_l_rune . ";";

	global $db;
	$db->sql_query($str);
	$result = $db->sql_query($sql);
	$everySlot = "";
	while($row = $db->sql_fetchrow($result))
		$everySlot .= getFormatedRuneAllowedSlot($row['name']) . ", ";
	$everySlot = substr($everySlot, 0, strlen($everySlot) - 2);
	return "<tr><td class=riftTd>Emplacement</td><td class=riftTd>" . $everySlot . "</td></tr>";
}

function getRuneAllowedWeapon($fk_l_rune)
{
	if($fk_l_rune === null)
		return "";

	$str = "select wt.name
	from rift_item_weapon_type wt
	left join rift_item_list il on il.fk_enum_id = wt.pk_weapon_type_id
	where il.fk_group_id = " . $fk_l_rune . ";";

	global $db;
	$db->sql_query($str);
	$result = $db->sql_query($sql);
	$everySlot = "";
	while($row = $db->sql_fetchrow($result))
		$everySlot .= getFormatedWeaponType($row['name']) . ", ";
	$everySlot = substr($everySlot, 0, strlen($everySlot) - 2);
	return "<tr><td class=riftTd>Emplacement</td><td class=riftTd>" . $everySlot . "</td></tr>";
}

function getFormatedRuneAllowedSlot($value)
{
	$ret = "";
	switch($value)
	{
		case "GLOVES": $ret = "Mains";break;
		case "WEAPON_2H": $ret = "A deux mains";break;
		case "FEET": $ret = "Pieds";break;
		case "CHEST": $ret = "Torse";break;
		case "SHOULDERS": $ret = "Épaules";break;
		case "PANTS": $ret = "Jambes";break; // Vérifier
		case "BELT": $ret = "Ceinture";break;
		case "LEGS": $ret = "Jambes";break; // Vérifier
		case "WEAPON_MAIN": $ret = "Main principale";break;
		case "HELMET": $ret = "Tête";break;
		case "WEAPON_RANGED": $ret = "A distance";break;
		case "WEAPON_OFF": $ret = "Main secondaire";break;
		default : $ret = $value;
	}
	return utf8_encode($ret);
}

function getFormatedSoulbound($value)
{
	$ret = "";
	switch($value)
	{
		case "BIND_ON_EQUIP": $ret = "Lié quand équipé";break;
		case "BIND_ON_PICKUP": $ret = "Lié quand ramassé";break;
		case "BIND_ON_USE": $ret = "Lié quand utilisé";break; // vérifier
		default : $ret = $value;
	}
	return utf8_encode($ret);
}

function getFormatedRiftGem($value)
{
	$ret = "";
	switch($value)
	{
		case "LESSER": $ret = "Inférieure";break;
		case "GREATER": $ret = "Supérieure";break;
		default: $ret = $value;break;
	}
	return utf8_encode($ret);
}

function getFormatedValue($value)
{
	$valueStr = "";
	$temp;
	if(!is_numeric($value))
		return $value;
		
	if($value == 0)
		return "Aucune valeur";
	if($value >= 10000)
	{
		$temp = intval($value / 10000);
		$valueStr = $temp . " <img src = ./images/my_pictures/money/pp.png /> ";
		$value = $value - $temp * 10000;
	}
	
	if($value >= 100)
	{
		$temp = intval($value / 100);
		$valueStr = $valueStr . $temp . " <img src = ./images/my_pictures/money/po.png /> ";
		$value = $value - $temp * 100;
	}
	else if($valueStr != "")
		$valueStr = $valueStr . " 0 <img src = ./images/my_pictures/money/po.png /> ";
	$valueStr = $valueStr . $value . " <img src = ./images/my_pictures/money/pa.png />";
	
	return $valueStr;
}

function getFormatedArmorType($value)
{
	$ret = "";
	switch($value)
	{
		case "CLOTH": $ret =  "Tissu";break; // Vérifier
		case "LEATHER": $ret =  "Cuir";break; // Vérifier
		case "CHAIN": $ret =  "Chaîne";break; // Vérifier
		case "PLATE": $ret = "Armure de plates";break;
		default: $ret = $value;break;
	}
	return utf8_encode($ret);
}

function getFormatedWeaponType($value)
{
	$ret = "";
	switch($value)
	{
		case "TOTEM": $ret = "Totem";break;
		case "SHIELD": $ret = "Bouclier";break;
		case "ONE_H_DAGGER": $ret = "Dague à une main";break;
		case "ONE_H_SWORD": $ret = "Épée à une main";break;
		case "ONE_H_MACE": $ret = "Masse à une main";break;
		case "ONE_H_AXE": $ret = "Hache à une main";break;
		case "ONE_H_WAND": $ret = "Baguette";break;
		case "ONE_H_WAND_AIR": $ret = "Baguette d'air";break;
		case "ONE_H_WAND_LIFE": $ret = "Baguette de vie";break;
		case "ONE_H_WAND_FIRE": $ret = "Baguette de feu";break;
		case "ONE_H_WAND_WATER": $ret = "Baguette d'eau";break;
		case "ONE_H_WAND_EARTH": $ret = "Baguette de terre";break;
		case "ONE_H_WAND_DEATH_EPIC": $ret = "Baguette de mort";break;
		case "ONE_H_WAND_DEATH_EPIC_A": $ret = "Baguette de mort";break;
		case "TWO_H_RANGED_GUN": $ret = "Arme à feu";break;
		case "TWO_H_RANGED_BOW": $ret = "Arc";break;
		case "TWO_H_SWORD": $ret = "Épée à deux mains";break;
		case "TWO_H_HAMMER": $ret = "Marteau à deux mains";break;
		case "TWO_H_STAFF": $ret = "Bâton à deux mains";break;
		case "TWO_H_AXE": $ret = "Hache à deux mains";break;
		case "TWO_H_POLEARM": $ret = "Arme d'hast";break;
		default: $ret = $value;
	}
	
	return utf8_encode($ret);
}

function getFormatedSlot($value)
{
	$ret = "";
	switch($value)
	{
		case "HELMET": $ret = "Tête";break;
		case "CHEST": $ret = "Torse";break;
		case "ONE_HAND": $ret = "Une main";break;
		case "NECK": $ret = "Cou";break;
		case "TWO_HANDED": $ret = "À deux mains";break; // Vérifier
		case "LEGS": $ret = "Jambes";break;
		case "FEET": $ret = "Pieds";break;
		case "WEAPON_RANGED": $ret = "À distance";break;
		case "BELT": $ret = "Taille";break;
		case "OFF_HAND": $ret = "Main secondaire";break;
		case "RING": $ret = "Anneau";break;
		case "TRINKET_2": $ret = "Totem";break;
		case "SHOULDERS": $ret = "Epaules";break;
		case "GLOVES": $ret = "Gants";break;
		case "MAIN_HAND": $ret = "Main principale";break;
		case "MOUNT": $ret = "Monture";break;
		case "WEAPON_OFF": $ret = "Weapon_off";break; // Vérifier
		case "RIFT_VESSEL": $ret = "Focus planaire";break;
		case "WEAPON_2H": $ret = "À deux mains";break; // Vérifier
		case "SYNERGY_CRYSTAL": $ret = "Crystal de synérgie";break;
		default: $ret = $value;
	}
	
	return utf8_encode($ret);
}

function escapeQuote($text)
{
	return preg_replace("/'/", "\'", $text);
}

function removeDoubleQuote($text)
{
	return preg_replace("/\"/", "", $text);
}

function escapeQuoteSql($text)
{
	return preg_replace("/'/", "''", $text);
}

function formatTextWidth($text)
{
	$NB_CHAR = 40;

	$textLength = strlen($text);
	if($textLength <= $NB_CHAR)
		return $text;
	$spacePos = strpos($text, " ", $NB_CHAR);
	if($spacePos === false)
		return $text;
	
	$lastSpacePos = 0;
	$textResult = "";
	$maxLength = strlen($text);
	
	$i = 0;
	$DEBUG = false;
	if($DEBUG)
		echo $text."<br><br>";
	
	while($spacePos !== false)
	{
		if($DEBUG)
		{
			echo "<br><b>".$i."</b><br>";
			echo "spacePos = ".$spacePos."<br>";
		}
		$textPart = substr($text, $lastSpacePos, $spacePos-$lastSpacePos);
		if($DEBUG)
			echo "textPart = ".$textPart."<br>";
		$textResult .= $textPart."<br/>";
		if($DEBUG)
			echo "textResult = ".$textResult."<br>";
		$lastSpacePos = $spacePos;
		if($DEBUG)
			echo "lastSpacePos = ".$lastSpacePos."<br>";
		if($lastSpacePos + $NB_CHAR > $maxLength)
		{
			$textResult .= substr($text, $lastSpacePos);
			break;
		}
		$spacePos = strpos($text, " ", $lastSpacePos + $NB_CHAR);
		if($spacePos === false)
		{
			$textResult .= substr($text, $lastSpacePos);
			break;
		}
		
		$i++;
		if($i > 100)
			break; // Garde-fou jusqu'à que ma fonction soit bien testée
	}
	if($DEBUG)
		echo "<br><br>FINAL :".$textResult;
	return $textResult;
}

function explodeRiftMessage($message, $split, $pFunction)
{
	$DEBUG = false;
	$baliseOpen = "[".$split."]";
	$baliseClose = "[/".$split."]";
	$baliseLength = strlen($split);
	$itemsTab = explode($baliseOpen, $message);
	$nbItems = count($itemsTab);
	if($nbItems > 1)
	{
		$newMessage = "";
		if($DEBUG)
			echo "<br><br>nbItems = ".$nbItems."<br><b>Message total : </b><br>" . printNoHTML($message) . "<br>---------------------<br>";
		for($count = 0; $count < $nbItems; $count++)
		{
			$language = "french";
			if($DEBUG)
				echo "count = ".$count."<br>";
			$item = $itemsTab[$count];
			$endPos = strpos($item, $baliseClose);
			if($DEBUG)
			{
				echo "item = ".printNoHTML($item)."<br>";
				echo "endPos = ".$endPos."<br>";
			}
			if($endPos <= 0)
			{
				if($DEBUG)
					echo "endPos <= 0 --> on ajoute item à newMessage"."<br>";
				$item = preg_replace("/\[\/".$split."\]/", "", $item);
				//$item = preg_replace("/=.*/", "", $item);
				//$item = preg_replace("/]/", "", $item);
				//$item = substr($item, 1);
				$newMessage .= $item;
				if($DEBUG)
					echo "newMessage = ".printNoHTML($newMessage)."<br>";
				continue;
			}
			/*if(strpos($item, '=') === 0)
			{
				if($DEBUG)
				{
					echo "item in language : ".printNoHTML($item)."<br>";
					echo "strpos : ".strpos($item, ']')."<br>";
				}
				$language = substr($item, 1, strpos($item, ']') - 1);
				$item = substr($item, strpos($item, ']') + 0);
				if($DEBUG)
					echo "item in language fin : ".printNoHTML($item)."<br>";
			}*/
			$endPos = strpos($item, $baliseClose);
			//$itemClean = substr($item, 1, $endPos - 1); // version avec prise en charge de la langue qui ne fonctionne pas très bien (notemment dans une citation)
			$itemClean = substr($item, 0, $endPos - 0);
			if($DEBUG)
			{
				echo "itemClean = ".printNoHTML($itemClean)."<br>";
				echo "strpos2 : ".strpos($item, '=')."<br>";
				echo "language = ".printNoHTML($language)."<br>";
			}

			$resultTab = $pFunction($itemClean, $language);
			if(count($resultTab) == 1) // Si il n'y a qu'une case, c'est que l'objet n'a pas été trouvé, alors pas de JS
				$newMessage .= $resultTab[0];
			else
				$newMessage .= getJSFunction($resultTab[0], $resultTab[1]);
			
			$endOfMessage = substr($item, $endPos+$baliseLength+3);
			if($DEBUG)
				echo "endOfMessage = ".$endOfMessage."<br>";
			$newMessage .= $endOfMessage;
			if($DEBUG)
				echo "re-newMessage = ".$newMessage."<br>";
		}
		$message = $newMessage;
		if($DEBUG)
			echo "<br><br><b>Message total après: </b><br>".$message;
	}
	return $message;
}

function printNoHTML($text)
{
	$text = preg_replace("/</", "&lt;", $text);
	$text = preg_replace("/>/", "&gt;", $text);
	
	return $text;
}

?>
