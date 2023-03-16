package ch.correvon.rift.fishing.tools;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import ch.correvon.rift.fishing.windows.baitWindow.LearningResultInterface;

public class LearningRobot extends Thread
{
	public LearningRobot(LearningResultInterface lri, int nbPoint, JLabel labelLearnTime)
	{
		try
		{
			this.robot = new Robot();
		}
		catch(AWTException e)
		{
			e.printStackTrace();
		}
		
		this.lri = lri;
		this.nbPoint = nbPoint;
		this.interval = 3;
		this.labelLearnTime = labelLearnTime;
	}
	
	@Override public void run()
	{
		Point point;
		boolean isLast;
		Timer timer = null;
		for(int i = 0; i < this.nbPoint; i++)
		{
			if(timer != null)
			{
				timer.cancel();
				this.timerTask.cancel();
			}
			timer = new Timer();
			this.timerTask = new MyTimerTask(this.interval);
			timer.schedule (this.timerTask, 0, 1000);

			this.robot.delay(this.interval * 1000);
			point = MouseInfo.getPointerInfo().getLocation();
			isLast = i+1 == this.nbPoint;
			this.lri.next(point, this.interval, isLast);
			this.timerTask.resetTime();
		}
		timer.cancel();
		this.timerTask.cancel();
	}
	
	public int getInterval()
	{
		return interval;
	}
	
	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	private LearningResultInterface lri;
	private Robot robot;
	private int interval;
	private int nbPoint;
	private MyTimerTask timerTask;
	private JLabel labelLearnTime;
	
	private class MyTimerTask extends TimerTask
	{
		public MyTimerTask(int restingTime)
		{
			this.restingTime = restingTime;
			this.initialRestingTime = restingTime;
		}
		
		public void resetTime()
		{
			this.restingTime = this.initialRestingTime;
		}
		
        @Override public void run()
        {
            labelLearnTime.setText(this.restingTime + " secondes");
            this.restingTime--;
        }
        
        private int restingTime;
        private int initialRestingTime;
	}
}
