package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class ArmorType extends A_MyEnum
{
	public static final A_MyEnum CLOTH = new ArmorType(1, "Cloth");
	public static final A_MyEnum LEATHER = new ArmorType(2, "Leather");
	public static final A_MyEnum CHAIN = new ArmorType(3, "Chain");
	public static final A_MyEnum PLATE = new ArmorType(4, "Plate");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(4);
	
	static
	{
		list.add(CLOTH);
		list.add(LEATHER);
		list.add(CHAIN);
		list.add(PLATE);
	}
	
	public ArmorType(int value, String name)
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
