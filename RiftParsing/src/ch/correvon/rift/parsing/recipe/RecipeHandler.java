package ch.correvon.rift.parsing.recipe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.correvon.rift.helper.ListHelper;
import ch.correvon.rift.parsing.recipe.enumeration.RequiredSkill;
import ch.correvon.rift.parsing.recipe.subObject.ItemQuantity;
import ch.correvon.rift.parsing.subObject.ListElement;
import ch.correvon.rift.parsing.subObject.MultiLanguageText;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Tuto, voir http://java.developpez.com/faq/xml/?page=sax#parserSax
 * @author lco
 *
 */
public class RecipeHandler extends DefaultHandler
{
	public RecipeHandler()
	{
		super();
		this.recipe = null;
		this.multiLanguageText = null;
		this.itemQuantity = null;
		
		this.count = 0;
		MultiLanguageText.resetId();
		ListElement.resetId();
	}

	// Détection d'ouverture de balise
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	throws SAXException
	{
		this.isReady = false;
		this.buffer = new StringBuffer();

		if(qName.equals("Recipes"))
			this.recipeList = new LinkedList<Recipe>();
		else if(qName.equals("Recipe"))
			this.recipe = new Recipe();
		else if(qName.equals("Id"))
			this.inId = true;
		else if(qName.equals("Name"))
		{
			if(!this.inName && !this.inItem && !this.inFirstLearnedBy)
			{
				this.inName = true;
				this.multiLanguageText = new MultiLanguageText();
			}
		}
		else if(qName.equals("Description"))
		{
			if(!this.inDescription && !this.inItem && !this.inFirstLearnedBy)
			{
				this.inDescription = true;
				this.multiLanguageText = new MultiLanguageText();
			}
		}
		else if(qName.equals("English"))
			this.inEnglish = true;
		else if(qName.equals("French"))
			this.inFrench = true;
		else if(qName.equals("German"))
			this.inGerman = true;
		else if(qName.equals("Creates"))
			this.inCreates = true;
		else if(qName.equals("Ingredients"))
			this.inIngredients = true;
		else if(qName.equals("Item"))
		{
			if(this.inIngredients || this.inCreates)
				this.itemQuantity = new ItemQuantity();
			else
				throw new SAXException("Devrait être dans Ingredients ou Creates une fois dans item (start)");
			this.inItem = true;
		}
		else if(qName.equals("ItemKey"))
			this.inItemKey = true;
		else if(qName.equals("Quantity"))
			this.inQuantity = true;
		else if(qName.equals("TrainerCost"))
			this.inTrainerCost = true;
		else if(qName.equals("RequiredSkill"))
			this.inRequiredSkill = true;
		else if(qName.equals("RequiredSkillPoints"))
			this.inRequiredSkillPoints = true;
		else if(qName.equals("HighUntil"))
			this.inHighUntil = true;
		else if(qName.equals("MediumUntil"))
			this.inMediumUntil = true;
		else if(qName.equals("LowUntil"))
			this.inLowUntil = true;
		else if(qName.equals("FirstLearnedBy"))
			this.inFirstLearnedBy = true;
		else
			if(!this.inFirstLearnedBy)
				throw new SAXException("Balise " + qName + " inconnue.");
	}

