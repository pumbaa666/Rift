package ch.correvon.rift.parsing.recipe.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;


public class RequiredSkill extends A_MyEnum
{
	public static final A_MyEnum LIGHT_WEAPONSMITH = new RequiredSkill(1, "Light Weaponsmith");
	public static final A_MyEnum OUTFITTER = new RequiredSkill(2, "Outfitter");
	public static final A_MyEnum RUNECRAFTER = new RequiredSkill(3, "Runecrafter");
	public static final A_MyEnum ARMORSMITH = new RequiredSkill(4, "Armorsmith");
	public static final A_MyEnum ALCHEMIST = new RequiredSkill(5, "Alchemist");
	public static final A_MyEnum ARTIFICER = new RequiredSkill(6, "Artificer");
	public static final A_MyEnum MINING = new RequiredSkill(7, "Mining");
	public static final A_MyEnum BUTCHERING = new RequiredSkill(8, "Butchering");
	public static final A_MyEnum FORAGING = new RequiredSkill(9, "Foraging");
		
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(9);
	
	static
	{
		list.add(LIGHT_WEAPONSMITH);
		list.add(OUTFITTER);
		list.add(RUNECRAFTER);
		list.add(ARMORSMITH);
		list.add(ALCHEMIST);
		list.add(ARTIFICER);
		list.add(MINING);
		list.add(BUTCHERING);
		list.add(FORAGING);
	}
	
	public RequiredSkill(int value, String name)
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
