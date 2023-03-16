package ch.correvon.rift.auctionHouse.tools;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import ch.correvon.rift.auctionHouse.interfaces.PrintLog;

public class MyRobot extends Robot
{
	public MyRobot(PrintLog mainWindow) throws AWTException
	{
		super();
		this.mainWindow = mainWindow;
	}
	
	public void setShortDelay(int value)
	{
		this.shortDelay = value;
	}
	
	@Override public void delay(int delay)
	{
		if(delay <= 0)
			return;
		this.mainWindow.printLog("delay "+delay);
		super.delay(delay);
	}
	
	@Override public void keyPress(int keyCode)
	{
		this.mainWindow.printLog("keyPress : "+KeyEvent.getKeyText(keyCode) + "("+keyCode+")");
		super.keyPress(keyCode);
	}
	
	public void altTab()
	{
		this.keyPress(KeyEvent.VK_ALT);
		this.delay(this.shortDelay);
		this.keyPress(KeyEvent.VK_TAB);
		this.delay(this.shortDelay);
		super.keyRelease(KeyEvent.VK_TAB);
		super.keyRelease(KeyEvent.VK_ALT);
	}
	
	public void shiftHome()
	{
		// Il y a un bug dans la jvm qui fait que la touche shift ne peut pas rester enfoncée si la touche Num Lock est activée.
		// Il faut donc contourner ce problème
		// Source : http://forums.oracle.com/forums/thread.jspa?threadID=2230592
		
//		boolean numLock = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
//		if(numLock)
//		{
//			Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
//			new NumLockThread().start();
//		}

		//Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
				
		this.keyPress(KeyEvent.VK_SHIFT);
		this.delay(this.shortDelay);
		this.keyPress(KeyEvent.VK_HOME);
		this.delay(this.shortDelay);
		super.keyRelease(KeyEvent.VK_HOME);
		super.keyRelease(KeyEvent.VK_SHIFT);

//		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, true); // Encore un bug qui empêche de remettre le Num Lock dans son état initial.
	}
	
	public void ctrlV()
	{
		this.keyPress(KeyEvent.VK_CONTROL);
		this.delay(this.shortDelay);
		this.keyPress(KeyEvent.VK_V);
		this.delay(this.shortDelay);
		super.keyRelease(KeyEvent.VK_V);
		super.keyRelease(KeyEvent.VK_CONTROL);

		this.keyPress(KeyEvent.VK_BACK_SPACE);
		this.delay(this.shortDelay);
		super.keyRelease(KeyEvent.VK_BACK_SPACE);
	}
	
	private PrintLog mainWindow;
	private int shortDelay;
}
