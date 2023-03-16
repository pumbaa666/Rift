package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class Calling extends A_MyEnum
{
	public static final A_MyEnum ROGUE = new Calling(1, "Rogue");
	public static final A_MyEnum CLERIC = new Calling(2, "Cleric");
	public static final A_MyEnum MAGE = new Calling(3, "Mage");
	public static final A_MyEnum WARRIOR = new Calling(4, "Warrior");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(4);
	
	static
	{
		list.add(ROGUE);
		list.add(CLERIC);
		list.add(MAGE);
		list.add(WARRIOR);
	}
	
	public Calling(int value, String name)
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
