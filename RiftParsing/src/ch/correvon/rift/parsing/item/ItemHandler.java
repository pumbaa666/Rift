package ch.correvon.rift.parsing.item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.correvon.rift.helper.ListHelper;
import ch.correvon.rift.parsing.item.enumeration.ArmorType;
import ch.correvon.rift.parsing.item.enumeration.Calling;
import ch.correvon.rift.parsing.item.enumeration.Collectible;
import ch.correvon.rift.parsing.item.enumeration.DamageType;
import ch.correvon.rift.parsing.item.enumeration.Rarity;
import ch.correvon.rift.parsing.item.enumeration.RiftGem;
import ch.correvon.rift.parsing.item.enumeration.RuneAllowedSlots;
import ch.correvon.rift.parsing.item.enumeration.SalvageSkill;
import ch.correvon.rift.parsing.item.enumeration.Slot;
import ch.correvon.rift.parsing.item.enumeration.SoulboundTrigger;
import ch.correvon.rift.parsing.item.enumeration.WeaponType;
import ch.correvon.rift.parsing.item.subObject.Apply;
import ch.correvon.rift.parsing.item.subObject.OnEquip;
import ch.correvon.rift.parsing.item.subObject.OnUse;
import ch.correvon.rift.parsing.subObject.ListElement;
import ch.correvon.rift.parsing.subObject.MultiLanguageText;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Tuto, voir http://java.developpez.com/faq/xml/?page=sax#parserSax
 * @author lco
 *
 */
public class ItemHandler extends DefaultHandler
{
	public ItemHandler()
	{
		super();
		this.item = null;
		this.multiLanguageText = null;
		this.onEquip = null;
		this.onUse = null;
		
		this.count = 0;
		MultiLanguageText.resetId();
		ListElement.resetId();
	}

