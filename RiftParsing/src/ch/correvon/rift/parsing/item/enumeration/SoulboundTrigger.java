package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class SoulboundTrigger extends A_MyEnum
{
	public static final A_MyEnum BIND_ON_EQUIP = new SoulboundTrigger(1, "BindOnEquip");
	public static final A_MyEnum BIND_ON_PICKUP = new SoulboundTrigger(2, "BindOnPickup");
	public static final A_MyEnum BIND_ON_USE = new SoulboundTrigger(3, "BindOnUse");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(3);
	
	static
	{
		list.add(BIND_ON_EQUIP);
		list.add(BIND_ON_PICKUP);
		list.add(BIND_ON_USE);
	}
	
	public SoulboundTrigger(int value, String name)
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
