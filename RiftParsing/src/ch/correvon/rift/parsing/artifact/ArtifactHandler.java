package ch.correvon.rift.parsing.artifact;

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
// TODO en entier 
public class ArtifactHandler extends DefaultHandler
{
	public ArtifactHandler()
	{
		super();
		this.artifact = null;
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

		if(qName.equals("ArtifactCollections"))
			this.artifactList = new LinkedList<Artifact>();
		else if(qName.equals("ArtifactCollection"))
			this.artifact = new Artifact();
		else if(qName.equals("Name"))
		{
			if(!this.inName && !this.inItem && !this.inReward && !this.inFirstCompletedBy)
			{
				this.inName = true;
				this.multiLanguageText = new MultiLanguageText();
			}
		}
		else if(qName.equals("Description"))
		{
			if(!this.inDescription && !this.inItem && !this.inFirstCompletedBy)
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
		else if(qName.equals("Level"))
			this.inLevel = true;
		else if(qName.equals("Difficulty"))
			this.inDifficulty = true;
		else if(qName.equals("Location"))
			this.inLocation = true;
		else if(qName.equals("Items"))
			this.inItem = true;
		else if(qName.equals("ItemKey"))
		{
			if(this.inItem)
				this.itemQuantity = new ItemQuantity();
			else if(!this.inReward)
				throw new SAXException("Devrait être dans Item ou Creates une fois dans ItemKey (start)");
			this.inItem = true;
		}
		else if(qName.equals("Rewards"))
			this.inRewards = true;
		else if(qName.equals("Guaranteed"))
			this.inGuaranteed = true;
		else if(qName.equals("Quantity"))
			this.inQuantity = true;
		else if(qName.equals("Reward"))
			this.inReward = true;
		else if(qName.equals("FirstCompletedBy"))
			this.inFirstCompletedBy = true;
		else
			if(!this.inFirstCompletedBy)
				throw new SAXException("Balise " + qName + " inconnue.");
	}

	// Détection fin de balise
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException
	{
		if(qName.equals("ArtifactCollections"))
		{
			// Nothing to do
		}
		else if(qName.equals("ArtifactCollection"))
		{
			this.artifactList.add(this.artifact);
			this.count++;
			if(this.count % 50 == 0)
			{
				double percent = (this.count / ((double)NB_ITEM)) * 100;
				String percentStr = new String(""+percent).substring(0,4);
				System.out.println(percentStr + "%");
			}
		}
		else if(qName.equals("Name"))
		{
			if(this.inName && !this.inItem && !this.inFirstCompletedBy)
			{
				this.artifact.setName(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
				this.inName = false;
				this.multiLanguageText = null;
			}
		}
		else if(qName.equals("Description"))
		{
			if(this.inDescription)
			{
				this.artifact.setDescription(ListHelper.getExistingMLIfExist(this.multiLanguageText, this.listML));
				this.inDescription = false;
				this.multiLanguageText = null;
			}
		}
		else if(qName.equals("English"))
		{
			if((this.inName || this.inDescription) && !this.inItem && !this.inFirstCompletedBy)
				this.multiLanguageText.setEnglish(this.getBuffer());
			this.inEnglish = false;
		}
		else if(qName.equals("French"))
		{
			if((this.inName || this.inDescription) && !this.inItem && !this.inFirstCompletedBy)
				this.multiLanguageText.setFrench(this.getBuffer());
			this.inFrench = false;
		}
		else if(qName.equals("German"))
		{
			if((this.inName || this.inDescription) && !this.inItem && !this.inFirstCompletedBy)
				this.multiLanguageText.setGerman(this.getBuffer());
			this.inGerman = false;
		}
		else if(qName.equals("Level"))
			this.inLevel = false;
		else if(qName.equals("Difficulty"))
			this.inDifficulty = false;
		else if(qName.equals("Location"))
		{
			//this.artifact.setRequiredSkillPoints(new Integer(this.getBuffer()));
			this.inLocation = false;
		}
		else if(qName.equals("Items"))
		{
			//this.artifact.setHighUntil(new Integer(this.getBuffer()));
			this.inItems = false;
		}
		else if(qName.equals("Rewards"))
		{
			//this.artifact.setMediumUntil(new Integer(this.getBuffer()));
			this.inRewards = false;
		}
		else if(qName.equals("Guaranteed"))
		{
			//this.artifact.setLowUntil(new Integer(this.getBuffer()));
			this.inGuaranteed = false;
		}
		else if(qName.equals("Quantity"))
		{
			//this.artifact.setLowUntil(new Integer(this.getBuffer()));
			this.inQuantity = false;
		}
		else if(qName.equals("Reward"))
		{
			//this.artifact.setLowUntil(new Integer(this.getBuffer()));
			this.inReward = false;
		}
		else if(qName.equals("FirstCompletedBy"))
			this.inFirstCompletedBy = false;
		else
			if(!this.inFirstCompletedBy)
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
	
	public List<Artifact> getArtifact() throws SAXException
	{
		if(!this.isReady)
			throw new SAXException("Le parsing n'est pas fini");
			
		return this.artifactList;
	}
	
	private String getBuffer()
	{
		return this.buffer.toString();
	}
	
	private List<Artifact> artifactList; //résultats de notre parsing
	private Artifact artifact; // Objet en cours
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
	private boolean inLevel;
	private boolean inDifficulty;
	private boolean inLocation;
	private boolean inItems;
	private boolean inRewards;
	private boolean inGuaranteed;
	private boolean inReward;
	private boolean inItem;
		private boolean inItemKey;
		private boolean inQuantity;
	
	private boolean inFirstCompletedBy;
	
	private boolean isReady;
	
	private int count;
	private static final int NB_ITEM = 1100;
//	private static final int PERCENT = NB_ITEM / 100;
}
