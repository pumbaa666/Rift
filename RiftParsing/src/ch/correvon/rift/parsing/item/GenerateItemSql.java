package ch.correvon.rift.parsing.item;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import ch.correvon.rift.helper.StringHelper;
import ch.correvon.rift.parsing.item.subObject.Apply;
import ch.correvon.rift.parsing.item.subObject.OnEquip;
import ch.correvon.rift.parsing.item.subObject.OnUse;
import ch.correvon.rift.parsing.subObject.A_MyEnum;
import ch.correvon.rift.parsing.subObject.GetId;
import ch.correvon.rift.parsing.subObject.ListElement;
import ch.correvon.rift.parsing.subObject.MultiLanguageText;

public class GenerateItemSql
{
	public static void generateSql(List<Item> listItem, String fileName)
	{
		try
		{
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("TRUNCATE TABLE rift_item_multilanguage_text;\n");
			out.write("TRUNCATE TABLE rift_item_on_equip;\n");
			out.write("TRUNCATE TABLE rift_item_apply;\n");
			out.write("TRUNCATE TABLE rift_item_on_use;\n");
			out.write("TRUNCATE TABLE rift_item_list;\n");
			out.write("TRUNCATE TABLE rift_item_item;\n");
			for(Item item:listItem)
				out.write(generateSql(item) + "\n");
			out.close();
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		
		System.out.println("Fichier " + fileName + " créé, " + listItem.size() + " items parsés");
	}
	
	public static String generateSql(Item item)
	{
		StringBuilder sb = new StringBuilder(1000000);

		Integer fkOnUseId = getOnUse(item, sb);
		Integer fkOnEquipId = getOnEquip(item, sb);
		Integer fkApply = getApply(item, sb);
		return generateItemSql(item, fkOnUseId, fkOnEquipId, fkApply, sb);
	}
	
	private static Integer getOnUse(Item item, StringBuilder sb)
	{
		OnUse onUse = item.getOnUse();
		if(onUse == null)
			return null;
		
		Integer pkOnUseId = onUse.getPk_id();
		Integer fkMLAbility = getMultilanguageID(onUse.getAbility(), sb);
		Integer fkMLTooltip = getMultilanguageID(onUse.getTooltip(), sb);
		Integer requiredItemLevel = onUse.getRequiredItemLevel();
		Integer fkRuneAllowedSlot = getList(onUse.getRuneAllowedSlots(), pkOnUseId, sb);
		Integer fkRuneAllowedWeaponType = getList(onUse.getRuneAllowedWeaponType(), pkOnUseId, sb);
		
		sb.append("insert into rift_item_on_use values (" + 
				StringHelper.quoteIfNotNull(pkOnUseId) + ", " + 
				StringHelper.quoteIfNotNull(fkMLAbility) + ", " + 
				StringHelper.quoteIfNotNull(fkMLTooltip) + ", " + 
				StringHelper.quoteIfNotNull(requiredItemLevel) + ", " +
				StringHelper.quoteIfNotNull(fkRuneAllowedSlot) + ", " +
				StringHelper.quoteIfNotNull(fkRuneAllowedWeaponType) + ");" + "\n");
		
		return pkOnUseId;
	}

	private static Integer getOnEquip(Item item, StringBuilder sb)
	{
		OnEquip onEquip = item.getOnEquip();
		if(onEquip == null)
			return null;
		
		int pkOnEquipId = onEquip.getPk_id();
		Integer fkMLAbility = getMultilanguageID(onEquip.getAbility(), sb);

		sb.append("insert into rift_item_on_equip values (" +
				StringHelper.quoteIfNotNull(pkOnEquipId) + ", " +
				StringHelper.quoteIfNotNull(fkMLAbility) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getIntelligence()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getWisdom()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getEndurance()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getDexterity()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getStrength()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getAttackPower()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getWeaponParry()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getShieldBlock()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getSpellCriticalHit()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getCriticalHit()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getDodge()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getSpellFocus()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getSpellPower()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getHitBonus()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getResistanceFire()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getResistanceLife()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getResistanceWater()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getResistanceAir()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getResistanceDeath()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getResistanceEarth()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getToughness()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getValor()) + ", " +
				StringHelper.quoteIfNotNull(onEquip.getBlockPercent()) + ");" + "\n");
		
		return pkOnEquipId;
	}
	
