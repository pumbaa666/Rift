package ch.correvon.rift.parsing.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser
{
	public static DefaultHandler readXML(String adresse, Class<? extends DefaultHandler> handlerClass)
	{
		boolean ok = false;
		DefaultHandler handler = null;
		try
		{
			InputStream inputStream = getInputStream(adresse);
			
			SAXParser parser = getParser();
			handler = handlerClass.newInstance();
		
			parser.parse(inputStream, handler);
			
			ok = true;
		}
		catch(SAXException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(!ok)
		{
			String errorMsg = "Une erreur s'est produite lors du parsing de l'url " + 
				adresse + ", " + "veuillez consulter la console pour connaitre la raison";
			System.err.println(errorMsg);
			return null;
		}
		
		return handler;
	}
	
	private static InputStream getInputStream(String adresse) throws MalformedURLException, IOException
	{
		URL url;
		try
		{
			url = new URL(adresse);
		}
		catch(MalformedURLException e)
		{
			return null;
		}
		URLConnection uc = url.openConnection();
		return uc.getInputStream();
	}
	
	private static SAXParser getParser() throws ParserConfigurationException, SAXException
	{
		SAXParserFactory fabrique = SAXParserFactory.newInstance();
		return fabrique.newSAXParser();
	}
}
