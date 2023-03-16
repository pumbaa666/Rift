package ch.correvon.rift.parsing.subObject;


public class ItemQuantity implements GetId
{
	public ItemQuantity()
	{
		this.pk_id = PK_ID;
		PK_ID++;
	}
	
	public int getPk_id()
	{
		return this.pk_id;
	}
	@Override public String getId()
	{
		return getPk_id()+""; // TODO retourner un int
//		return this.getItemKey();
	}
	public String getItemKey()
	{
		return itemKey;
	}
	public void setItemKey(String itemKey)
	{
		this.itemKey = itemKey;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	@Override public String toString()
	{
		return
			" - " + this.itemKey + "\n" + 
			" - " + this.quantity;
	}
	
	public String getSql(String tableName)
	{
		return "insert into " + tableName + " values (" + 
		"'" + this.pk_id + "', " + 
		"'" + this.itemKey + "', " + 
		"'" + this.quantity + "');";
	}
	
	private static int PK_ID = 0;
	
	private int pk_id;
	private String itemKey;
	private int quantity;
}