	// Détection fin de balise
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException
	{
		if(qName.equals("Recipes"))
		{
			// Nothing to do
		}
		else if(qName.equals("Recipe"))
		{
			this.recipeList.add(this.recipe);
			this.count++;
			if(this.count % 50 == 0)
			{
				double percent = (this.count / ((double)NB_ITEM)) * 100;
				String percentStr = new String(""+percent).substring(0,4);
				System.out.println(percentStr + "%");
			}
		}
		else if(qName.equals("Id"))
		{
			this.recipe.setId(this.getBuffer());
			this.inId = false;
		}
		else if(qName.equals("Name"))
		{
			if(this.inName && !this.inItem && !this.inFirstLearnedBy)
			{
				this.recipe.setName(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
				this.inName = false;
				this.multiLanguageText = null;
			}
		}
		else if(qName.equals("Description"))
		{
			if(this.inDescription)
			{
				this.recipe.setDescription(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
				this.inDescription = false;
				this.multiLanguageText = null;
			}
		}
		else if(qName.equals("English"))
		{
			if((this.inName || this.inDescription) && !this.inItem && !this.inFirstLearnedBy)
				this.multiLanguageText.setEnglish(this.getBuffer());
			this.inEnglish = false;
		}
		else if(qName.equals("French"))
		{
			if((this.inName || this.inDescription) && !this.inItem && !this.inFirstLearnedBy)
				this.multiLanguageText.setFrench(this.getBuffer());
			this.inFrench = false;
		}
		else if(qName.equals("German"))
		{
			if((this.inName || this.inDescription) && !this.inItem && !this.inFirstLearnedBy)
				this.multiLanguageText.setGerman(this.getBuffer());
			this.inGerman = false;
		}
		else if(qName.equals("Creates"))
			this.inCreates = false;
		else if(qName.equals("Ingredients"))
			this.inIngredients = false;
		else if(qName.equals("Item"))
		{
			if(this.inCreates)
				this.recipe.setItemQuantity(this.itemQuantity);
			else if(this.inIngredients)
				this.recipe.addIngredients(this.itemQuantity);
			else
				throw new SAXException("Devrait être dans Ingredients ou Creates une fois dans Item (end)");
			this.inItem = false;
		}
		else if(qName.equals("ItemKey"))
		{
			if(this.inCreates || this.inIngredients)
			{
				this.itemQuantity.setItemKey(this.getBuffer());
			}
			else
				throw new SAXException("Devrait être dans Ingredients ou Creates une fois dans ItemKey (end)");
			this.inItemKey = false;
		}
		else if(qName.equals("Quantity"))
		{
			if(this.inCreates || this.inIngredients)
				this.itemQuantity.setQuantity(new Integer(this.getBuffer()));
			else
				throw new SAXException("Devrait être dans Ingredients ou Creates une fois dans Quantity (end)");
			this.inQuantity = false;
		}
		else if(qName.equals("TrainerCost"))
		{
			this.recipe.setTrainerCost(new Integer(this.getBuffer()));
			this.inTrainerCost = false;
		}
		else if(qName.equals("RequiredSkill"))
		{
			this.recipe.setRequiredSkill((RequiredSkill)RequiredSkill.getEnum(this.getBuffer()));
			this.inRequiredSkill = false;
		}
		else if(qName.equals("RequiredSkillPoints"))
		{
			this.recipe.setRequiredSkillPoints(new Integer(this.getBuffer()));
			this.inRequiredSkillPoints = false;
		}
		else if(qName.equals("HighUntil"))
		{
			this.recipe.setHighUntil(new Integer(this.getBuffer()));
			this.inHighUntil = false;
		}
		else if(qName.equals("MediumUntil"))
		{
			this.recipe.setMediumUntil(new Integer(this.getBuffer()));
			this.inMediumUntil = false;
		}
		else if(qName.equals("LowUntil"))
		{
			this.recipe.setLowUntil(new Integer(this.getBuffer()));
			this.inLowUntil = false;
		}
		else if(qName.equals("FirstLearnedBy"))
			this.inFirstLearnedBy = false;
		else
			if(!this.inFirstLearnedBy)
				throw new SAXException("Balise "+qName+" inconnue.");
	}

	// Détection de caractères
	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException
	{
		String lecture = new String(ch, start, length);
//		if(this.buffer != null)
			this.buffer.append(lecture);
	}

	// Début du parsing
	@Override public void startDocument() throws SAXException
	{
		System.out.println("Début du parsing des recettes");
	}

	// Fin du parsing
	@Override public void endDocument() throws SAXException
	{
		System.out.println("Fin du parsing des recettes");
		this.isReady = true;
	}
	
	public List<Recipe> getRecipes() throws SAXException
	{
		if(!this.isReady)
			throw new SAXException("Le parsing n'est pas fini");
			
		return this.recipeList;
	}
	
	private String getBuffer()
	{
		return this.buffer.toString();
	}
	
	private List<Recipe> recipeList; //résultats de notre parsing
	private Recipe recipe; // Objet en cours
	private StringBuffer buffer; // buffer nous permettant de récupérer les données
	
	private MultiLanguageText multiLanguageText;
	private List<MultiLanguageText> listML = new ArrayList<MultiLanguageText>(1500);
	private ItemQuantity itemQuantity;
	
	private boolean inId;
	private boolean inName;
	private boolean inDescription;
		private boolean inEnglish;
		private boolean inFrench;
		private boolean inGerman;
	private boolean inCreates;
		private boolean inItem;
		private boolean inItemKey;
		private boolean inQuantity;
		private boolean inIngredients;
	private boolean inTrainerCost;
	private boolean inRequiredSkill;
	private boolean inRequiredSkillPoints;
	private boolean inHighUntil;
	private boolean inMediumUntil;
	private boolean inLowUntil;
	
	private boolean inFirstLearnedBy;
	
	private boolean isReady;
	
	private int count;
	private static final int NB_ITEM = 1115;
//	private static final int PERCENT = NB_ITEM / 100;
}
