package ch.correvon.rift.serverAlert.tools;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import ch.correvon.rift.serverAlert.xml.RiftServer;
import ch.correvon.rift.serverAlert.xml.ServerHandler;

import org.xml.sax.SAXException;

public class RiftTimer extends Thread
{
	public RiftTimer(Manager manager, ServerHandler handler, String serverToCheck, int check, int recheck, RiftPopulationEnum minPopulation, int minQueue, Time timeFrom, Time timeTo)
	{
		this.manager = manager;
		this.handler = handler;
		this.serverToCheck = serverToCheck;
		this.check = check * 60 * 1000;
		this.recheck = recheck * 60 * 1000;
		this.minPopulation = minPopulation;
		this.minQueue = minQueue;
		this.timeTo = timeTo;
		this.timeFrom = timeFrom;
		this.isEnable = true;
		this.exit = false;
	}
	
	public void exit()
	{
		this.exit = true;
	}
	
	public void setEnable(boolean value)
	{
		this.isEnable = value;
	}
	
	/**
	 * Run the listener.
	 * Check every TIMER millisecond if the value has changed
	 */
	@Override public void run()
	{
		int sleepTime = 0;
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
		if(riftServer == null)
		{
			this.manager.unknownServer();
			return;
		}

		do
		{
			this.mySleep(sleepTime);
			
			sleepTime = this.check;
			if(this.isEnable)
			{
				if(!this.isTime())
					continue;
				
				if(riftServer.getPopulation().compareTo(this.minPopulation) >= 0 && (this.minPopulation.compareTo(RiftPopulationEnum.high) < 0 || this.minPopulation.compareTo(RiftPopulationEnum.high) == 0 && riftServer.getQueued() >= this.minQueue))
				{
					this.manager.serverStatusAlert(true);
					sleepTime = this.recheck;
				}
				else
					this.manager.serverStatusOk();
			}
		}
		while(!this.exit);
	}
	
	private void mySleep(int sleepTime)
	{
		try
		{
			super.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
			System.err.println("Erreur pendant le sleep : ");
			e.printStackTrace();
		}
	}
	
	private boolean isTime()
	{
		Date dateNow = new Date();
		SimpleDateFormat hourFormat = new SimpleDateFormat("H");
		SimpleDateFormat minuteFormat = new SimpleDateFormat("m");
		GregorianCalendar gc = new GregorianCalendar(0, 0, 0, Integer.parseInt(hourFormat.format(dateNow)), Integer.parseInt(minuteFormat.format(dateNow)), 0);
		Time timeNow = new Time(gc.getTimeInMillis());
		
		return timeNow.after(this.timeFrom) && timeNow.before(this.timeTo);
	}

	private Manager manager;
	private ServerHandler handler;
	private String serverToCheck;
	private int check; // en minutes
	private int recheck; // en minutes
	private RiftPopulationEnum minPopulation;
	private int minQueue;
	private Time timeFrom;
	private Time timeTo;
	private boolean isEnable;
	private boolean exit;
}
