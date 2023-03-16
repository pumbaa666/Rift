package ch.correvon.rift.macro.robots;

import java.awt.Color;
import java.awt.Point;

import ch.correvon.rift.macro.windows.mainWindow.MainWindow;


public class Robot4 extends _RobotExecutor
{
	public Robot4(String name, MainWindow mainWindow)
	{
		super(name, mainWindow);
	}

	@Override public void execute()
	{
		Color color = super.getPixelColor(new Point(8,8));
		switch (color.getRed())
		{
		  case 49:
			this.robot.delay(1);  	/*	Role 4  */
	
			
			
		    break;	/*	Fin Role 4  */
		  case 250:  	/*	Role 5  */


			  
			  
			  
		    break;  /*	Fin Role 5  */
		  default:
			  /*	No Role   */
		}
	}

	@Override public void exit()
	{
		
	}
}
