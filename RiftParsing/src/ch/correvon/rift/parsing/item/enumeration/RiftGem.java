package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class RiftGem extends A_MyEnum
{
	public static final A_MyEnum LESSER = new RiftGem(1, "Lesser");
	public static final A_MyEnum GREATER = new RiftGem(2, "Greater");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(2);
	
	static
	{
		list.add(LESSER);
		list.add(GREATER);
	}
	
	public RiftGem(int value, String name)
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