	// Détection d'ouverture de balise
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	throws SAXException
	{
		/*if(debugBreak)
			System.out.println(qName);*/

		this.isReady = false;
		this.buffer = new StringBuffer();

		if(qName.equals("Items"))
			this.itemList = new LinkedList<Item>();
		else if(qName.equals("Item"))
			this.item = new Item();
		else if(qName.equals("ItemKey"))
			this.inItemKey = true;
		else if(qName.equals("Name"))
		{
			if(!this.inName)
			{
				this.inName = true;
				this.multiLanguageText = new MultiLanguageText();
			}
		}
		else if(qName.equals("Description"))
		{
			this.inDescription = true;
			this.multiLanguageText = new MultiLanguageText();
		}
		else if(qName.equals("FlavorText"))
		{
			this.inFlavorText = true;
			this.multiLanguageText = new MultiLanguageText();
		}
		else if(qName.equals("English"))
			this.inEnglish = true;
		else if(qName.equals("French"))
			this.inFrench = true;
		else if(qName.equals("German"))
			this.inGerman = true;
		else if(qName.equals("Value"))
			this.inValue = true;
		else if(qName.equals("MaxStackSize"))
			this.inMaxStackSize = true;
		else if(qName.equals("Rarity"))
			this.inRarity = true;
		else if(qName.equals("SoulboundTrigger"))
			this.inSoulboundTrigger = true;
		else if(qName.equals("AccountBound"))
			this.inAccountBound = true;
		else if(qName.equals("Icon"))
			this.inIcon = true;
		else if(qName.equals("OnUse"))
		{
			this.onUse = new OnUse();
			this.inOnUse = true;
		}
		else if(qName.equals("Tooltip"))
		{
			this.multiLanguageText = new MultiLanguageText();
			this.inTooltip = true;
		}
		else if(qName.equals("RequiredItemLevel"))
			this.inRequiredItemLevel = true;
		else if(qName.equals("RuneAllowedSlots"))
			this.inRuneAllowedSlots = true;
		else if(qName.equals("RuneAllowedWeaponType"))
			this.inRuneAllowedWeaponType = true;
		else if(qName.equals("ItemType"))
			this.inItemType = true;
		else if(qName.equals("RiftGem"))
			this.inRiftGem = true;
		else if(qName.equals("Slot"))
			this.inSlot = true;
		else if(qName.equals("ArmorType"))
			this.inArmorType = true;
		else if(qName.equals("Armor"))
			this.inArmor = true;
		else if(qName.equals("WeaponType"))
			this.inWeaponType = true;
		else if(qName.equals("MinimumDamage"))
			this.inMinimumDamage = true;
		else if(qName.equals("MaximumDamage"))
			this.inMaximumDamage = true;
		else if(qName.equals("Speed"))
			this.inSpeed = true;
		else if(qName.equals("Range"))
			this.inRange = true;
		else if(qName.equals("SpellDamage"))
			this.inSpellDamage = true;
		else if(qName.equals("Consumable"))
			this.inConsumable = true;
		else if(qName.equals("IsCurrency"))
			this.inIsCurrency = true;
		else if(qName.equals("GiveCurrency"))
			this.inGiveCurrency = true;
		else if(qName.equals("TemplateId") && this.inGiveCurrency)
		{
			// osef...
		}
		else if(qName.equals("Amount") && this.inGiveCurrency)
		{
			// osef...
		}
		else if(qName.equals("IsQuestItem"))
			this.inIsQuestItem = true;
		else if(qName.equals("OnEquip"))
		{
			this.onEquip = new OnEquip();
			this.inOnEquip = true;
		}
		else if(qName.equals("Ability"))
		{
			this.inAbility = true;
			this.multiLanguageText = new MultiLanguageText();
		}
		else if(qName.equals("Intelligence"))
			this.inIntelligence = true;
		else if(qName.equals("Wisdom"))
			this.inWisdom = true;
		else if(qName.equals("Endurance"))
			this.inEndurance = true;
		else if(qName.equals("Dexterity"))
			this.inDexterity = true;
		else if(qName.equals("Strength"))
			this.inStrength = true;
		else if(qName.equals("AttackPower"))
			this.inAttackPower = true;
		else if(qName.equals("WeaponParry"))
			this.inWeaponParry = true;
		else if(qName.equals("ShieldBlock"))
			this.inShieldBlock = true;
		else if(qName.equals("SpellCriticalHit"))
			this.inSpellCriticalHit = true;
		else if(qName.equals("CriticalHit"))
			this.inCriticalHit = true;
		else if(qName.equals("Dodge"))
			this.inDodge = true;
		else if(qName.equals("SpellFocus"))
			this.inSpellFocus = true;
		else if(qName.equals("HitBonus"))
			this.inHitBonus = true;
		else if(qName.equals("SpellPower"))
			this.inSpellPower = true;
		else if(qName.equals("ResistanceFire"))
			this.inResistanceFire = true;
		else if(qName.equals("ResistanceLife"))
			this.inResistanceLife = true;
		else if(qName.equals("ResistanceWater"))
			this.inResistanceWater = true;
		else if(qName.equals("ResistanceAir"))
			this.inResistanceAir = true;
		else if(qName.equals("ResistanceDeath"))
			this.inResistanceDeath = true;
		else if(qName.equals("ResistanceEarth"))
			this.inResistanceEarth = true;
		else if(qName.equals("Toughness"))
			this.inToughness = true;
		else if(qName.equals("Valor"))
			this.inValor = true;
		else if(qName.equals("BlockPercent"))
			this.inBlockPercent = true;
		else if(qName.equals("Cooldown"))
			this.inCooldown = true;
		else if(qName.equals("RequiredLevel"))
			this.inRequiredLevel = true;
		else if(qName.equals("RequiredPvPRank"))
			this.inRequiredPvPRank = true;
		else if(qName.equals("RunebreakSkillLevel"))
			this.inRunebreakSkillLevel = true;
		else if(qName.equals("RequiredSecondarySkill"))
			this.inRequiredSecondarySkill = true;
		else if(qName.equals("RequiredSecondarySkillLevel"))
			this.inRequiredSecondarySkillLevel = true;
		else if(qName.equals("RequiredFaction"))
			this.inRequiredFaction = true;
		else if(qName.equals("RequiredFactionLevel"))
			this.inRequiredFactionLevel = true;
		else if(qName.equals("DamageType"))
			this.inDamageType = true;
		else if(qName.equals("Collectible"))
			this.inCollectible = true;
		else if(qName.equals("RequiredCallings"))
			this.inRequiredCallings = true;
		else if(qName.equals("Calling"))
			this.inCalling = true;
		else if(qName.equals("ContainerSlots"))
			this.inContainerSlots = true;
		else if(qName.equals("Charges"))
			this.inCharges = true;
		else if(qName.equals("RequiredGuildLevel"))
			this.inRequiredGuildLevel = true;
		else if(qName.equals("RiftVessel"))
			this.inRiftVessel = true;
		else if(qName.equals("GreaterSlots"))
			this.inGreaterSlots = true;
		else if(qName.equals("LesserSlots"))
			this.inLesserSlots = true;
		else if(qName.equals("SalvageSkill"))
			this.inSalvageSkill = true;
		else if(qName.equals("SalvageSkillLevel"))
			this.inSalvageSkillLevel = true;
		else if(qName.equals("FirstLootedBy"))
			this.inFirstLootedBy = true;
		else if(qName.equals("IsAugmented"))
			this.inIsAugmented = true;
		else if(qName.equals("Apply"))
		{
			this.apply = new Apply();
			this.inApply = true;
		}
		else if(qName.equals("MovementSpeedMultiplierUnmounted"))
			this.inMovementSpeedMultiplierUnmounted = true;
		else if(qName.equals("MovementSpeedMultiplierMounted"))
			this.inMovementSpeedMultiplierMounted = true;
		else if(qName.equals("WeaponDamage"))
			this.inWeaponDamage = true;
		else if(qName.equals("MageManaPointsMax"))
			this.inMageManaPointsMax = true;
		else if(qName.equals("ResistancePhysical"))
			this.inResistancePhysical = true;
		else if(qName.equals("WeaponDPS"))
			this.inWeaponDPS = true;
		else
			if(!this.inFirstLootedBy)
				throw new SAXException("Balise " + qName + " inconnue.");
	}

