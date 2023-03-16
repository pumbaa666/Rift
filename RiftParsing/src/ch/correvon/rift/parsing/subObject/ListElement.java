package ch.correvon.rift.parsing.subObject;

public class ListElement
{
	public ListElement()
	{
		this.pk_id = PK_ID;
		PK_ID++;
	}
	
	public int getPk_id()
	{
		return this.pk_id;
	}
	
	public int getGroupId()
	{
		return groupId;
	}

	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}

	public String getEnumId()
	{
		return enumId;
	}

	public void setEnumId(String enumId)
	{
		this.enumId = enumId;
	}

	public int getObjectId()
	{
		return objectId;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}
	
	public String getSql(String table)
	{
		return "insert into " + table + " values (" + 
		"'" + this.pk_id + "', " + 
		"'" + this.groupId + "', " + 
		"'" + this.enumId + "', " + 
		"'" + this.objectId + "');";
	}
	
	public static void resetId()
	{
		PK_ID = 0;
	}

	private static int PK_ID = 0;
	private int pk_id;
	private int groupId;
	private String enumId;
	private int objectId;
}
