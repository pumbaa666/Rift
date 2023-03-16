package ch.correvon.rift.parsing.use;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.parsing.item.GenerateItemSql;
import ch.correvon.rift.parsing.item.ItemHandler;
import ch.correvon.rift.parsing.recipe.GenerateRecipeSql;
import ch.correvon.rift.parsing.recipe.RecipeHandler;
import ch.correvon.rift.parsing.xml.XMLParser;

import org.xml.sax.SAXException;

public class MainRiftParser
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int i = 0;
		int nbArg = args.length;
		if(nbArg == 0)
			printHelp();
		for(String arg:args)
		{
			if(arg.equals("-h"))
				printHelp();
			else if(arg.equals("-c"))
				cleanFile = true;
			else if(arg.equals("-i") && i < nbArg)
				generateItemSql(args[i+1]);
			else if(arg.equals("-r") && i < nbArg)
				generateRecipeSql(args[i+1]);
			i++;
		}
	}
	
	public static void generateItemSql(String fileName)
	{
		fileName = checkFileName(fileName);
		if(fileName == null)
			return;
		
		ItemHandler itemHandler = (ItemHandler)XMLParser.readXML(fileName, ItemHandler.class);
		
		try
		{
			GenerateItemSql.generateSql(itemHandler.getItems(), "insertItem.sql");
		}
		catch(SAXException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateRecipeSql(String fileName)
	{
		fileName = checkFileName(fileName);
		if(fileName == null)
			return;
		
		RecipeHandler itemHandler = (RecipeHandler)XMLParser.readXML(fileName, RecipeHandler.class);
		
		try
		{
			GenerateRecipeSql.generateSql(itemHandler.getRecipes(), "insertRecipe.sql");
		}
		catch(SAXException e)
		{
			e.printStackTrace();
		}
	}
	
	private static String checkFileName(String fileName)
	{
		if(fileName == null || fileName.isEmpty())
		{
			printHelp();
			return null;
		}
		
		File file = new File(fileName);
		if(file.exists())
		{
			if(cleanFile)
				file = cleanFile(fileName);
			fileName = file.toURI().toString();
		}
		else
		{
			try
			{
				file = new File(new URI(fileName));
				if(!file.exists())
					new URI(":::"); // Rise an URISyntaxException to show error
			}
			catch(URISyntaxException e)
			{
				System.err.println("Impossible de trouver le fichier "+fileName);
				e.printStackTrace();
				return null;
			}
		}
		return fileName;
	}
	
	private static File cleanFile(String fileName)
	{
		System.out.println("Épurage du fichier "+fileName);
		BufferedReader br = null;
		PrintWriter pw = null;
		String newFileName = fileName+"_clean";
		File cleanFile = null;
		try
		{
			//flux de lecture en UTF-8
			//br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			cleanFile = new File(newFileName);
			cleanFile.createNewFile();
			
			//flux d'écriture en ISO (valeur par défaut)
			pw = new PrintWriter(new FileOutputStream(cleanFile));
			String ligne;
			boolean inFirstLootedBy = false;
			List<String> firstList = new ArrayList<String>(10);
			firstList.add("FirstLootedBy");
			firstList.add("FirstLearnedBy");
			firstList.add("FirstCompletedBy");
			
			while((ligne = br.readLine()) != null)
			{
				if(firstList.contains("<"+ligne+">"))
					inFirstLootedBy = true;
				
				if(!inFirstLootedBy)
					pw.println(ligne);

				if(firstList.contains("</"+ligne+">"))
					inFirstLootedBy = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try{pw.close();}
			catch(Throwable e){}
			try{br.close();}
			catch(Throwable e){}
		}
		
		return cleanFile;
	}
	
	private static void printHelp()
	{
		System.out.println("RiftParsing v0.3 codé par Pumbaa");
		System.out.println("Usage : java -jar riftParsing0.1.jar [-c][-i RIFT_ITEM_FILENAME.xml][-r RIFT_RECIPE_FILENAME.xml]");
		System.out.println("\t-c clean files, remove FirstLootedBy, FirstLootedBy, ... (must be first argument)");
	}
	
	private static boolean cleanFile = false;
}
