package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class Slot extends A_MyEnum
{
	public static final A_MyEnum HELMET = new Slot(1, "Helmet");
	public static final A_MyEnum CHEST = new Slot(2, "Chest");
	public static final A_MyEnum ONE_HAND = new Slot(3, "OneHand");
	public static final A_MyEnum NECK = new Slot(4, "Neck");
	public static final A_MyEnum TWO_HANDED = new Slot(5, "TwoHanded");
//	public static final A_MyEnum WEAPON_2H = new Slot(5, "Weapon_2h");
	public static final A_MyEnum LEGS = new Slot(6, "Legs");
	public static final A_MyEnum FEET = new Slot(7, "Feet");
	public static final A_MyEnum WEAPON_RANGED = new Slot(8, "Weapon_Ranged");
	public static final A_MyEnum BELT = new Slot(9, "Belt");
	public static final A_MyEnum OFF_HAND = new Slot(10, "OffHand");
	public static final A_MyEnum RING = new Slot(11, "Ring");
	public static final A_MyEnum TRINKET_2 = new Slot(12, "Trinket_2");
	public static final A_MyEnum SHOULDERS = new Slot(13, "Shoulders");
	public static final A_MyEnum GLOVES = new Slot(14, "Gloves");
	public static final A_MyEnum MAIN_HAND = new Slot(15, "MainHand");
	public static final A_MyEnum MOUNT = new Slot(16, "Mount"); // ?!? voir objet E3F6FEB40701010101
	public static final A_MyEnum WEAPON_OFF = new Slot(17, "Weapon_Off");
	public static final A_MyEnum RIFT_VESSEL = new Slot(18, "Rift Vessel");
	public static final A_MyEnum WEAPON_2H = new Slot(19, "Weapon_2h");
	public static final A_MyEnum SYNERGY_CRYSTAL = new Slot(20, "Synergy Crystal");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(20);
	
	static
	{
		list.add(HELMET);
		list.add(CHEST);
		list.add(ONE_HAND);
		list.add(NECK);
//		list.add(WEAPON_2H);
		list.add(TWO_HANDED);
		list.add(LEGS);
		list.add(FEET);
		list.add(WEAPON_RANGED);
		list.add(BELT);
		list.add(OFF_HAND);
		list.add(RING);
		list.add(TRINKET_2);
		list.add(SHOULDERS);
		list.add(GLOVES);
		list.add(MAIN_HAND);
		list.add(MOUNT);
		list.add(WEAPON_OFF);
		list.add(RIFT_VESSEL);
		list.add(WEAPON_2H);
		list.add(SYNERGY_CRYSTAL);
	}
	
	public Slot(int value, String name)
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
