package ch.correvon.rift.parsing.item.subObject;

import ch.correvon.rift.parsing.subObject.MultiLanguageText;

public class OnEquip
{
	public OnEquip()
	{
		this.pk_id = PK_ID;
		PK_ID++;

		/*this.intelligence = -1;
		this.wisdom = -1;
		this.endurance = -1;
		this.dexterity = -1;
		this.strength = -1;
		this.attackPower = -1;
		this.weaponParry = -1;
		this.shieldBlock = -1;
		this.spellCriticalHit = -1;
		this.criticalHit = -1;
		this.dodge = -1;
		this.spellFocus = -1;
		this.spellPower = -1;
		this.hitBonus = -1;
		this.resistanceFire = -1;
		this.resistanceLife = -1;
		this.resistanceWater = -1;
		this.resistanceAir = -1;
		this.resistanceDeath = -1;
		this.resistanceEarth = -1;
		this.toughness = -1;
		this.valor = -1;
		this.blockPercent = -1;*/
	}
	
	public int getPk_id()
	{
		return this.pk_id;
	}
	public MultiLanguageText getAbility()
	{
		return ability;
	}
	public void setAbility(MultiLanguageText ability)
	{
		this.ability = ability;
	}
	public Integer getIntelligence()
	{
		return intelligence;
	}
	public void setIntelligence(Integer intelligence)
	{
		this.intelligence = intelligence;
	}
	public Integer getWisdom()
	{
		return wisdom;
	}
	public void setWisdom(Integer wisdom)
	{
		this.wisdom = wisdom;
	}
	public Integer getEndurance()
	{
		return endurance;
	}
	public void setEndurance(Integer endurance)
	{
		this.endurance = endurance;
	}
	public Integer getDexterity()
	{
		return dexterity;
	}
	public void setDexterity(Integer dexterity)
	{
		this.dexterity = dexterity;
	}
	public Integer getStrength()
	{
		return strength;
	}
	public void setStrength(Integer strength)
	{
		this.strength = strength;
	}
	public Integer getAttackPower()
	{
		return attackPower;
	}
	public void setAttackPower(Integer attackPower)
	{
		this.attackPower = attackPower;
	}
	public Integer getWeaponParry()
	{
		return weaponParry;
	}
	public void setWeaponParry(Integer weaponParry)
	{
		this.weaponParry = weaponParry;
	}
	public Integer getShieldBlock()
	{
		return shieldBlock;
	}
	public void setShieldBlock(Integer shieldBlock)
	{
		this.shieldBlock = shieldBlock;
	}
	public Integer getSpellCriticalHit()
	{
		return spellCriticalHit;
	}
	public void setSpellCriticalHit(Integer spellCriticalHit)
	{
		this.spellCriticalHit = spellCriticalHit;
	}
	
	public Integer getValor()
	{
		return valor;
	}

	public void setValor(Integer valor)
	{
		this.valor = valor;
	}

	public Integer getSpellFocus()
	{
		return spellFocus;
	}

	public void setSpellFocus(Integer spellFocus)
	{
		this.spellFocus = spellFocus;
	}

	public Integer getCriticalHit()
	{
		return criticalHit;
	}

	public void setCriticalHit(Integer criticalHit)
	{
		this.criticalHit = criticalHit;
	}

	public Integer getDodge()
	{
		return dodge;
	}

	public void setDodge(Integer dodge)
	{
		this.dodge = dodge;
	}

	public Integer getHitBonus()
	{
		return hitBonus;
	}

	public void setHitBonus(Integer hitBonus)
	{
		this.hitBonus = hitBonus;
	}

	public Integer getSpellPower()
	{
		return spellPower;
	}

	public void setSpellPower(Integer spellPower)
	{
		this.spellPower = spellPower;
	}

	public Integer getResistanceFire()
	{
		return resistanceFire;
	}

	public void setResistanceFire(Integer resistanceFire)
	{
		this.resistanceFire = resistanceFire;
	}

	public Integer getResistanceLife()
	{
		return resistanceLife;
	}

	public void setResistanceLife(Integer resistanceLife)
	{
		this.resistanceLife = resistanceLife;
	}

