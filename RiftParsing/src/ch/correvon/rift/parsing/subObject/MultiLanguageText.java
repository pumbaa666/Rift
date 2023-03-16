package ch.correvon.rift.parsing.subObject;

import ch.correvon.rift.helper.StringHelper;

public class MultiLanguageText
{
	public MultiLanguageText()
	{
		this.pk_id = PK_ID;
		PK_ID++;
	}
	
	public int getPk_id()
	{
		return this.pk_id;
	}
	
	public String getEnglish()
	{
		return english;
	}

	public void setEnglish(String english)
	{
		this.english = english;
	}

	public String getFrench()
	{
		return french;
	}

	public void setFrench(String french)
	{
		this.french = french;
	}

	public String getGerman()
	{
		return german;
	}

	public void setGerman(String german)
	{
		this.german = german;
	}
	
	@Override public String toString()
	{
		return 
			" - " + this.english + "\n" + 
			" - " + this.french + "\n" + 
			" - " + this.german;
	}
	
	public String getSql(String table)
	{
		if(!this.alreadyInserted)
		{
			this.alreadyInserted = true;
			return "insert into " + table + " values (" + 
				"'" + this.pk_id + "', " + 
				"'" + StringHelper.escapeSql(this.english) + "', " + 
				"'" + StringHelper.escapeSql(this.french) + "', " + 
				"'" + StringHelper.escapeSql(this.german) + "');";
		}
		
		return "";
	}

	@Override public boolean equals(Object o)
	{
		MultiLanguageText ml = (MultiLanguageText)o;
		return 	this.english.equals(ml.getEnglish()) &&
				this.french.equals(ml.getFrench()) &&
				this.german.equals(ml.getGerman());
	}
	
	public static void resetId()
	{
		PK_ID = 0;
	}

	private static int PK_ID = 0;
	
	private int pk_id;
	private String english;
	private String french;
	private String german;
	
	private boolean alreadyInserted = false;
}
