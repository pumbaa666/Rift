package ch.correvon.rift.serverAlert.xml;

import java.util.LinkedList;
import java.util.List;

import ch.correvon.rift.serverAlert.tools.RiftPopulationEnum;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Tuto, voir http://java.developpez.com/faq/xml/?page=sax#parserSax
 * @author lco
 *
 */
public class ServerHandler extends DefaultHandler
{
	public ServerHandler()
	{
		super();
		this.isReady = false;
	}

	//détection d'ouverture de balise
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	throws SAXException
	{
		this.isReady = false;

		if(qName.equals("status"))
			this.serversList = new LinkedList<RiftServer>();
		else if(qName.equals("shard"))
		{
			this.server = new RiftServer();
			
			this.server.setName(attributes.getValue("name"));
			this.server.setOnline(Boolean.getBoolean(attributes.getValue("online")));
			this.server.setLocked(Boolean.getBoolean(attributes.getValue("locked")));
			this.server.setPopulation(RiftPopulationEnum.valueOf(attributes.getValue("population")));
			
			try
			{
				int queued = Integer.parseInt(attributes.getValue("queued"));
				this.server.setQueued(queued);
			}
			catch(Exception e)
			{
				throw new SAXException(e);
			}
			
			this.server.setLanguage(attributes.getValue("language"));
			this.server.setPvp(Boolean.getBoolean(attributes.getValue("pvp")));
			this.server.setRp(Boolean.getBoolean(attributes.getValue("rp")));
			this.server.setRecommend(Boolean.getBoolean(attributes.getValue("recommend")));
		}
		else
		{
			throw new SAXException("Balise " + qName + " inconnue.");
		}
	}

	//détection fin de balise
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException
	{
		if(qName.equals("status"))
		{
			
		}
		else if(qName.equals("shard"))
			this.serversList.add(this.server);
		else
			throw new SAXException("Balise "+qName+" inconnue.");
	}

	//détection de caractères
	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException
	{
		String lecture = new String(ch, start, length);
		if(this.buffer != null)
			this.buffer.append(lecture);
	}

	//début du parsing
	@Override public void startDocument() throws SAXException
	{
//		System.out.println("Début du parsing");
	}

	//fin du parsing
	@Override public void endDocument() throws SAXException
	{
		this.isReady = true;
	}
	
	public List<RiftServer> getServers() throws SAXException
	{
		if(!this.isReady)
			throw new SAXException("Le parsing n'est pas fini");
			
		return this.serversList;
	}
	
	public RiftServer getServer(String serverName) throws SAXException
	{
		if(!this.isReady)
			throw new SAXException("Le parsing n'est pas fini");
			
		for(RiftServer server:this.serversList)
			if(server.getName().equals(serverName))
				return server;
		return null;
	}

	private List<RiftServer> serversList; //résultats de notre parsing
	private RiftServer server; // Objet en cours
	private StringBuffer buffer; // buffer nous permettant de récupérer les données
	private boolean isReady;
}
