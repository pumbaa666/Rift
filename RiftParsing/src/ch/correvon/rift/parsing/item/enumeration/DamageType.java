package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class DamageType extends A_MyEnum
{
	public static final A_MyEnum AIR = new DamageType(1, "Air");
	public static final A_MyEnum DEATH = new DamageType(2, "Death");
	public static final A_MyEnum LIFE = new DamageType(3, "Life");
	public static final A_MyEnum WATER = new DamageType(4, "Water");
	public static final A_MyEnum FIRE = new DamageType(5, "Fire");
	public static final A_MyEnum EARTH = new DamageType(6, "Earth");
	
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(6);
	
	static
	{
		list.add(AIR);
		list.add(DEATH);
		list.add(LIFE);
		list.add(WATER);
		list.add(FIRE);
		list.add(EARTH);
	}
	
	public DamageType(int value, String name)
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
