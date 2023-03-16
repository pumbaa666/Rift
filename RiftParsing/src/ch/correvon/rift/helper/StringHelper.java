package ch.correvon.rift.helper;

public class StringHelper
{
	/**
	 * Echappe les simple quote par des doubles quotes
	 * et change les backslash par des slashs pour que le chemin des icônes soit correct.
	 * @param str
	 * @return
	 */
	public static String escapeSql(String str)
	{
		if(str == null)
			return null;
		return str.replace("'", "''").replace("\\", "/");
	}
	
	public static String quoteIfNotNull(Object obj)
	{
		if(obj == null)
			return null;
		return "'" + obj.toString() + "'";
	}
}
