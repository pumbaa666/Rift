package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class WeaponType extends A_MyEnum
{
	public static final A_MyEnum TOTEM = new WeaponType(1, "Totem");
	public static final A_MyEnum SHIELD = new WeaponType(2, "Shield");
	
	public static final A_MyEnum ONE_H_DAGGER = new WeaponType(3, "1h_dagger");
	public static final A_MyEnum ONE_H_SWORD = new WeaponType(4, "1h_sword");
	public static final A_MyEnum ONE_H_MACE = new WeaponType(5, "1h_mace");
	public static final A_MyEnum ONE_H_AXE = new WeaponType(6, "1h_axe");
	public static final A_MyEnum ONE_H_WAND = new WeaponType(7, "1h_wand");
	public static final A_MyEnum ONE_H_WAND_AIR = new WeaponType(8, "1h_wand_air");
	public static final A_MyEnum ONE_H_WAND_LIFE = new WeaponType(9, "1h_wand_life");
	public static final A_MyEnum ONE_H_WAND_FIRE = new WeaponType(10, "1h_wand_fire");
	public static final A_MyEnum ONE_H_WAND_WATER = new WeaponType(11, "1h_wand_water");
	public static final A_MyEnum ONE_H_WAND_EARTH = new WeaponType(12, "1h_wand_earth");
	public static final A_MyEnum ONE_H_WAND_DEATH_EPIC = new WeaponType(13, "1h_wand_death_epic");
	public static final A_MyEnum ONE_H_WAND_DEATH_EPIC_A = new WeaponType(14, "1h_wand_death_epic_a");
	
	public static final A_MyEnum TWO_H_RANGED_GUN = new WeaponType(15, "2h_ranged_gun");
	public static final A_MyEnum TWO_H_RANGED_BOW = new WeaponType(16, "2h_ranged_bow");
	public static final A_MyEnum TWO_H_SWORD = new WeaponType(17, "2h_sword");
	public static final A_MyEnum TWO_H_HAMMER = new WeaponType(18, "2h_hammer");
	public static final A_MyEnum TWO_H_STAFF = new WeaponType(19, "2h_staff");
	public static final A_MyEnum TWO_H_AXE = new WeaponType(20, "2h_axe");
	public static final A_MyEnum TWO_H_POLEARM = new WeaponType(21, "2h_polearm");
	public static final A_MyEnum TWO_H_SHOVEL = new WeaponType(22, "2h_shovel");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(22);
	
	static
	{
		list.add(TOTEM);
		list.add(SHIELD);
		list.add(ONE_H_DAGGER);
		list.add(ONE_H_SWORD);
		list.add(ONE_H_MACE);
		list.add(ONE_H_AXE);
		list.add(ONE_H_WAND);
		list.add(ONE_H_WAND_AIR);
		list.add(ONE_H_WAND_LIFE);
		list.add(ONE_H_WAND_FIRE);
		list.add(ONE_H_WAND_WATER);
		list.add(ONE_H_WAND_EARTH);
		list.add(ONE_H_WAND_DEATH_EPIC);
		list.add(ONE_H_WAND_DEATH_EPIC_A);
		list.add(TWO_H_RANGED_GUN);
		list.add(TWO_H_RANGED_BOW);
		list.add(TWO_H_SWORD);
		list.add(TWO_H_HAMMER);
		list.add(TWO_H_STAFF);
		list.add(TWO_H_AXE);
		list.add(TWO_H_POLEARM);
		list.add(TWO_H_SHOVEL);
	}
	
	public WeaponType(int value, String name)
	{
		super(value, name);
	}

	public static A_MyEnum getEnum(String stringValue) throws SAXException
	{
		for(A_MyEnum myEnum:list)
			if(myEnum.getName().equals(stringValue))
				return myEnum;
		throw new SAXException("Enumération "+stringValue+" non trouvée");
	}
}
