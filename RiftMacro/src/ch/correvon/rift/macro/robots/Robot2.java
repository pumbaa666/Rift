package ch.correvon.rift.macro.robots;

import java.awt.Color;
import java.awt.Point;

import ch.correvon.rift.macro.windows.mainWindow.MainWindow;
import de.ksquared.system.keyboard.KeyEvent;

public class Robot2 extends _RobotExecutor
{
	public Robot2(String name, MainWindow mainWindow)
	{
		super(name, mainWindow);
	}

	@Override public void execute()
	{

		Color color = super.getPixelColor(new Point(8, 8));
		switch(color.getRed())
		{
			case 49: /*	Role 4  */
				Color cut = super.getPixelColor(new Point(8, 24));/*	Cut, cut, cut  */
				if(cut.equals(Color.WHITE))
				{
					Color flinch = super.getPixelColor(new Point(8, 40));/*	Cut, cut, cut  */
					if(flinch.equals(Color.WHITE))
					{
						super.hitKeySafe(KeyEvent.VK_0);
						this.robot.delay(9);
					}
				}
				Color sftbld = super.getPixelColor(new Point(24, 8));
				switch(sftbld.getRed())
				{

					case 255:/*	Shifting Blade UP   */
						Color cfocus = super.getPixelColor(new Point(24, 24));
						switch(cfocus.getRed())
						{
							case 255:/*	combat focus up   */
								this.robot.delay(5);
								super.hitKeySafe(KeyEvent.VK_6);
								this.robot.delay(652);
								super.hitKeySafe(KeyEvent.VK_6);
								break;
							default:
								this.robot.delay(1);
								super.hitKeySafe(KeyEvent.VK_2);/*	Combat focus pas UP   */
						}

						break;
					default:
						this.robot.delay(3);
						super.hitKeySafe(KeyEvent.VK_2);/*	Shifting Blade pas UP   */
				}

				break; /*	Fin Role 4  */
			case 255: /*	Role 5  */
				Color cut2 = super.getPixelColor(new Point(8, 24));/*	Cut, cut, cut  */
				if(cut2.equals(Color.WHITE))
				{
					super.hitKeySafe(KeyEvent.VK_0);
					this.robot.delay(31);
				}
				Color builder = super.getPixelColor(new Point(24, 8));
				switch(builder.getRed())
				{
					case 255:/*	OtStream ou    */
						super.hitKeySafe(KeyEvent.VK_1);
						break;
					case 0:/*	pas 3 Cut    */
						super.hitKeySafe(KeyEvent.VK_2);
						break;
					case 49: /*	 svgsweap plus up */
						super.hitKeySafe(KeyEvent.VK_3);
						break;
					default:/*	 Dt/ss */
						super.hitKeySafe(KeyEvent.VK_4);

				}
				break; /*	Fin Role 5  */
			default:
				/*	No Role   */
				Color color3 = super.getPixelColor(new Point(8, 24));
				System.out.println(" / color : [r = " + color3.getRed() + ", g = " + color3.getGreen() + ", b = " + color3.getBlue() + "]");
		}

	}

	@Override public void exit()
	{

	}
}
