package ch.correvon.rift.macro.robots;

import java.awt.Color;
import java.awt.Point;

import ch.correvon.rift.macro.windows.mainWindow.MainWindow;
import de.ksquared.system.keyboard.KeyEvent;

public class Robot1 extends _RobotExecutor
{
	public Robot1(String name, MainWindow mainWindow)
	{
		super(name, mainWindow);
	}

	@Override public void execute()
	{
		Color color = super.getPixelColor(POINT_8_8); // Appel du membre static

		switch(color.getRed())
		{
			case 49:

				this.robot.delay(1); /*	Role 4  */

				Color cut = super.getPixelColor(POINT_8_24);/*	Cut, cut, cut  */  // Appel du membre static
				
				if(cut.getRed() == 0)
				{
					super.hitKeySafe(KeyEvent.VK_0);
					
					this.robot.delay(21);
				}
				super.hitKey(KeyEvent.VK_1);

				break; /*	Fin Role 4  */
			case 250: /*	Role 5  */

				break; /*	Fin Role 5  */
			default: /*	No Role   */

		}
	}

	@Override public void exit()
	{

	}

	// Membres static, instenciés une seule fois
	private static final Point POINT_8_8 = new Point(8, 8);
	private static final Point POINT_8_24 = new Point(8, 24);
}
