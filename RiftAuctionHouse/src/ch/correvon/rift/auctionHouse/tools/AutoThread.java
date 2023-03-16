package ch.correvon.rift.auctionHouse.tools;

import ch.correvon.rift.auctionHouse.interfaces.ProcessAuto;

public class AutoThread extends Thread
{
	public AutoThread(/*MyRobot robot, */ProcessAuto mainWindow)
	{
//		this.robot = robot;
		this.mainWindow = mainWindow;
//		this.delay = delay;
	}

	@Override public void run()
	{
		while(true)
		{
			if(this.run)
			{
				this.mainWindow.process();
//				this.robot.altTab();
			}
			try
			{
				super.sleep(this.delay);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void setRun(boolean value)
	{
		this.run = value;
	}

//	public int getDelay()
//	{
//		return delay;
//	}
//	
	public void setDelay(int delay)
	{
		this.delay = delay;
	}

//	private MyRobot robot;
	private ProcessAuto mainWindow;
	private boolean run = false;
	private int delay = 100;
}