	private static Integer getApply(Item item, StringBuilder sb)
	{
		Apply apply = item.getApply();
		if(apply == null)
			return null;
		
		int pkApplyId = apply.getPk_id();

		sb.append("insert into rift_item_apply values (" +
				StringHelper.quoteIfNotNull(apply.getPk_id()) + ", " +
				StringHelper.quoteIfNotNull(apply.getIntelligence()) + ", " +
				StringHelper.quoteIfNotNull(apply.getWisdom()) + ", " +
				StringHelper.quoteIfNotNull(apply.getEndurance()) + ", " +
				StringHelper.quoteIfNotNull(apply.getDexterity()) + ", " +
				StringHelper.quoteIfNotNull(apply.getStrength()) + ", " +
				StringHelper.quoteIfNotNull(apply.getAttackPower()) + ", " +
				StringHelper.quoteIfNotNull(apply.getWeaponParry()) + ", " +
				StringHelper.quoteIfNotNull(apply.getShieldBlock()) + ", " +
				StringHelper.quoteIfNotNull(apply.getSpellCriticalHit()) + ", " +
				StringHelper.quoteIfNotNull(apply.getCriticalHit()) + ", " +
				StringHelper.quoteIfNotNull(apply.getDodge()) + ", " +
				StringHelper.quoteIfNotNull(apply.getSpellFocus()) + ", " +
				StringHelper.quoteIfNotNull(apply.getHitBonus()) + ", " +
				StringHelper.quoteIfNotNull(apply.getSpellPower()) + ", " +
				StringHelper.quoteIfNotNull(apply.getResistanceFire()) + ", " +
				StringHelper.quoteIfNotNull(apply.getResistanceLife()) + ", " +
				StringHelper.quoteIfNotNull(apply.getResistanceWater()) + ", " +
				StringHelper.quoteIfNotNull(apply.getResistanceAir()) + ", " +
				StringHelper.quoteIfNotNull(apply.getResistanceDeath()) + ", " +
				StringHelper.quoteIfNotNull(apply.getResistanceEarth()) + ", " +
				StringHelper.quoteIfNotNull(apply.getToughness()) + ", " +
				StringHelper.quoteIfNotNull(apply.getValor()) + ", " +
				StringHelper.quoteIfNotNull(apply.getBlockPercent()) + ", " +
				StringHelper.quoteIfNotNull(apply.getMovementSpeedMultiplierUnmounted()) + ", " +
				StringHelper.quoteIfNotNull(apply.getMovementSpeedMultiplierMounted()) + ", " +
				StringHelper.quoteIfNotNull(apply.getWeaponDamage()) + ", " +
				StringHelper.quoteIfNotNull(apply.getMageManaPointsMax()) + ", " +
				StringHelper.quoteIfNotNull(apply.getResistancePhysical()) + ", " +
				StringHelper.quoteIfNotNull(apply.getWeaponDPS()) + ");" + "\n");
		
		return pkApplyId;
	}
	
