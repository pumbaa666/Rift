package ch.correvon.rift.parsing.item.subObject;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.item.enumeration.RuneAllowedSlots;
import ch.correvon.rift.parsing.item.enumeration.WeaponType;
import ch.correvon.rift.parsing.subObject.MultiLanguageText;

public class OnUse
{
	public OnUse()
	{
		this.pk_id = PK_ID;
		PK_ID++;
		this.runeAllowedSlots = null;
	}

	public int getPk_id()
	{
		return this.pk_id;
	}
	public MultiLanguageText getTooltip()
	{
		return tooltip;
	}
	public void setTooltip(MultiLanguageText tooltip)
	{
		this.tooltip = tooltip;
	}
	public Integer getRequiredItemLevel()
	{
		return requiredItemLevel;
	}
	public void setRequiredItemLevel(Integer requiredItemLevel)
	{
		this.requiredItemLevel = requiredItemLevel;
	}
	public List<RuneAllowedSlots> getRuneAllowedSlots()
	{
		return runeAllowedSlots;
	}
	public void setRuneAllowedSlots(List<RuneAllowedSlots> runeAllowedSlots)
	{
		this.runeAllowedSlots = runeAllowedSlots;
	}

	public void addRuneAllowedSlots(RuneAllowedSlots runeAllowedSlots)
	{
		if(this.runeAllowedSlots == null)
			this.runeAllowedSlots = new ArrayList<RuneAllowedSlots>(10);
		this.runeAllowedSlots.add(runeAllowedSlots);
	}

	public MultiLanguageText getAbility()
	{
		return ability;
	}

	public void setAbility(MultiLanguageText ability)
	{
		this.ability = ability;
	}

	public List<WeaponType> getRuneAllowedWeaponType()
	{
		return runeAllowedWeaponType;
	}

	public void setRuneAllowedWeaponType(List<WeaponType> runeAllowedWeaponType)
	{
		this.runeAllowedWeaponType = runeAllowedWeaponType;
	}
	
	public void addRuneAllowedWeaponType(WeaponType runeAllowedWeaponType)
	{
		if(this.runeAllowedWeaponType == null)
			this.runeAllowedWeaponType = new ArrayList<WeaponType>(10);
		this.runeAllowedWeaponType.add(runeAllowedWeaponType);
	}

	@Override public String toString()
	{
		return
			this.ability + "\n" +
			this.tooltip + "\n" + 
			" - " + this.requiredItemLevel + "\n" +
			" - " + this.runeAllowedSlots + "\n"+  
			" - " + this.runeAllowedWeaponType;
	}
	
	private static int PK_ID = 0;
	
	private int pk_id;
	private MultiLanguageText ability;
	private MultiLanguageText tooltip;
	private Integer requiredItemLevel;
	private List<RuneAllowedSlots> runeAllowedSlots;
	private List<WeaponType> runeAllowedWeaponType;
}
