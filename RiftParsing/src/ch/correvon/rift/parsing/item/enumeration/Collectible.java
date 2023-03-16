package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class Collectible extends A_MyEnum
{
	public static final A_MyEnum BOOKS = new Collectible(1, "Books");
	public static final A_MyEnum MOUNT = new Collectible(2, "Mount");
	public static final A_MyEnum PETS = new Collectible(3, "Pets");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(3);
	
	static
	{
		list.add(BOOKS);
		list.add(MOUNT);
		list.add(PETS);
	}
	
	public Collectible(int value, String name)
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
