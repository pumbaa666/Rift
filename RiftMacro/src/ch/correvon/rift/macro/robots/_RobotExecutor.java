package ch.correvon.rift.macro.robots;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.correvon.rift.macro.windows.mainWindow.MainWindow;

public abstract class _RobotExecutor extends Thread
{
	/* ------------------------------------------------------ *\
	|* 						Constructor						  *|
	\* ------------------------------------------------------ */
	public _RobotExecutor(String name, MainWindow mainWindow)
	{
		this.name = name;
		this.mainWindow = mainWindow;
		//this.printLog("Création d'un nouveau RobotExecutor '" + name + "'");
		
		try
		{
			this.robot = new Robot();
		}
		catch(AWTException e)
		{
			this.printError("échec de la création du RobotExecutor '" + name + "'");
			e.printStackTrace();
		}
	}
	
	/* ------------------------------------------------------ *\
	|* 					Abstract methods					  *|
	\* ------------------------------------------------------ */
	abstract public void execute();
	abstract public void exit();
	
	/* ------------------------------------------------------ *\
	|* 					   Public methods					  *|
	\* ------------------------------------------------------ */
	@Override public void run()
	{
		double avg;
		long start = new Date().getTime();
		//this.printLog("Démarrage du RobotExecutor '" + this.name + "' à " + getCurrentTime());
		this.execute();
		//this.printLog("Fin du RobotExecutor '" + this.name + "' à " + getCurrentTime());
		long stop = new Date().getTime();
		avg = stop - start;
		
		avg_time += avg;
		nb++;
		
		System.out.println("Fin du RobotExecutor '" + this.name + "' à " + avg + " ms (avg = " + (avg_time/nb) + ")");
	}
	
	/* ------------------------------------------------------ *\
	|* 					  Protected methods					  *|
	\* ------------------------------------------------------ */
	protected void printLog(String str)
	{
		this.mainWindow.printLog(str);
	}
	
	protected void printError(String str)
	{
		this.mainWindow.printError(str);
	}
	
	/* --------------------------------------- *\
	|* 			    Printscreen 			   *|
	\* --------------------------------------- */
	protected BufferedImage localPrintScreen(Point center)
	{
		return this.robot.createScreenCapture(new Rectangle(center.x, center.y, 1, 1));
	}
	
	/**
	 * Do a printscreen around a point
	 * 
	 * @param center
	 * @param dx "radius" of the printscreen
	 * @param dy "radius" of the printscreen
	 * 
	 * @return the printscreen
	 */
	protected BufferedImage localPrintScreen(Point center, int dx, int dy)
	{
		return this.robot.createScreenCapture(new Rectangle(center.x-dx, center.y-dy, dx*2, dy*2));
	}
	
	protected Color getPixelColor(Point center)
	{
		BufferedImage img = this.localPrintScreen(center);
		return new Color(img.getRGB(0, 0));
	}
	
	/* --------------------------------------- *\
	|* 			       KeyPress	 			   *|
	\* --------------------------------------- */
	/**
	 * The robot hit a key and release it
	 * @param keyCode
	 */
	protected void hitKey(Integer keyCode)
	{
		this.robot.keyPress(keyCode);
		this.robot.keyRelease(keyCode);
	}
	
	/**
	 * The robot hit a key and release it.
	 * If the key hit should rise a bot, it doesn't.
	 * Used to prevent recursive invocation a robots.
	 * 
	 * @param keyCode
	 */
	protected void hitKeySafe(Integer keyCode)
	{
		lockKey(keyCode);
		this.hitKey(keyCode);
	}
	
	/* ------------------------------------------------------ *\
	|*   				  Static methods					  *|
	\* ------------------------------------------------------ */
	private static String getCurrentTime()
	{
		return SDF.format(new Date());
	}
	
	/* --------------------------------------- *\
	|* 				    Lock    			   *|
	\* --------------------------------------- */
	/**
	 * Lock a key.
	 * If the key hit should rise a bot, it doesn't.
	 * Used to prevent recursive invocation a robots.
	 * 
	 * The MyKeyAdapter will unlock the key with the consume method, do not care about it.
	 * 
	 * @param keyCode
	 */
	synchronized protected static void lockKey(Integer keyCode)
	{
		keysLocked.add(keyCode);
	}
	
	/**
	 * Consume a key locked by lockKey.
	 * The MyKeyAdapter will unlock the key, you shouldn't invoke this method yourself.
	 * 
	 * @param keyCode
	 */
	synchronized public static void consume(Integer keyCode)
	{
		Integer keyToRemove = null;
		for(Integer key:keysLocked)
		{
			if(key.equals(keyCode))
			{
				keyToRemove = key;
				break;
			}
		}
		
		if(keyToRemove != null)
			keysLocked.remove(keyToRemove);
	}
	
	/**
	 * Test if a key is locked.
	 * The MyKeyAdapter will test it for you, you shouldn't invoke this method yourself.
	 * 
	 * @param keyCode
	 * @return
	 */
	synchronized public static boolean isKeyLocked(Integer keyCode)
	{
		for(Integer key:keysLocked)
			if(key.equals(keyCode))
				return true;
		return false;
	}
	
	/* ------------------------------------------------------ *\
	|*   				  Attributes						  *|
	\* ------------------------------------------------------ */
	protected Robot robot;
	private String name;
	private MainWindow mainWindow;
	
	/* --------------------------------------- *\
	|* 				   Static	 			   *|
	\* --------------------------------------- */
	private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.S");
	private static final List<Integer> keysLocked = new ArrayList<>(10);
	
	private static double avg_time = 0;
	private static int nb = 0;
}