	// Détection fin de balise
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException
	{
		String valueZZZ = this.getBuffer();
		/*if(qName.equals("Live_EU_Tahkaat"))
			debugBreak = true;
		if(debugBreak)
			System.out.println(qName + " : " + valueZZZ);*/
		if(qName.equals("Items"))
		{
			// Nothing to do
		}
		else if(qName.equals("Item"))
		{
			this.itemList.add(this.item);
			this.count++;
			if(this.count % 500 == 0)
			{
				double percent = (this.count / ((double)NB_ITEM)) * 100;
				String percentStr = new String(""+percent).substring(0,4);
				System.out.println(percentStr + "%");
			}
		}
		else if(qName.equals("ItemKey"))
		{
			this.item.setItemKey(this.getBuffer());
			this.inItemKey = false;
		}
		else if(qName.equals("Name"))
		{
			if(this.inName && !this.inFirstLootedBy)
			{
				this.item.setName(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
				//System.out.println(this.item.getName());
				this.inName = false;
				this.multiLanguageText = null;
			}
		}
		else if(qName.equals("Description"))
		{
			if(this.inDescription)
			{
				this.item.setDescription(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
				this.inDescription = false;
				this.multiLanguageText = null;
			}
		}
		else if(qName.equals("FlavorText"))
		{
			if(this.inFlavorText)
			{
				this.item.setFlavorText(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
				this.inFlavorText = false;
				this.multiLanguageText = null;
			}
		}
		else if(qName.equals("English"))
		{
			this.multiLanguageText.setEnglish(this.getBuffer());
			this.inEnglish = false;
		}
		else if(qName.equals("French"))
		{
			this.multiLanguageText.setFrench(this.getBuffer());
			this.inFrench = false;
		}
		else if(qName.equals("German"))
		{
			this.multiLanguageText.setGerman(this.getBuffer());
			this.inGerman = false;
		}
		else if(qName.equals("Value"))
		{
			this.item.setValue(new Integer(this.getBuffer()));
			this.inValue = false;
		}
		else if(qName.equals("MaxStackSize"))
		{
			this.item.setMaxStackSize(new Integer(this.getBuffer()));
			this.inMaxStackSize = false;
		}
		else if(qName.equals("Rarity"))
		{
			this.item.setRarity((Rarity)Rarity.getEnum(this.getBuffer()));
			this.inRarity = false;
		}
		else if(qName.equals("SoulboundTrigger"))
		{
			this.item.setSoulboundTrigger((SoulboundTrigger)SoulboundTrigger.getEnum(this.getBuffer()));
			this.inSoulboundTrigger = false;
		}
		else if(qName.equals("AccountBound"))
		{
			this.item.setAccountBound(true);
			this.inAccountBound = false;
		}
		else if(qName.equals("Icon"))
		{
			this.item.setIcon(this.getBuffer());
			this.inIcon = false;
		}
		else if(qName.equals("Consumable"))
		{
			this.item.setConsumable(true);
			this.inConsumable = false;
		}
		else if(qName.equals("IsCurrency"))
		{
			this.item.setCurrency(true);
			this.inIsCurrency = false;
		}
		else if(qName.equals("GiveCurrency"))
		{
			this.inGiveCurrency = false;
			// osef...
		}
		else if(qName.equals("TemplateId"))
		{
			// osef...
		}
		else if(qName.equals("Amount"))
		{
			// osef...
		}
		else if(qName.equals("IsQuestItem"))
		{
			this.item.setQuestItem(true);
			this.inIsQuestItem = false;
		}
		else if(qName.equals("OnUse"))
		{
			this.item.setOnUse(this.onUse);
			this.inOnUse = false;
		}
		else if(qName.equals("Ability"))
		{
			if(this.inOnUse)
				this.onUse.setAbility(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
			else if(this.inOnEquip)
				this.onEquip.setAbility(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnUse ou inOnEquip avec Ability");
			this.inAbility = false;
		}
		else if(qName.equals("Tooltip"))
		{
			if(!this.inOnUse)
				throw new SAXException("Etat inconnu, devrait être dans onUse avec Tooltip");
			this.onUse.setTooltip(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
			this.inTooltip = false;
		}
		else if(qName.equals("RequiredItemLevel"))
		{
			if(!this.inOnUse)
				throw new SAXException("Etat inconnu, devrait être dans onUse avec RequiredItemLevel");
			this.onUse.setRequiredItemLevel(new Integer(this.getBuffer()));
			this.inRequiredItemLevel = false;
		}
		else if(qName.equals("RuneAllowedSlots"))
		{
			if(!this.inOnUse)
				throw new SAXException("Etat inconnu, devrait être dans onUse avec RuneAllowedSlots");
//			this.onUse.addRuneAllowedSlots((RuneAllowedSlots)RuneAllowedSlots.getEnum(this.getBuffer()));
			this.inRuneAllowedSlots = false;
		}
		else if(qName.equals("RiftGem"))
		{
			this.item.setRiftGem((RiftGem)RiftGem.getEnum(this.getBuffer()));
			this.inRiftGem = false;
		}
		else if(qName.equals("Slot"))
		{
			if(this.inRuneAllowedSlots)
			{
				if(!this.inOnUse)
					throw new SAXException("Etat inconnu, devrait être dans onUse avec RuneAllowedSlots et inRuneAllowedSlots");
				this.onUse.addRuneAllowedSlots((RuneAllowedSlots)RuneAllowedSlots.getEnum(this.getBuffer()));
			}
			else
			{
				this.item.setSlot((Slot)Slot.getEnum(this.getBuffer()));
				this.inSlot = false;
			}
		}
		else if(qName.equals("RuneAllowedWeaponType"))
		{
			if(!this.inOnUse)
				throw new SAXException("Etat inconnu, devrait être dans onUse avec RuneAllowedWeaponType");
//			this.onUse.addRuneAllowedWeaponType((WeaponType)WeaponType.getEnum(this.getBuffer()));
			this.inRuneAllowedWeaponType = false;
		}
		else if(qName.equals("ItemType"))
		{
			if(!this.inOnUse || !this.inRuneAllowedWeaponType)
				throw new SAXException("Etat inconnu, devrait être dans onUse et inRuneAllowedWeaponType avec ItemType");
			this.onUse.addRuneAllowedWeaponType((WeaponType)WeaponType.getEnum(this.getBuffer()));
			this.inItemType = false;
		}
		else if(qName.equals("ArmorType"))
		{
			this.item.setArmorType((ArmorType)ArmorType.getEnum(this.getBuffer()));
			this.inArmorType = false;
		}
		else if(qName.equals("Armor"))
		{
			this.item.setArmor(new Integer(this.getBuffer()));
			this.inArmor = false;
		}
		else if(qName.equals("WeaponType"))
		{
			this.item.setWeaponType((WeaponType)WeaponType.getEnum(this.getBuffer()));
			this.inWeaponType = false;
		}
		else if(qName.equals("MinimumDamage"))
		{
			this.item.setMinimumDamage(new Integer(this.getBuffer()));
			this.inMinimumDamage = false;
		}
		else if(qName.equals("MaximumDamage"))
		{
			this.item.setMaximumDamage(new Integer(this.getBuffer()));
			this.inMaximumDamage = false;
		}
		else if(qName.equals("Speed"))
		{
			this.item.setSpeed(new Double(this.getBuffer()));
			this.inSpeed = false;
		}
		else if(qName.equals("Range"))
		{
			this.item.setRange(new Double(this.getBuffer()));
			this.inRange = false;
		}
		else if(qName.equals("SpellDamage"))
		{
			this.item.setSpellDamage(new Integer(this.getBuffer()));
			this.inSpellDamage = false;
		}
		else if(qName.equals("OnEquip"))
		{
			this.item.setOnEquip(this.onEquip);
			this.inOnEquip = false;
		}
		else if(qName.equals("Intelligence"))
		{
			if(this.inOnEquip)
				this.onEquip.setIntelligence(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setIntelligence(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec Intelligence");
			this.inIntelligence = false;
		}
		else if(qName.equals("Wisdom"))
		{
			if(this.inOnEquip)
				this.onEquip.setWisdom(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setWisdom(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec Wisdom");
			this.inWisdom = false;
		}
		else if(qName.equals("Endurance"))
		{
			if(this.inOnEquip)
				this.onEquip.setEndurance(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setEndurance(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec Endurance");
			this.inEndurance = false;
		}
		else if(qName.equals("Dexterity"))
		{
			if(this.inOnEquip)
				this.onEquip.setDexterity(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setDexterity(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec Dexterity");
			this.inDexterity = false;
		}
		else if(qName.equals("Strength"))
		{
			if(this.inOnEquip)
				this.onEquip.setStrength(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setStrength(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec Strength");
			this.inStrength = false;
		}
		else if(qName.equals("AttackPower"))
		{
			if(this.inOnEquip)
				this.onEquip.setAttackPower(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setAttackPower(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec AttackPower");
			this.inAttackPower = false;
		}
		else if(qName.equals("WeaponParry"))
		{
			if(this.inOnEquip)
				this.onEquip.setWeaponParry(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setWeaponParry(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec WeaponParry");
			this.inWeaponParry = false;
		}
		else if(qName.equals("ShieldBlock"))
		{
			if(this.inOnEquip)
				this.onEquip.setShieldBlock(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setShieldBlock(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec ShieldBlock");
			this.inShieldBlock = false;
		}
		else if(qName.equals("SpellCriticalHit"))
		{
			if(this.inOnEquip)
				this.onEquip.setSpellCriticalHit(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setSpellCriticalHit(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec SpellCriticalHit");
			this.inSpellCriticalHit = false;
		}
		else if(qName.equals("CriticalHit"))
		{
			if(this.inOnEquip)
				this.onEquip.setCriticalHit(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setCriticalHit(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec CriticalHit");
			this.inCriticalHit = false;
		}
		else if(qName.equals("Dodge"))
		{
			if(this.inOnEquip)
				this.onEquip.setDodge(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setDodge(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip avec Dodge");
			this.inDodge = false;
		}
		else if(qName.equals("SpellFocus"))
		{
			if(this.inOnEquip)
				this.onEquip.setSpellFocus(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setSpellFocus(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec SpellFocus");
			this.inSpellFocus = false;
		}
		else if(qName.equals("HitBonus"))
		{
			if(this.inOnEquip)
				this.onEquip.setHitBonus(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setHitBonus(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec HitBonus");
			this.inHitBonus = false;
		}
		else if(qName.equals("SpellPower"))
		{
			if(this.inOnEquip)
				this.onEquip.setSpellPower(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setSpellPower(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec SpellPower");
			this.inSpellPower = false;
		}
		else if(qName.equals("ResistanceFire"))
		{
			if(this.inOnEquip)
				this.onEquip.setResistanceFire(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setResistanceFire(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec ResistanceFire");
			this.inResistanceFire = false;
		}
		else if(qName.equals("ResistanceLife"))
		{
			if(this.inOnEquip)
				this.onEquip.setResistanceLife(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setResistanceLife(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec ResistanceLife");
			this.inResistanceLife = false;
		}
		else if(qName.equals("ResistanceWater"))
		{
			if(this.inOnEquip)
				this.onEquip.setResistanceWater(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setResistanceWater(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec ResistanceWater");
			this.inResistanceWater = false;
		}
		else if(qName.equals("ResistanceAir"))
		{
			if(this.inOnEquip)
				this.onEquip.setResistanceAir(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setResistanceAir(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec ResistanceAir");
			this.inResistanceAir = false;
		}
		else if(qName.equals("ResistanceDeath"))
		{
			if(this.inOnEquip)
				this.onEquip.setResistanceDeath(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setResistanceDeath(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec ResistanceDeath");
			this.inResistanceDeath = false;
		}
		else if(qName.equals("ResistanceEarth"))
		{
			if(this.inOnEquip)
				this.onEquip.setResistanceEarth(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setResistanceEarth(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec ResistanceEarth");
			this.inResistanceEarth = false;
		}
		else if(qName.equals("Toughness"))
		{
			if(this.inOnEquip)
				this.onEquip.setToughness(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setToughness(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec Toughness");
			this.inToughness = false;
		}
		else if(qName.equals("Valor"))
		{
			if(this.inOnEquip)
				this.onEquip.setValor(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setValor(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec Valor");
			this.inValor = false;
		}
		else if(qName.equals("BlockPercent"))
		{
			if(this.inOnEquip)
				this.onEquip.setBlockPercent(new Integer(this.getBuffer()));
			else if(this.inApply)
				this.apply.setBlockPercent(new Integer(this.getBuffer()));
			else
				throw new SAXException("Etat inconnu, devrait être dans inOnEquip ou inApply avec BlockPercent");
			this.inBlockPercent = false;
		}
		else if(qName.equals("MovementSpeedMultiplierUnmounted"))
		{
			if(!this.inApply)
				throw new SAXException("Etat inconnu, devrait être dans inApply avec MovementSpeedMultiplierUnmounted");
			this.apply.setMovementSpeedMultiplierUnmounted(new Integer(this.getBuffer()));
			this.inMovementSpeedMultiplierUnmounted = false;
		}
		else if(qName.equals("MovementSpeedMultiplierMounted"))
		{
			if(!this.inApply)
				throw new SAXException("Etat inconnu, devrait être dans inApply avec MovementSpeedMultiplierMounted");
			this.apply.setMovementSpeedMultiplierMounted(new Integer(this.getBuffer()));
			this.inMovementSpeedMultiplierMounted = false;
		}
		else if(qName.equals("WeaponDamage"))
		{
			if(!this.inApply)
				throw new SAXException("Etat inconnu, devrait être dans inApply avec WeaponDamage");
			this.apply.setWeaponDamage(new Integer(this.getBuffer()));
			this.inWeaponDamage = false;
		}
		else if(qName.equals("MageManaPointsMax"))
		{
			if(!this.inApply)
				throw new SAXException("Etat inconnu, devrait être dans inApply avec MageManaPointsMax");
			this.apply.setMageManaPointsMax(new Integer(this.getBuffer()));
			this.inMageManaPointsMax = false;
		}
		else if(qName.equals("ResistancePhysical"))
		{
			if(!this.inApply)
				throw new SAXException("Etat inconnu, devrait être dans inApply avec ResistancePhysical");
			this.apply.setResistancePhysical(new Integer(this.getBuffer()));
			this.inResistancePhysical = false;
		}
		else if(qName.equals("WeaponDPS"))
		{
			if(!this.inApply)
				throw new SAXException("Etat inconnu, devrait être dans inApply avec WeaponDPS");
			this.apply.setWeaponDPS(new Integer(this.getBuffer()));
			this.inWeaponDPS = false;
		}
		else if(qName.equals("Cooldown"))
		{
			this.item.setCooldown(new Double(this.getBuffer()));
			this.inCooldown = false;
		}
		else if(qName.equals("RequiredLevel"))
		{
			this.item.setRequiredLevel(new Integer(this.getBuffer()));
			this.inRequiredLevel = false;
		}
		else if(qName.equals("RequiredPvPRank"))
		{
			this.item.setRequiredLevel(new Integer(this.getBuffer()));
			this.inRequiredPvPRank = false;
		}
		else if(qName.equals("RunebreakSkillLevel"))
		{
			this.item.setRunebreakSkillLevel(new Integer(this.getBuffer()));
			this.inRunebreakSkillLevel = false;
		}
		else if(qName.equals("RequiredSecondarySkill"))
		{
			this.item.setRequiredSecondarySkill(this.getBuffer());
			this.inRequiredSecondarySkill = false;
		}
		else if(qName.equals("RequiredSecondarySkillLevel"))
		{
			this.item.setRequiredSecondarySkillLevel(new Integer(this.getBuffer()));
			this.inRequiredSecondarySkillLevel = false;
		}
		else if(qName.equals("RequiredFaction"))
		{
			this.item.setRequiredFaction(this.getBuffer());
			this.inRequiredFaction = false;
		}
		else if(qName.equals("RequiredFactionLevel"))
		{
			this.item.setRequiredFactionLevel(new Integer(this.getBuffer()));
			this.inRequiredFactionLevel = false;
		}
		else if(qName.equals("DamageType"))
		{
			this.item.setDamageType((DamageType)DamageType.getEnum(this.getBuffer()));
			this.inDamageType = false;
		}
		else if(qName.equals("Collectible"))
		{
			this.item.setCollectible((Collectible)Collectible.getEnum(this.getBuffer()));
			this.inCollectible = false;
		}
		else if(qName.equals("RequiredCallings"))
		{
//			this.item.addRequiredCallings((RequiredCallings)RequiredCallings.getEnum(this.getBuffer()));
			this.inRequiredCallings = false;
		}
		else if(qName.equals("Calling"))
		{
			this.item.addCalling((Calling)Calling.getEnum(this.getBuffer()));
			this.inCalling = false;
		}
		else if(qName.equals("ContainerSlots"))
		{
			this.item.setContainerSlots(new Integer(this.getBuffer()));
			this.inContainerSlots = false;
		}
		else if(qName.equals("Charges"))
		{
			this.item.setCharges(new Integer(this.getBuffer()));
			this.inCharges = false;
		}
		else if(qName.equals("RequiredGuildLevel"))
		{
			this.item.setRequiredGuildLevel(new Integer(this.getBuffer()));
			this.inRequiredGuildLevel = false;
		}
		else if(qName.equals("RiftVessel"))
		{
			this.inRiftVessel = false;
		}
		else if(qName.equals("GreaterSlots"))
		{
			this.item.setGreaterSlots(new Integer(this.getBuffer()));
			this.inGreaterSlots = false;
		}
		else if(qName.equals("LesserSlots"))
		{
			this.item.setLesserSlots(new Integer(this.getBuffer()));
			this.inLesserSlots = false;
		}
		else if(qName.equals("SalvageSkill"))
		{
			this.item.setSalvageSkill((SalvageSkill)SalvageSkill.getEnum(this.getBuffer()));
			this.inSalvageSkill = false;
		}
		else if(qName.equals("SalvageSkillLevel"))
		{
			this.item.setSalvageSkillLevel(new Integer(this.getBuffer()));
			this.inSalvageSkillLevel = false;
		}
		else if(qName.equals("FirstLootedBy"))
			this.inFirstLootedBy = false;
		else if(qName.equals("IsAugmented"))
			this.inIsAugmented = false;
		else if(qName.equals("Apply"))
		{
			this.item.setApply(this.apply);
			this.inApply = false;
		}
		else
			if(!this.inFirstLootedBy)
				throw new SAXException("Balise "+qName+" inconnue.");
	}

	// Détection de caractères
	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException
	{
		String lecture = new String(ch, start, length);
//		if(this.buffer != null)
			this.buffer.append(lecture);
//		System.out.println(lecture);
	}

	// Début du parsing
	@Override public void startDocument() throws SAXException
	{
		this.isReady = false;
		System.out.println("Début du parsing des items");
	}

	// Fin du parsing
	@Override public void endDocument() throws SAXException
	{
		System.out.println("Fin du parsing des items");
		this.isReady = true;
	}
	
	public List<Item> getItems() throws SAXException
	{
		if(!this.isReady)
			throw new SAXException("Le parsing n'est pas fini");
			
		return this.itemList;
	}
	
	private String getBuffer()
	{
//		if(this.buffer == null)
//			return null;
		return this.buffer.toString();
	}
	
	private List<Item> itemList; //résultats de notre parsing
	private Item item; // Objet en cours
	private StringBuffer buffer; // buffer nous permettant de récupérer les données
	
	private MultiLanguageText multiLanguageText;
	private List<MultiLanguageText> listML = new ArrayList<MultiLanguageText>(50000);
	private OnEquip onEquip;
	private OnUse onUse;
	private Apply apply;
	
	private boolean inItemKey;
	private boolean inName;
	private boolean inDescription;
	private boolean inFlavorText;
		private boolean inEnglish;
		private boolean inFrench;
		private boolean inGerman;
	private boolean inValue;
	private boolean inMaxStackSize;
	private boolean inRarity;
	private boolean inSoulboundTrigger;
	private boolean inAccountBound;
	private boolean inIcon;
	private boolean inConsumable;
	private boolean inIsCurrency;
	private boolean inGiveCurrency;
	private boolean inOnUse;
		private boolean inTooltip;
		private boolean inRequiredItemLevel;
		private boolean inRuneAllowedSlots;
		private boolean inRuneAllowedWeaponType;
		private boolean inItemType;
	private boolean inRiftGem;
	private boolean inIsQuestItem;
	private boolean inSlot;
	private boolean inArmorType;
	private boolean inArmor;
	private boolean inWeaponType;
	private boolean inMinimumDamage;
	private boolean inMaximumDamage;
	private boolean inSpeed;
	private boolean inRange;
	private boolean inSpellDamage;
	private boolean inOnEquip;
		private boolean inAbility;
		private boolean inIntelligence;
		private boolean inWisdom;
		private boolean inEndurance;
		private boolean inDexterity;
		private boolean inStrength;
		private boolean inAttackPower;
		private boolean inWeaponParry;
		private boolean inShieldBlock;
		private boolean inSpellCriticalHit;
		private boolean inCriticalHit;
		private boolean inDodge;
		private boolean inSpellFocus;
		private boolean inHitBonus;
		private boolean inSpellPower;
		private boolean inResistanceFire;
		private boolean inResistanceLife;
		private boolean inResistanceWater;
		private boolean inResistanceAir;
		private boolean inResistanceDeath;
		private boolean inResistanceEarth;
		private boolean inToughness;
		private boolean inValor;
		private boolean inBlockPercent;
	private boolean inCooldown;
	private boolean inRequiredLevel;
	private boolean inRequiredPvPRank;
	private boolean inRunebreakSkillLevel;
	private boolean inRequiredSecondarySkill;
	private boolean inRequiredSecondarySkillLevel;
	private boolean inRequiredFaction;
	private boolean inRequiredFactionLevel;
	private boolean inDamageType;
	private boolean inCollectible;
	private boolean inRequiredCallings;
	private boolean inCalling;
	private boolean inContainerSlots;
	private boolean inCharges;
	private boolean inRequiredGuildLevel;
	private boolean inRiftVessel;
	private boolean inGreaterSlots;
	private boolean inLesserSlots;
	private boolean inSalvageSkill;
	private boolean inSalvageSkillLevel;
	private boolean inIsAugmented;
	private boolean inApply;
	private boolean inFirstLootedBy;
	private boolean inMovementSpeedMultiplierUnmounted;
	private boolean inMovementSpeedMultiplierMounted;
	private boolean inWeaponDamage;
	private boolean inMageManaPointsMax;
	private boolean inResistancePhysical;
	private boolean inWeaponDPS;
	
	private boolean isReady;
	
	private boolean debugBreak = false;
	
	private int count;
	private static final int NB_ITEM = 46589;
//	private static final int PERCENT = NB_ITEM / 100;
}
