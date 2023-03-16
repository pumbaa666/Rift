package ch.correvon.rift.parsing.artifact.enumeration;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.riftParsing.subObject.A_MyEnum;

import org.xml.sax.SAXException;

public class Location extends A_MyEnum
{
	private static int i = 1;
	public static final A_MyEnum SILVERWOOD = new Location(i++, "Silverwood");
	public static final A_MyEnum FREEMARCH = new Location(i++, "Freemarch");
	public static final A_MyEnum GLOAMWOOD = new Location(i++, "Gloamwood");
	public static final A_MyEnum STONEFIELD = new Location(i++, "Stonefield");
	public static final A_MyEnum SCARLET_GORGE = new Location(i++, "Scarlet Gorge");
	public static final A_MyEnum SCARWOOD_REACH = new Location(i++, "Scarwood Reach");
	public static final A_MyEnum DROUGHTLANDS = new Location(i++, "Droughtlands");
	public static final A_MyEnum MOONSHADE_HIGHLANDS = new Location(i++, "Moonshade Highlands");
	public static final A_MyEnum IRON_PINE_PEAK = new Location(i++, "Iron Pine Peak");
	public static final A_MyEnum SHIMMERSAND = new Location(i++, "Shimmersand");
	public static final A_MyEnum STILLMOOR = new Location(i++, "Stillmoor");
	public static final A_MyEnum SANCTUM = new Location(i++, "Sanctum");
	public static final A_MyEnum MERIDIAN = new Location(i++, "Meridian");
	public static final A_MyEnum WORLD = new Location(i++, "World");
	
	private static final List<A_MyEnum> list = new ArrayList<A_MyEnum>(i);
	
	static
	{
		list.add(SILVERWOOD);
		list.add(FREEMARCH);
		list.add(GLOAMWOOD);
		list.add(STONEFIELD);
		list.add(SCARLET_GORGE);
		list.add(SCARWOOD_REACH);
		list.add(DROUGHTLANDS);
		list.add(MOONSHADE_HIGHLANDS);
		list.add(IRON_PINE_PEAK);
		list.add(SHIMMERSAND);
		list.add(STILLMOOR);
		list.add(SANCTUM);
		list.add(MERIDIAN);
		list.add(WORLD);
	}
	
	public Location(int value, String name)
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