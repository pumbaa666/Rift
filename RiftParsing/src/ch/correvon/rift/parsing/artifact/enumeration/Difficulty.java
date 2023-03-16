package ch.correvon.rift.parsing.artifact.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.item.enumeration.ArmorType;
import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;

public class Difficulty extends A_MyEnum
{
	public static final A_MyEnum EASY = new ArmorType(1, "Easy");
	public static final A_MyEnum NORMAL = new ArmorType(2, "Normal");
	public static final A_MyEnum HARD = new ArmorType(3, "Hard");
	public static final A_MyEnum VERY_HARD = new ArmorType(4, "Very Hard");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(4);
	
	static
	{
		list.add(EASY);
		list.add(NORMAL);
		list.add(HARD);
		list.add(VERY_HARD);
	}
	
	public Difficulty(int value, String name)
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