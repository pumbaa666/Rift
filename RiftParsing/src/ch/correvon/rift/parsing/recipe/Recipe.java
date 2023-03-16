package ch.correvon.rift.parsing.recipe;

import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.helper.ListHelper;
import ch.correvon.rift.parsing.recipe.enumeration.RequiredSkill;
import ch.correvon.rift.parsing.recipe.subObject.ItemQuantity;
import ch.correvon.rift.parsing.subObject.MultiLanguageText;

public class Recipe
{
	public Recipe()
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
	public ItemQuantity getItemQuantity()
	{
		return itemQuantity;
	}
	public void setItemQuantity(ItemQuantity itemQuantity)
	{
		this.itemQuantity = itemQuantity;
	}
	public List<ItemQuantity> getIngredients()
	{
		return ingredients;
	}
	public void setIngredients(List<ItemQuantity> ingredients)
	{
		this.ingredients = ingredients;
	}
	public void addIngredients(ItemQuantity ingredients)
	{
		if(this.ingredients == null)
			this.ingredients = new ArrayList<ItemQuantity>(5); // Volontairement bas, les recettes demandant plus d'ingrédients sont rares
		this.ingredients.add(ingredients);
	}
	public RequiredSkill getRequiredSkill()
	{
		return requiredSkill;
	}
	public void setRequiredSkill(RequiredSkill requiredSkill)
	{
		this.requiredSkill = requiredSkill;
	}
	public int getRequiredSkillPoints()
	{
		return requiredSkillPoints;
	}
	public void setRequiredSkillPoints(int requiredSkillPoints)
	{
		this.requiredSkillPoints = requiredSkillPoints;
	}
	public int getHighUntil()
	{
		return highUntil;
	}
	public void setHighUntil(int highUntil)
	{
		this.highUntil = highUntil;
	}
	public int getMediumUntil()
	{
		return mediumUntil;
	}
	public void setMediumUntil(int mediumUntil)
	{
		this.mediumUntil = mediumUntil;
	}
	public int getLowUntil()
	{
		return lowUntil;
	}
	public void setLowUntil(int lowUntil)
	{
		this.lowUntil = lowUntil;
	}
	
	@Override public String toString()
	{
		return 
			this.id + "\n" +
			this.name + "\n" +
			this.description + "\n" +
			this.itemQuantity + "\n" +
			ListHelper.printList(this.ingredients) +
			this.requiredSkill + "\n" +
			this.requiredSkillPoints + "\n" +
			this.highUntil + "\n" +
			this.mediumUntil + "\n" +
			this.lowUntil;
	}

	private static int PK_ID = 0;

	private int pk_id;
	private String id;
	private MultiLanguageText name;
	private MultiLanguageText description;
	private ItemQuantity itemQuantity;
	private List<ItemQuantity> ingredients;
	private int trainerCost;
	private RequiredSkill requiredSkill;
	private int requiredSkillPoints;
	private int highUntil;
	private int mediumUntil;
	private int lowUntil;
	
	public int getTrainerCost()
	{
		return trainerCost;
	}
	public void setTrainerCost(int trainerCost)
	{
		this.trainerCost = trainerCost;
	}
}
