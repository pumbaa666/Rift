package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class RuneAllowedSlots extends A_MyEnum
{
	private static int i = 1;
	public static final A_MyEnum GLOVES = new RuneAllowedSlots(i++, "Gloves");
	public static final A_MyEnum WEAPON_2H = new RuneAllowedSlots(i++, "Weapon_2h");
	public static final A_MyEnum FEET = new RuneAllowedSlots(i++, "Feet");
	public static final A_MyEnum CHEST = new RuneAllowedSlots(i++, "Chest");
	public static final A_MyEnum SHOULDERS = new RuneAllowedSlots(i++, "Shoulders");
	public static final A_MyEnum PANTS = new RuneAllowedSlots(i++, "Pants");
	public static final A_MyEnum BELT = new RuneAllowedSlots(i++, "Belt");
	public static final A_MyEnum LEGS = new RuneAllowedSlots(i++, "Legs");
	public static final A_MyEnum WEAPON_MAIN = new RuneAllowedSlots(i++, "Weapon_Main");
	public static final A_MyEnum HELMET = new RuneAllowedSlots(i++, "Helmet");
	public static final A_MyEnum WEAPON_RANGED = new RuneAllowedSlots(i++, "Weapon_Ranged");
	public static final A_MyEnum WEAPON_OFF = new RuneAllowedSlots(i++, "Weapon_Off");
		
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(i);
	
	static
	{
		list.add(GLOVES);
		list.add(WEAPON_2H);
		list.add(FEET);
		list.add(CHEST);
		list.add(SHOULDERS);
		list.add(PANTS);
		list.add(BELT);
		list.add(LEGS);
		list.add(WEAPON_MAIN);
		list.add(HELMET);
		list.add(WEAPON_RANGED);
		list.add(WEAPON_OFF);
	}
	
	public RuneAllowedSlots(int value, String name)
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
