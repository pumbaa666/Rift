package ch.correvon.rift.parsing.item.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class Rarity extends A_MyEnum
{
	public static final A_MyEnum TRASH = new Rarity(1, "Trash");
	public static final A_MyEnum COMMON = new Rarity(2, "Common");
	public static final A_MyEnum UNCOMMON = new Rarity(3, "Uncommon");
	public static final A_MyEnum RARE = new Rarity(4, "Rare");
	public static final A_MyEnum EPIC = new Rarity(5, "Epic");
	public static final A_MyEnum RELIC = new Rarity(6, "Relic");
	public static final A_MyEnum QUEST_ITEM = new Rarity(7, "Quest Item");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(7);
	
	static
	{
		list.add(TRASH);
		list.add(COMMON);
		list.add(UNCOMMON);
		list.add(RARE);
		list.add(EPIC);
		list.add(RELIC);
		list.add(QUEST_ITEM);
	}
	
	public Rarity(int value, String name)
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