	public Integer getResistanceWater()
	{
		return resistanceWater;
	}

	public void setResistanceWater(Integer resistanceWater)
	{
		this.resistanceWater = resistanceWater;
	}

	public Integer getResistanceAir()
	{
		return resistanceAir;
	}

	public void setResistanceAir(Integer resistanceAir)
	{
		this.resistanceAir = resistanceAir;
	}

	public Integer getResistanceDeath()
	{
		return resistanceDeath;
	}

	public void setResistanceDeath(Integer resistanceDeath)
	{
		this.resistanceDeath = resistanceDeath;
	}

	public Integer getToughness()
	{
		return toughness;
	}

	public void setToughness(Integer toughness)
	{
		this.toughness = toughness;
	}

	public Integer getResistanceEarth()
	{
		return resistanceEarth;
	}

	public void setResistanceEarth(Integer resistanceEarth)
	{
		this.resistanceEarth = resistanceEarth;
	}

	public Integer getBlockPercent()
	{
		return blockPercent;
	}

	public void setBlockPercent(Integer blockPercent)
	{
		this.blockPercent = blockPercent;
	}
	
	@Override public String toString()
	{
		return
			(this.ability != null ? " - "+ability+"\n" : "") +  
			(this.intelligence != -1 ? " - "+intelligence+"\n" : "") +  
			(this.wisdom != -1 ? " - "+wisdom+"\n" : "") +  
			(this.endurance != -1 ? " - "+endurance+"\n" : "") +  
			(this.dexterity != -1 ? " - "+dexterity+"\n" : "") +  
			(this.strength != -1 ? " - "+strength+"\n" : "") +  
			(this.attackPower != -1 ? " - "+attackPower+"\n" : "") +  
			(this.weaponParry != -1 ? " - "+weaponParry+"\n" : "") +  
			(this.shieldBlock != -1 ? " - "+shieldBlock+"\n" : "") +  
			(this.criticalHit != -1 ? " - "+criticalHit+"\n" : "") +  
			(this.dodge != -1 ? " - "+dodge+"\n" : "") +  
			(this.spellFocus != -1 ? " - "+spellFocus+"\n" : "") +  
			(this.hitBonus != -1 ? " - "+hitBonus+"\n" : "") +  
			(this.spellPower != -1 ? " - "+spellPower+"\n" : "") +  
			(this.resistanceFire != -1 ? " - "+resistanceFire+"\n" : "") +  
			(this.resistanceLife != -1 ? " - "+resistanceLife+"\n" : "") +  
			(this.resistanceWater != -1 ? " - "+resistanceWater+"\n" : "") +  
			(this.resistanceAir != -1 ? " - "+resistanceAir+"\n" : "") +  
			(this.resistanceDeath != -1 ? " - "+resistanceDeath+"\n" : "") +  
			(this.resistanceEarth != -1 ? " - "+resistanceEarth+"\n" : "") +  
			(this.toughness != -1 ? " - "+toughness+"\n" : "") +  
			(this.valor != -1 ? " - "+valor+"\n" : "") +  
			(this.blockPercent != -1 ? " - "+blockPercent+"\n" : "") +  
			(this.spellCriticalHit != -1 ? " - "+spellCriticalHit+"\n" : "");
	}

	private static int PK_ID = 0;
	
	private int pk_id;
	private MultiLanguageText ability;
	private Integer intelligence;
	private Integer wisdom;
	private Integer endurance;
	private Integer dexterity;
	private Integer strength;
	private Integer attackPower;
	private Integer weaponParry;
	private Integer shieldBlock;
	private Integer spellCriticalHit;
	private Integer criticalHit;
	private Integer dodge;
	private Integer spellFocus;
	private Integer spellPower;
	private Integer hitBonus;
	private Integer resistanceFire;
	private Integer resistanceLife;
	private Integer resistanceWater;
	private Integer resistanceAir;
	private Integer resistanceDeath;
	private Integer resistanceEarth;
	private Integer toughness;
	private Integer valor;
	private Integer blockPercent;
	
}
