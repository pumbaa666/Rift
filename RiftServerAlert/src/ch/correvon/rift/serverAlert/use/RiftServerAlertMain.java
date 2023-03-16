package ch.correvon.rift.serverAlert.use;

import java.sql.Time;
import java.util.GregorianCalendar;

import ch.correvon.rift.serverAlert.tools.Manager;
import ch.correvon.rift.serverAlert.tools.RiftPopulationEnum;
import ch.correvon.rift.serverAlert.windows.mainWindow.MainWindow;

public class RiftServerAlertMain
{
// TODO :
//	- statistiques (avec graphique ?) sur les tranches horaires pleines

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args.length <= 1)
		{
			MainWindow mainWindow = new MainWindow("Rift Server Alert v0.3");
			mainWindow.run();
		}
		else
		{
			String adresse = null;
			String serverToCheck = null;
			int check = 5;
			int recheck = 15;
			RiftPopulationEnum minPopulation = RiftPopulationEnum.high;
			int minQueue = 0;
			Time timeFrom = null;
			Time timeTo = null;
			String wolIp = null;
			String wolMac = null;
			int wolPort = 0;
			
			String[] tabHourSplit;
			
			for(String arg:args)
			{
        		if(arg.contains("help"))
        		{
        			System.out.println("usage : RiftServerAlert url=[URL] server=[SERVER] check=[CHECK] recheck=[RECHECK] population=[POPULATION] queue=[QUEUE] timeFrom=[TIME_FROM] timeTo=[TIME_TO]\n" +
        					" - [URL] is the url of the xml server file. Ex : http://www.riftgame.com/en/status/eu-status.xml\n" +
        					" - [SERVER] is the name of the server you want to check. Ex : Rubicon\n" +
        					" - [CHECK] is the delay in minute between two check.\n" +
        					" - [RECHECK] is the delay in minute before next check after an alert.\n" +
        					" - [POPULATION] is the needed population on the server to rise an alert. Can be : low, medium, high, full\n" + 
        					" - [QUEUE] is the queue needed to rise an alert.\n" +
        					" - [TIME_FROM], [TIME_FROM] is the periode of time when the programme is activ. Ex : 16h00 [TO] 19h00");
        			return;
        		}
        		else if(arg.startsWith("url"))
        			adresse = getArgValue(arg);
        		else if(arg.startsWith("server"))
        			serverToCheck = getArgValue(arg);
        		else if(arg.startsWith("check"))
        		{
        			try
					{
        				check = new Integer(getArgValue(arg));
					}
					catch(NumberFormatException e)
					{
					}
        		}
        		else if(arg.startsWith("recheck"))
        		{
        			try
					{
        				recheck = new Integer(getArgValue(arg));
					}
					catch(NumberFormatException e)
					{
					}
        		}
        		else if(arg.startsWith("queue"))
        		{
        			try
					{
        				minQueue = new Integer(getArgValue(arg));
					}
					catch(NumberFormatException e)
					{
					}
        		}
        		else if(arg.startsWith("population"))
        			minPopulation = RiftPopulationEnum.valueOf(getArgValue(arg));
        		else if(arg.startsWith("timeFrom"))
        		{
        			tabHourSplit = getArgValue(arg).split("h");
    	        	if(tabHourSplit.length != 2)
    	        		System.err.println("L'heure de départ est incorrecte : "+arg);
    	        	else
    	        	{
    	        		GregorianCalendar gc = new GregorianCalendar(0, 0, 0, Integer.valueOf(tabHourSplit[0]), Integer.valueOf(tabHourSplit[1]), 0);
    	        		timeFrom = new Time(gc.getTimeInMillis());
    	        	}
        		}
        		else if(arg.startsWith("timeTo"))
        		{
        			tabHourSplit = getArgValue(arg).split("h");
    	        	if(tabHourSplit.length != 2)
    	        		System.err.println("L'heure de départ est incorrecte : "+arg);
    	        	else
    	        	{
    	        		GregorianCalendar gc = new GregorianCalendar(0, 0, 0, Integer.valueOf(tabHourSplit[0]), Integer.valueOf(tabHourSplit[1]), 0);
    	        		timeTo = new Time(gc.getTimeInMillis());
    	        	}
        		}
        		else if(arg.startsWith("WOLIP"))
        			wolIp = getArgValue(arg);
        		else if(arg.startsWith("WOLMAC"))
        			wolMac = getArgValue(arg);
        		else if(arg.startsWith("WOLPort"))
        		{
        			try
					{
						wolPort = new Integer(getArgValue(arg));
					}
					catch(NumberFormatException e)
					{
					}
        		}
			}
			
			Manager manager = new Manager(adresse);
			manager.launch(serverToCheck, check, recheck, minPopulation, minQueue, timeFrom, timeTo, wolIp, wolMac, wolPort);
		}
	}
	
	private static String getArgValue(String arg)
	{
		String[] split = arg.split("=", 2);
		if(split.length != 2)
		{
			System.err.println("L'argument suivant est incorrect : "+arg);
			return "";
		}
		
		return split[1];
	}
}
