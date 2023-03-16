package ch.correvon.rift.parsing.item;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.helper.ListHelper;
import ch.correvon.rift.parsing.item.enumeration.ArmorType;
import ch.correvon.rift.parsing.item.enumeration.Calling;
import ch.correvon.rift.parsing.item.enumeration.Collectible;
import ch.correvon.rift.parsing.item.enumeration.DamageType;
import ch.correvon.rift.parsing.item.enumeration.Rarity;
import ch.correvon.rift.parsing.item.enumeration.RiftGem;
import ch.correvon.rift.parsing.item.enumeration.SalvageSkill;
import ch.correvon.rift.parsing.item.enumeration.Slot;
import ch.correvon.rift.parsing.item.enumeration.SoulboundTrigger;
import ch.correvon.rift.parsing.item.enumeration.WeaponType;
import ch.correvon.rift.parsing.item.subObject.Apply;
import ch.correvon.rift.parsing.item.subObject.OnEquip;
import ch.correvon.rift.parsing.item.subObject.OnUse;
import ch.correvon.rift.parsing.subObject.MultiLanguageText;

public class Item
{
	public Item()
	{
		this.pk_id = PK_ID;
		PK_ID++;
	}
	
	public int getPk_id()
	{
		return this.pk_id;
	}
	public String getItemKey()
	{
		return itemKey;
	}
	public void setItemKey(String itemKey)
	{
		this.itemKey = itemKey;
	}
	public MultiLanguageText getName()
	{
		return name;
	}
	public void setName(MultiLanguageText name)
	{
		this.name = name;
	}
	public Integer getMaxStackSize()
	{
		return maxStackSize;
	}
	public void setMaxStackSize(Integer maxStackSize)
	{
		this.maxStackSize = maxStackSize;
	}
	public MultiLanguageText getDescription()
	{
		return description;
	}
	public void setDescription(MultiLanguageText description)
	{
		this.description = description;
	}
	public MultiLanguageText getFlavorText()
	{
		return flavorText;
	}
	public void setFlavorText(MultiLanguageText flavorText)
	{
		this.flavorText = flavorText;
	}
	public Integer getValue()
	{
		return value;
	}
	public void setValue(Integer value)
	{
		this.value = value;
	}
	public Rarity getRarity()
	{
		return rarity;
	}
	public void setRarity(Rarity rarity)
	{
		this.rarity = rarity;
	}
	public SoulboundTrigger isSoulboundTrigger()
	{
		return soulboundTrigger;
	}
	public void setSoulboundTrigger(SoulboundTrigger soulboundTrigger)
	{
		this.soulboundTrigger = soulboundTrigger;
	}
	public String getIcon()
	{
		return icon;
	}
	public void setIcon(String icon)
	{
		this.icon = icon;
	}
	public boolean isConsumable()
	{
		return consumable;
	}
	public void setConsumable(boolean consumable)
	{
		this.consumable = consumable;
	}
	public OnUse getOnUse()
	{
		return onUse;
	}
	public void setOnUse(OnUse onUse)
	{
		this.onUse = onUse;
	}
	public RiftGem getRiftGem()
	{
		return riftGem;
	}
	public void setRiftGem(RiftGem riftGem)
	{
		this.riftGem = riftGem;
	}
	public boolean isQuestItem()
	{
		return isQuestItem;
	}
	public void setQuestItem(boolean isQuestItem)
	{
		this.isQuestItem = isQuestItem;
	}
	public Slot getSlot()
	{
		return slot;
	}
	public void setSlot(Slot slot)
	{
		this.slot = slot;
	}
	public ArmorType getArmorType()
	{
		return armorType;
	}
	public void setArmorType(ArmorType armorType)
	{
		this.armorType = armorType;
	}
	public Integer getArmor()
	{
		return armor;
	}
	public void setArmor(Integer armor)
	{
		this.armor = armor;
	}
	public WeaponType getWeaponType()
	{
		return weaponType;
	}
	public void setWeaponType(WeaponType weaponType)
	{
		this.weaponType = weaponType;
	}
	public Integer getMinimumDamage()
	{
		return minimumDamage;
	}
	public void setMinimumDamage(Integer minimumDamage)
	{
		this.minimumDamage = minimumDamage;
	}
	public Integer getMaximumDamage()
	{
		return maximumDamage;
	}
	public void setMaximumDamage(Integer maximumDamage)
	{
		this.maximumDamage = maximumDamage;
	}
	public double getSpeed()
	{
		return speed;
	}
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}
	public double getRange()
	{
		return range;
	}
	public void setRange(double range)
	{
		this.range = range;
	}
	public Integer getSpellDamage()
	{
		return spellDamage;
	}
	public void setSpellDamage(Integer spellDamage)
	{
		this.spellDamage = spellDamage;
	}
	public OnEquip getOnEquip()
	{
		return onEquip;
	}
	public void setOnEquip(OnEquip onEquip)
	{
		this.onEquip = onEquip;
	}
	public double getCooldown()
	{
		return cooldown;
	}
	public void setCooldown(double cooldown)
	{
		this.cooldown = cooldown;
	}
	public Integer getRequiredLevel()
	{
		return requiredLevel;
	}
	public void setRequiredLevel(Integer requiredLevel)
	{
		this.requiredLevel = requiredLevel;
	}
	public Integer getRunebreakSkillLevel()
	{
		return runebreakSkillLevel;
	}
	public void setRunebreakSkillLevel(Integer runebreakSkillLevel)
	{
		this.runebreakSkillLevel = runebreakSkillLevel;
	}
	
	public List<Calling> getRequiredCallings()
	{
		return requiredCallings;
	}
	public void setRequiredCallings(List<Calling> requiredCallings)
	{
		this.requiredCallings = requiredCallings;
	}
	public void addCalling(Calling requiredCallings)
	{
		if(this.requiredCallings == null)
			this.requiredCallings = new ArrayList<Calling>(3);
		this.requiredCallings.add(requiredCallings);
	}
	public String getRequiredSecondarySkill()
	{
		return requiredSecondarySkill;
	}
	public void setRequiredSecondarySkill(String requiredSecondarySkill)
	{
		this.requiredSecondarySkill = requiredSecondarySkill;
	}
	public Integer getRequiredSecondarySkillLevel()
	{
		return requiredSecondarySkillLevel;
	}
	public void setRequiredSecondarySkillLevel(Integer requiredSecondarySkillLevel)
	{
		this.requiredSecondarySkillLevel = requiredSecondarySkillLevel;
	}
	public SoulboundTrigger getSoulboundTrigger()
	{
		return soulboundTrigger;
	}
	public DamageType getDamageType()
	{
		return damageType;
	}
	public void setDamageType(DamageType damageType)
	{
		this.damageType = damageType;
	}
	public Integer getRequiredFactionLevel()
	{
		return requiredFactionLevel;
	}
	public void setRequiredFactionLevel(Integer requiredFactionLevel)
	{
		this.requiredFactionLevel = requiredFactionLevel;
	}
	public String getRequiredFaction()
	{
		return requiredFaction;
	}
	public void setRequiredFaction(String requiredFaction)
	{
		this.requiredFaction = requiredFaction;
	}
	public Integer getRequiredPvPRank()
	{
		return requiredPvPRank;
	}
	public void setRequiredPvPRank(Integer requiredPvPRank)
	{
		this.requiredPvPRank = requiredPvPRank;
	}
	public Collectible getCollectible()
	{
		return collectible;
	}
	public void setCollectible(Collectible collectible)
	{
		this.collectible = collectible;
	}
	public boolean isCurrency()
	{
		return isCurrency;
	}
	public void setCurrency(boolean isCurrency)
	{
		this.isCurrency = isCurrency;
	}
	public Integer getContainerSlots()
	{
		return containerSlots;
	}
	public void setContainerSlots(Integer containerSlots)
	{
		this.containerSlots = containerSlots;
	}
	public Integer getCharges()
	{
		return charges;
	}
	public void setCharges(Integer charges)
	{
		this.charges = charges;
	}
	public Integer getRequiredGuildLevel()
	{
		return requiredGuildLevel;
	}
	public void setRequiredGuildLevel(Integer requiredGuildLevel)
	{
		this.requiredGuildLevel = requiredGuildLevel;
	}
	public Integer getGreaterSlots()
	{
		return greaterSlots;
	}
	public void setGreaterSlots(Integer greaterSlots)
	{
		this.greaterSlots = greaterSlots;
	}
	public Integer getLesserSlots()
	{
		return lesserSlots;
	}
	public void setLesserSlots(Integer lesserSlots)
	{
		this.lesserSlots = lesserSlots;
	}
	public SalvageSkill getSalvageSkill()
	{
		return salvageSkill;
	}
	public void setSalvageSkill(SalvageSkill salvageSkill)
	{
		this.salvageSkill = salvageSkill;
	}
	public Integer getSalvageSkillLevel()
	{
		return salvageSkillLevel;
	}
	public void setSalvageSkillLevel(Integer salvageSkillLevel)
	{
		this.salvageSkillLevel = salvageSkillLevel;
	}
	public boolean isAccountBound()
	{
		return accountBound;
	}
	public void setAccountBound(boolean accountBound)
	{
		this.accountBound = accountBound;
	}
	public Apply getApply()
	{
		return apply;
	}
	public void setApply(Apply apply)
	{
		this.apply = apply;
	}

	@Override public String toString()
	{
		return 
			(this.itemKey != null ? " - "+itemKey+"\n" : "") +  
			(this.name != null ? " - "+name+"\n" : "") +  
			(this.maxStackSize != -1 ? " - "+maxStackSize+"\n" : "") +  
			(this.description != null ? " - "+description+"\n" : "") +  
			(this.flavorText != null ? " - "+flavorText+"\n" : "") +  
			(this.value != -1 ? " - "+value+"\n" : "") +  
			(this.rarity != null ? " - "+rarity+"\n" : "") +  
			(this.soulboundTrigger != null ? " - "+soulboundTrigger+"\n" : "") +  
			(this.accountBound != false ? " - "+accountBound+"\n" : "") +  
			(this.icon != null ? " - "+icon+"\n" : "") +  
			(this.consumable) +  
			(this.onUse != null ? " - "+onUse+"\n" : "") +  
			(this.riftGem != null ? " - "+riftGem+"\n" : "") +  
			(this.isQuestItem) +  
			(this.slot != null ? " - "+slot+"\n" : "") +  
			(this.armorType != null ? " - "+armorType+"\n" : "") +  
			(this.armor != -1 ? " - "+armor+"\n" : "") +  
			(this.weaponType != null ? " - "+weaponType+"\n" : "") +  
			(this.minimumDamage != -1 ? " - "+minimumDamage+"\n" : "") +  
			(this.maximumDamage != -1 ? " - "+maximumDamage+"\n" : "") +  
			(this.speed >= 0 ? " - "+speed+"\n" : "") +  
			(this.range >= 0 ? " - "+range+"\n" : "") +  
			(this.spellDamage != -1 ? " - "+spellDamage+"\n" : "") +  
			(this.onEquip != null ? " - "+onEquip+"\n" : "") +  
			(this.cooldown >= 0 ? " - "+cooldown+"\n" : "") +  
			(this.requiredLevel != -1 ? " - "+requiredLevel+"\n" : "") +  
			(this.requiredPvPRank != -1 ? " - "+requiredPvPRank+"\n" : "") +  
			(this.runebreakSkillLevel != -1 ? " - "+runebreakSkillLevel+"\n" : "") +  
			(this.requiredSecondarySkill != null ? " - "+requiredSecondarySkill+"\n" : "") +  
			(this.requiredSecondarySkillLevel != -1 ? " - "+requiredSecondarySkillLevel+"\n" : "") +  
			(this.damageType != null ? " - "+damageType+"\n" : "") +  
			(this.requiredFaction != null ? " - "+requiredFaction+"\n" : "") +  
			(this.requiredFactionLevel != -1 ? " - "+requiredFactionLevel+"\n" : "") +  
			(this.collectible != null ? " - "+collectible+"\n" : "") +  
			(this.isCurrency) +  
			(this.requiredCallings != null ? " - "+ListHelper.printList(requiredCallings)+"\n" : "") +  
			(this.containerSlots != -1 ? " - "+containerSlots+"\n" : "") +  
			(this.charges != -1 ? " - "+charges+"\n" : "") +  
			(this.requiredGuildLevel != -1 ? " - "+requiredGuildLevel+"\n" : "") +  
			(this.greaterSlots != -1 ? " - "+greaterSlots+"\n" : "") +  
			(this.lesserSlots != -1 ? " - "+lesserSlots+"\n" : "") +  
			(this.salvageSkill != null ? " - "+salvageSkill+"\n" : "") +  
			(this.salvageSkillLevel != -1 ? " - "+salvageSkillLevel+"\n" : "") +
			(this.apply != null ? " - "+apply+"\n" : "");
	}

	private static int PK_ID = 0;
	
	private int pk_id;
	private String itemKey;
	private MultiLanguageText name;
	private Integer maxStackSize;
	private MultiLanguageText description;
	private MultiLanguageText flavorText;
	private Integer value;
	private Rarity rarity;
	private SoulboundTrigger soulboundTrigger;
	private String icon;
	private boolean consumable;
	private OnUse onUse;
	private RiftGem riftGem;
	private boolean isQuestItem;
	private Slot slot;
	private ArmorType armorType;
	private Integer armor;
	private WeaponType weaponType;
	private Integer minimumDamage;
	private Integer maximumDamage;
	private double speed;
	private double range;
	private Integer spellDamage;
	private OnEquip onEquip;
	private double cooldown;
	private Integer requiredLevel;
	private Integer requiredPvPRank;
	private Integer runebreakSkillLevel;
	private String requiredSecondarySkill;
	private Integer requiredSecondarySkillLevel;
	private DamageType damageType;
	private String requiredFaction; // TODO faire un MyEnum ?
	private Integer requiredFactionLevel;
	private Collectible collectible;
	private boolean isCurrency;
	private List<Calling> requiredCallings;
	private Integer containerSlots;
	private Integer charges;
	private Integer requiredGuildLevel;
	private Integer greaterSlots;
	private Integer lesserSlots;
	private SalvageSkill salvageSkill;
	private Integer salvageSkillLevel;
	private boolean accountBound;
	private Apply apply;
}
