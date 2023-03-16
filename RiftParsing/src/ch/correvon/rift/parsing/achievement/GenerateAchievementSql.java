package ch.correvon.rift.parsing.achievement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import ch.correvon.rift.helper.StringHelper;
import ch.correvon.rift.parsing.recipe.subObject.ItemQuantity;
import ch.correvon.rift.parsing.subObject.GetId;
import ch.correvon.rift.parsing.subObject.ListElement;
import ch.correvon.rift.parsing.subObject.MultiLanguageText;


// TODO en entier
public class GenerateAchievementSql
{
	public static void generateSql(List<Achievement> listRecipe, String fileName)
	{
		try
		{
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("TRUNCATE TABLE rift_recipe_multilanguage_text;\n");
			out.write("TRUNCATE TABLE rift_recipe_list;\n");
			out.write("TRUNCATE TABLE rift_recipe_recipe;\n");
			out.write("TRUNCATE TABLE rift_recipe_item_quantity;\n");
			for(Achievement recipe:listRecipe)
				out.write(generateSql(recipe) + "\n");
			out.close();
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		
		System.out.println("Fichier " + fileName + " créé, " + listRecipe.size() + " items parsés");
	}
	
	public static String generateSql(Achievement recipe)
	{
		StringBuilder sb = new StringBuilder(1000);

		int pkRecipeId = recipe.getPk_id();
		Integer fkMLName = getMultilanguageID(recipe.getName(), sb);
		Integer fkMLDescription = getMultilanguageID(recipe.getDescription(), sb);
//		Integer fkItemQuantity = getItemQuantity(recipe, sb);
//		Integer fkIngredients = getList(recipe.getIngredients(), pkRecipeId, sb);

		sb.append("insert into rift_recipe_recipe values (" + 
				StringHelper.quoteIfNotNull(pkRecipeId) + ", " + 
				StringHelper.quoteIfNotNull(recipe.getId()) + ", " + 
				StringHelper.quoteIfNotNull(fkMLName) + ", " + 
				StringHelper.quoteIfNotNull(fkMLDescription) + ", " + ");"); 
		
		String strReturn = null;
		try
		{
			strReturn = new String(sb.toString().getBytes("UTF-8"));
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return strReturn;
	}
	
	private static Integer getMultilanguageID(MultiLanguageText ml, StringBuilder sb)
	{
		if(ml == null)
			return null;

		sb.append(ml.getSql("rift_recipe_multilanguage_text") + "\n");
		return ml.getPk_id();
	}
	
	private static Integer getList(List<ItemQuantity> list, int objectId, StringBuilder sb)
	{
		if(list != null)
			for(ItemQuantity myEnum:list)
			{
				ListElement listElement = new ListElement();
				listElement.setGroupId(groupListId);
				listElement.setEnumId(getEnumId(myEnum));
				listElement.setObjectId(objectId);
				
				sb.append(myEnum.getSql() + "\n");
				sb.append(listElement.getSql("rift_recipe_list") + "\n");
			}
		groupListId++;
		return groupListId-1;
	}
	
	private static String getEnumId(GetId myEnum)
	{
		if(myEnum == null)
			return null;
		return myEnum.getId();
	}

	private static int groupListId = 0;
}
