package ch.correvon.rift.parsing.achievement;

import java.util.List;

import ch.correvon.rift.parsing.artifact.enumeration.Difficulty;
import ch.correvon.rift.parsing.item.Item;
import ch.correvon.rift.parsing.subObject.MultiLanguageText;

public class Achievement
{
	public Achievement()
	{
		this.pk_id = PK_ID;
		PK_ID++;
	}
	public int getPk_id()
	{
		return this.pk_id;
	}
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public MultiLanguageText getName()
	{
		return name;
	}
	public void setName(MultiLanguageText name)
	{
		this.name = name;
	}
	public MultiLanguageText getDescription()
	{
		return description;
	}
	public void setDescription(MultiLanguageText description)
	{
		this.description = description;
	}
	
	@Override public String toString()
	{
		return 
			this.id + "\n" +
			this.name + "\n" +
			this.description + "\n";
		// TODO suite
	}

	public Integer getLevel()
	{
		return level;
	}
	public void setLevel(Integer level)
	{
		this.level = level;
	}
	public Difficulty getDifficulty()
	{
		return difficulty;
	}
	public void setDifficulty(Difficulty difficulty)
	{
		this.difficulty = difficulty;
	}
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
	public List<Item> getItemList()
	{
		return itemList;
	}
	public void setItemList(List<Item> itemList)
	{
		this.itemList = itemList;
	}

	private static int PK_ID = 0;

	private int pk_id;
	private String id;
	private Category category;
	private MultiLanguageText name;
	private MultiLanguageText description;
	private Faction faction;
	private Score score;
	private List<Achievement> requirement;
}
