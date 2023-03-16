package ch.correvon.rift.parsing.subObject;

public class A_MyEnum implements GetId
{
	public A_MyEnum(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public String getId()
	{
		return this.id+"";
	}
	
	public String getName()
	{
		return this.name;
	}
	
	@Override public String toString()
	{
		return this.name;
	}
	
	private int id;
	private String name;
}
