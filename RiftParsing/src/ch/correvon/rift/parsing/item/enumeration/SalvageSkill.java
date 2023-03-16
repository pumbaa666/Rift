package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class SalvageSkill extends A_MyEnum
{
	public static final A_MyEnum ARTIFICER = new SalvageSkill(1, "Artificer");
	public static final A_MyEnum ALCHEMIST = new SalvageSkill(1, "Alchemist");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(1);
	
	static
	{
		list.add(ARTIFICER);
		list.add(ALCHEMIST);
	}
	
	public SalvageSkill(int value, String name)
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
