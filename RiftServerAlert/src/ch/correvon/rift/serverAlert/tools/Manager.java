package ch.correvon.rift.serverAlert.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ch.correvon.rift.serverAlert.windows.mainWindow.MainWindow;
import ch.correvon.rift.serverAlert.xml.RiftServer;
import ch.correvon.rift.serverAlert.xml.ServerHandler;

import org.xml.sax.SAXException;

public class Manager
{
	public Manager(String adresse)
	{
		this(adresse, null);
	}
	
	public Manager(String adresse, MainWindow mainWindow)
	{
		this.defaultServer = null;
		this.mainWindow = mainWindow;
		this.readXML(adresse);
	}
	
	private void readXML(String adresse)
	{
		this.errorMsg = null;
		
		boolean ok = false;
		this.handler = null;
		this.timer = null;
		try
		{
			InputStream inputStream = this.getInputStream(adresse);
			SAXParser parser = this.getParser();
			this.handler = new ServerHandler();
		
			parser.parse(inputStream, this.handler);
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
			this.errorMsg = "Une erreur s'est produite lors du parsing de l'url, " +
					"veuillez consulter la console pour connaitre la raison";
			System.err.println(this.errorMsg);
		}
	}
	
	public String getErrorMsg()
	{
		return this.errorMsg;
	}

	private InputStream getInputStream(String adresse) throws MalformedURLException, IOException
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
	
	private SAXParser getParser() throws ParserConfigurationException, SAXException
	{
		SAXParserFactory fabrique = SAXParserFactory.newInstance();
		return fabrique.newSAXParser();
	}
	
	public void launch(String serverToCheck, int check, int recheck, RiftPopulationEnum minPopulation, int minQueue, Time timeFrom, Time timeTo, String wolIp, String wolMac, int wolPort)
	{
		if(this.errorMsg != null)
		{
			this.systray = new RiftSystray(this, this.errorMsg);
			return;
		}
		
		this.wolIp = wolIp;
		this.wolMac = wolMac;
		this.wolPort = wolPort;
		
		this.serverToCheck = serverToCheck;
		this.systray = new RiftSystray(this);
		this.timer = new RiftTimer(this, this.handler, serverToCheck, check, recheck, minPopulation, minQueue, timeFrom, timeTo);
		this.timer.start();
	}
	
	public void unknownServer()
	{
		this.systray.serverUnknown("Le serveur "+this.serverToCheck+" est introuvable");
	}
	
	public void serverStatusOk()
	{
		this.systray.ok();
	}
	
	public void setEnableTimer(boolean value)
	{
		this.timer.setEnable(value);
	}
	
	public void serverStatusAlert(boolean automaticAction)
	{
		RiftServer riftServer;
		try
		{
			riftServer = this.handler.getServer(this.serverToCheck);
		}
		catch(SAXException e)
		{
			e.printStackTrace();
			return;
		}
		this.systray.alert("Le serveur "+this.serverToCheck+" a atteint la population "+riftServer.getPopulation()+" (queue:"+riftServer.getQueued()+")", automaticAction);
		
		if(automaticAction && !this.isNullOrEmpty(this.wolIp) && !this.isNullOrEmpty(this.wolMac))
		{
			String errorMsg = WakeOnLan.sendWOL(this.wolIp, this.wolMac, this.wolPort);
			if(errorMsg != null)
				this.systray.serverUnknown("Impossible d'envoyer le paquet wake on lan. Cause : "+errorMsg);
		}
	}
	
	public void showWindow()
	{
		if(this.hasMainWindow())
			this.mainWindow.showWindow();
	}
	
	public boolean hasMainWindow()
	{
		return this.mainWindow != null;
	}
	
	public void release()
	{
		if(this.systray != null)
			this.systray.exit();
		if(this.timer != null)
			this.timer.exit();
	}
	
	public void exit(boolean exitMainWindow)
	{
		if(exitMainWindow && this.hasMainWindow())
			this.mainWindow.exit();
		this.release();
		System.exit(0);
	}

	public String getDefaultServer()
	{
		return defaultServer;
	}

	public void setDefaultServer(String defaultServer)
	{
		this.defaultServer = defaultServer;
	}

	public ServerHandler getHandler()
	{
		return handler;
	}

	public void setHandler(ServerHandler handler)
	{
		this.handler = handler;
	}

	public RiftTimer getTimer()
	{
		return timer;
	}

	public void setTimer(RiftTimer timer)
	{
		this.timer = timer;
	}

	public RiftSystray getSystray()
	{
		return systray;
	}

	public void setSystray(RiftSystray systray)
	{
		this.systray = systray;
	}
	
	private boolean isNullOrEmpty(String string)
	{
		return string == null || string.isEmpty();
	}
	
//	private String adresse;
	private String errorMsg;
	private String serverToCheck;
	private MainWindow mainWindow;
	
	private String wolIp;
	private String wolMac;
	private int wolPort;
	
	private String defaultServer;
	private ServerHandler handler;
	private RiftTimer timer;
	private RiftSystray systray;
}
