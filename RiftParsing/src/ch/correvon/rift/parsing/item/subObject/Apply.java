package ch.correvon.rift.parsing.item.subObject;


public class Apply
{
	public Apply()
	{
		this.pk_id = PK_ID;
		PK_ID++;
	}

	public int getPk_id()
	{
		return this.pk_id;
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
	public Integer getSpellFocus()
	{
		return spellFocus;
	}
	public void setSpellFocus(Integer spellFocus)
	{
		this.spellFocus = spellFocus;
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
	public Integer getResistanceEarth()
	{
		return resistanceEarth;
	}
	public void setResistanceEarth(Integer resistanceEarth)
	{
		this.resistanceEarth = resistanceEarth;
	}
	public Integer getToughness()
	{
		return toughness;
	}
	public void setToughness(Integer toughness)
	{
		this.toughness = toughness;
	}
	public Integer getValor()
	{
		return valor;
	}
	public void setValor(Integer valor)
	{
		this.valor = valor;
	}
	public Integer getBlockPercent()
	{
		return blockPercent;
	}
	public void setBlockPercent(Integer blockPercent)
	{
		this.blockPercent = blockPercent;
	}
	public Integer getMovementSpeedMultiplierUnmounted()
	{
		return movementSpeedMultiplierUnmounted;
	}
	public void setMovementSpeedMultiplierUnmounted(Integer movementSpeedMultiplierUnmounted)
	{
		this.movementSpeedMultiplierUnmounted = movementSpeedMultiplierUnmounted;
	}
	public Integer getMovementSpeedMultiplierMounted()
	{
		return movementSpeedMultiplierMounted;
	}
	public void setMovementSpeedMultiplierMounted(Integer movementSpeedMultiplierMounted)
	{
		this.movementSpeedMultiplierMounted = movementSpeedMultiplierMounted;
	}
	public Integer getMageManaPointsMax()
	{
		return mageManaPointsMax;
	}

	public void setMageManaPointsMax(Integer mageManaPointsMax)
	{
		this.mageManaPointsMax = mageManaPointsMax;
	}
	public Integer getWeaponDamage()
	{
		return weaponDamage;
	}
	public void setWeaponDamage(Integer weaponDamage)
	{
		this.weaponDamage = weaponDamage;
	}
	public Integer getResistancePhysical()
	{
		return resistancePhysical;
	}

	public void setResistancePhysical(Integer resistancePhysical)
	{
		this.resistancePhysical = resistancePhysical;
	}
	
	public Integer getWeaponDPS()
	{
		return weaponDPS;
	}
	public void setWeaponDPS(Integer weaponDPS)
	{
		this.weaponDPS = weaponDPS;
	}

	@Override public String toString()
	{
		return
			this.hitBonus + "\n";
		// TODO
	}

	private static int PK_ID = 0;
	
	private int pk_id;
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
	private Integer hitBonus;
	private Integer spellPower;
	private Integer resistanceFire;
	private Integer resistanceLife;
	private Integer resistanceWater;
	private Integer resistanceAir;
	private Integer resistanceDeath;
	private Integer resistanceEarth;
	private Integer toughness;
	private Integer valor;
	private Integer blockPercent;
	private Integer movementSpeedMultiplierUnmounted;
	private Integer movementSpeedMultiplierMounted;
	private Integer weaponDamage;
	private Integer mageManaPointsMax;
	private Integer resistancePhysical;
	private Integer weaponDPS;
}