	private static String generateItemSql(Item item, Integer fkOnUseId, Integer fkOnEquipId, Integer fkApply, StringBuilder sb)
	{
		int pkItemId = item.getPk_id();
		
		Integer fkRequiredCallings = getList(item.getRequiredCallings(), pkItemId, sb);

		sb.append("insert into rift_item_item values (" +
				StringHelper.quoteIfNotNull(pkItemId) + ", " +
				StringHelper.quoteIfNotNull(StringHelper.escapeSql(item.getItemKey())) + ", " +
				StringHelper.quoteIfNotNull(getMultilanguageID(item.getName(), sb)) + ", " +
				StringHelper.quoteIfNotNull(item.getMaxStackSize()) + ", " +
				StringHelper.quoteIfNotNull(getMultilanguageID(item.getDescription(), sb)) + ", " +
				StringHelper.quoteIfNotNull(getMultilanguageID(item.getFlavorText(), sb)) + ", " +
				StringHelper.quoteIfNotNull(item.getValue()) + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getRarity())) + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getSoulboundTrigger())) + ", " +
				StringHelper.quoteIfNotNull(StringHelper.escapeSql(item.getIcon())) + ", " +
				StringHelper.quoteIfNotNull((item.isConsumable()) ? "1":"0") + ", " +
				StringHelper.quoteIfNotNull(fkOnUseId) + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getRiftGem())) + ", " +
				StringHelper.quoteIfNotNull((item.isQuestItem()) ? "1":"0") + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getSlot())) + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getArmorType())) + ", " +
				StringHelper.quoteIfNotNull(item.getArmor()) + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getWeaponType())) + ", " +
				StringHelper.quoteIfNotNull(item.getMinimumDamage()) + ", " +
				StringHelper.quoteIfNotNull(item.getMaximumDamage()) + ", " +
				StringHelper.quoteIfNotNull(item.getSpeed()) + ", " +
				StringHelper.quoteIfNotNull(item.getRange()) + ", " +
				StringHelper.quoteIfNotNull(item.getSpellDamage()) + ", " +
				StringHelper.quoteIfNotNull(fkOnEquipId) + ", " +
				StringHelper.quoteIfNotNull(fkApply) + ", " +
				StringHelper.quoteIfNotNull(item.getCooldown()) + ", " +
				StringHelper.quoteIfNotNull(item.getRequiredLevel()) + ", " +
				StringHelper.quoteIfNotNull(item.getRequiredPvPRank()) + ", " +
				StringHelper.quoteIfNotNull(item.getRunebreakSkillLevel()) + ", " +
				StringHelper.quoteIfNotNull(StringHelper.escapeSql(item.getRequiredSecondarySkill())) + ", " +
				StringHelper.quoteIfNotNull(item.getRequiredSecondarySkillLevel()) + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getDamageType())) + ", " +
				StringHelper.quoteIfNotNull(StringHelper.escapeSql(item.getRequiredFaction())) + ", " +
				StringHelper.quoteIfNotNull(item.getRequiredFactionLevel()) + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getCollectible())) + ", " +
				StringHelper.quoteIfNotNull((item.isCurrency()) ? "1":"0") + ", " +
				StringHelper.quoteIfNotNull(item.getContainerSlots()) + ", " +
				StringHelper.quoteIfNotNull(item.getCharges()) + ", " +
				StringHelper.quoteIfNotNull(item.getRequiredGuildLevel()) + ", " +
				StringHelper.quoteIfNotNull(item.getGreaterSlots()) + ", " +
				StringHelper.quoteIfNotNull(item.getLesserSlots()) + ", " +
				StringHelper.quoteIfNotNull(getEnumId(item.getSalvageSkill())) + ", " +
				StringHelper.quoteIfNotNull(item.getSalvageSkillLevel()) + ", " +
				StringHelper.quoteIfNotNull(fkRequiredCallings) + ");" + "\n");
		
		String strReturn = null;
		try
		{
			strReturn = new String(sb.toString().getBytes("UTF-8"));
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return strReturn;
	}
	
	private static Integer getMultilanguageID(MultiLanguageText ml, StringBuilder sb)
	{
		if(ml == null)
			return null;

		sb.append(ml.getSql("rift_item_multilanguage_text") + "\n");
		return ml.getPk_id();
	}
	
	private static Integer getList(List<? extends A_MyEnum> list, int objectId, StringBuilder sb)
	{
		if(list == null)
			return null;

		for(A_MyEnum myEnum:list)
		{
			ListElement listElement = new ListElement();
			listElement.setGroupId(groupListId);
			listElement.setEnumId(getEnumId(myEnum));
			listElement.setObjectId(objectId);
			
			sb.append(listElement.getSql("rift_item_list") + "\n");
		}
		groupListId++;
		return groupListId-1;
	}
	
	private static String getEnumId(GetId myEnum)
	{
		if(myEnum == null)
			return null;
		return myEnum.getId();
	}
	
	private static int groupListId = 0;
}
