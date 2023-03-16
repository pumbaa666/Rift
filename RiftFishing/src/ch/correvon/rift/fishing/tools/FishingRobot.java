package ch.correvon.rift.fishing.tools;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import ch.correvon.pumbaaUtils.helpers.ClipBoardHelper;
import ch.correvon.rift.fishing.windows.baitWindow.Bait;
import ch.correvon.rift.fishing.windows.mainWindow.LocationColor;
import ch.correvon.rift.fishing.windows.mainWindow.MainWindow;

public class FishingRobot extends Thread
{
	public FishingRobot(LocationColor locationColor, MainWindow mainWindow, Bait bait, Point fishstick, int nbBaitToUse, boolean confirmation)
	{
		this.mainWindow = mainWindow;
		this.terminated = false;
		try
		{
			this.robot = new Robot();
			this.mainWindow.printLog("Robot créé", this.mainWindow.logStyleBlack, true, true);
		}
		catch(AWTException e)
		{
			e.printStackTrace();
		}
		this.exit = false;
		this.locationColor = locationColor;
		this.mousemoveX = 0;
		this.mousemoveY = 0;
		
		this.maximumDuration = 0;
		this.maximumFish = 0;
		this.currentFish = 0;
		this.maximumFail = 0;
		this.currentFail = 0;
		this.closeBot = false;
		this.saveLog = false;
		this.pressKey = -1;
		this.shutdownComputer = false;
		this.exitRift = false;
		
		this.bait = bait;
		this.fishstick = new Point(fishstick);
		this.nbBaitUsed = 0;
		this.nbBaitToUse = nbBaitToUse;
		
		this.confirmation = confirmation;
	}
	
	/* ------------ Start panel ------------ */
	public void setFishingButton(int value)
	{
		this.fishingButton = value;
	}
	
	public void setStartWaitingTime(int startWaitingTime)
	{
		this.startWaitingTime = startWaitingTime;
	}
	
	public void setMouseMove(int x, int y)
	{
		this.mousemoveX = x;
		this.mousemoveY = y;
	}
	
	public void setLootButtonColor(Color color)
	{
		this.lootButtonColorLower = new Color(color.getRed()-5, color.getGreen()-10, color.getBlue()-10);
		this.lootButtonColorHigher = new Color(color.getRed()+5, color.getGreen()+10, color.getBlue()+10);
	}
	
	public void exit()
	{
		this.mainWindow.activateRunButton(false);
		this.exit = true;
	}
	
	/* ------------ Stop panel ------------ */
	public void setMaximumDuration(double duration)
	{
		this.maximumDuration = duration;
	}
	
	private boolean checkDuration()
	{
		if(this.maximumDuration == 0)
			return true;
		
		if(System.currentTimeMillis() > this.startTime + this.maximumDuration)
		{
			this.mainWindow.printLog("Arrêt programmé selon l'heure", this.mainWindow.logStyleOrange, true, true);
			return false;
		}
		return true;
	}
	
	public void setMaximumFish(int fish)
	{
		this.maximumFish = fish;
	}

	private boolean checkFish()
	{
		if(this.maximumFish == 0)
			return true;
		
		if(this.currentFish >= this.maximumFish)
		{
			this.mainWindow.printLog("Arrêt programmé après " + this.maximumFish + " poissons pêchés", this.mainWindow.logStyleOrange, true, true);
			return false;
		}
		return true;
	}
	
	public void setMaximumFail(int fail)
	{
		this.maximumFail = fail;
	}
	
	private boolean checkFail()
	{
		if(this.maximumFail == 0)
			return true;
		
		if(this.currentFail >= this.maximumFail)
		{
			this.mainWindow.printLog("Arrêt programmé après " + this.maximumFail + " échecs", this.mainWindow.logStyleOrange, true, true);
			return false;
		}
		return true;
	}
	
	public void setCloseBot(boolean close)
	{
		this.closeBot = close;
	}
	
	public void setPressKey(String code)
	{
		Integer i;
		try
		{
			i = new Integer(code);
			this.pressKey = i;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			this.pressKey = -1;
		}
	}
	
	public void setSaveLog(boolean save)
	{
		this.saveLog = save;
	}
	
	public void setShutdownComputer(boolean shutdown)
	{
		this.shutdownComputer = shutdown;
	}
	
	public void setExitRift(boolean exitRift)
	{
		this.exitRift = exitRift;
	}
	
	public boolean isTerminated()
	{
		return this.terminated;
	}
	
	@Override public void run()
	{
		int initMouseX = 0;
		int initMouseY = 0;

		BufferedImage image;
		
		int moyenneRed;
		int moyenneGreen;
		int moyenneBlue;
		
		boolean toutPrendre;
		Color moyenneColor = new Color(0, 0, 0);
		
		this.mainWindow.printLog("début de la macro", this.mainWindow.logStyleBlack, true, true);
		this.startTime = System.currentTimeMillis();
		this.lastFail = 99;
		try
		{
			super.sleep(this.startWaitingTime); // Attends X seconde, le temps de placer la souris où on veut.
			this.leftClick("gauche pour focus Rift");
			Point mousePoint = MouseInfo.getPointerInfo().getLocation(); // Sauvegarde l'emplacement initial de la souris pour se déplacer autour à chaque poisson pêché
			initMouseX = mousePoint.x;
			initMouseY = mousePoint.y;
			Point newPlace = new Point(initMouseX, initMouseY);

			this.applyBait(true);
			this.robot.mouseMove(initMouseX, initMouseY); // replace la souris après avoir utilisé l'appât

			boolean splashResult;
			boolean sens = true;
			while(!this.exit)
			{
				toutPrendre = false;
				
				this.pressKey(this.fishingButton);
				this.robot.delay(this.getRandomDelay(400, 600));
				
				this.leftClick();
				this.robot.delay(this.getRandomDelay(3500, 4500));

				deleteFileInDirectory(new File("images"));
				splashResult = this.waitUntilSplash(newPlace, 0, 3);
				if(!splashResult)
				{
					this.mainWindow.fishCaught(0);
					this.currentFail++;
					this.lastFail = 0;
					if(this.exit || !this.checkFail()) 
						break;
				}

				this.robot.mouseMove(newPlace.x, newPlace.y);
				this.robot.delay(this.getRandomDelay(50, 75));
				this.leftClick("gauche après 1er remous");
				
				/* --- Tout prendre ? --- */
				int countSplash = 2;
				while(!toutPrendre && !this.exit && splashResult)
				{
					this.robot.delay(this.getRandomDelay(1000, 1500));
					image = this.localPrintScreen(newPlace);
					this.saveToJpg(1, countSplash, image);
					
					moyenneColor = parseImage(image);
					moyenneRed = moyenneColor.getRed();
					moyenneGreen = moyenneColor.getGreen();
					moyenneBlue = moyenneColor.getBlue();
					this.mainWindow.printLog("Tout prendre : " + moyenneRed + ", " + moyenneGreen + ", " + moyenneBlue, this.mainWindow.logStyleBlack, true, true);

					if(	compareColorComponantGreater(new Color(moyenneRed, moyenneGreen, moyenneBlue), this.lootButtonColorLower) &&
						compareColorComponantLower(new Color(moyenneRed, moyenneGreen, moyenneBlue), this.lootButtonColorHigher))
					{
						this.mainWindow.fishCaught(countSplash-1);
						this.lastFail++;
						this.mainWindow.printLog("Poisson pêché après " + (countSplash-1) + " remous", this.mainWindow.logStyleBlue, true, true);
						this.currentFish++;
						this.currentFail = 0;
						toutPrendre = true;
					}
	
					if(!toutPrendre)
					{
						this.mainWindow.printLog("Pas trouvé Tout prendre, attends un nouveau remous", this.mainWindow.logStyleLightBlue, true, true);
	
						splashResult = this.waitUntilSplash(newPlace, countSplash, 2);
						if(!splashResult)
						{
							break;
						}
						this.robot.mouseMove(newPlace.x, newPlace.y);
						this.robot.delay(this.getRandomDelay(50, 75));
						this.leftClick("gauche après remous n° " + countSplash);
					}
					countSplash++;
					if(countSplash > 5) // Garde-fou n°2
						break;
				}

				this.leftClick();
				this.robot.delay(this.getRandomDelay(200, 400));
				
				this.applyBait(false);
				
				newPlace = this.mousemove(initMouseX, initMouseY, this.mousemoveX, this.mousemoveY, sens);
				sens = !sens;
				
				if(!this.exit)
				{
					if(!this.checkDuration()) 
						break;
					if(!this.checkFish()) 
						break;
					if(!this.checkFail()) 
						break;
				}
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		if(!this.exit)
		{
			if(this.pressKey >= 0)
			{
				this.mainWindow.printLog("Presse la touche code " + this.pressKey, this.mainWindow.logStyleOrange, true, true);
				this.robot.keyPress(this.pressKey);
				this.mainWindow.printLog("Attends 30 secondes", this.mainWindow.logStyleOrange, true, true);
				this.robot.delay(30000);
			}
			if(this.exitRift)
			{
				this.mainWindow.printLog("Extinction de Rift", this.mainWindow.logStyleOrange, true, true);
				
				String oldClipboard = ClipBoardHelper.readClipboard();
	
				// Enter
				this.robot.keyPress(KeyEvent.VK_ENTER);
				this.robot.delay(500);
				this.robot.keyRelease(KeyEvent.VK_ENTER);
				this.robot.delay(500);
				
				// Copie /quit dans le presse-papier
				ClipBoardHelper.writeClipboard("/quit");
				
				// Presse ctrl
				this.robot.keyPress(KeyEvent.VK_CONTROL);
				this.robot.delay(500);
	
				// V
				this.robot.keyPress(KeyEvent.VK_V);
				this.robot.delay(50);
				this.robot.keyRelease(KeyEvent.VK_V);
				this.robot.delay(100);
	
				// Lâche ctrl
				this.robot.keyRelease(KeyEvent.VK_CONTROL);
				this.robot.delay(500);
				
				// Enter
				this.robot.keyPress(KeyEvent.VK_ENTER);
				this.robot.delay(500);
				this.robot.keyRelease(KeyEvent.VK_ENTER);
	
				ClipBoardHelper.writeClipboard(oldClipboard);
			}
			if(this.shutdownComputer)
			{
				this.mainWindow.printLog("Extinction de l'ordinateur programmée", this.mainWindow.logStyleOrange, true, true);
				this.mainWindow.shutdownComputer();
			}
			if(this.saveLog)
				this.mainWindow.saveLog();
			if(this.closeBot)
				this.mainWindow.exit();
		}
		
		this.mainWindow.printLog("fin de la macro", this.mainWindow.logStyleBlack, true, true);
		this.mainWindow.activateRunButton(true);
		this.terminated = true;
	}
	
	private void applyBait(boolean firstUse)
	{
		if(this.bait == null)
			return;
		
		if(!firstUse) // Lors de la 1ère application de l'appât sur la canne, ne compte pas comme une utilisation
		{
			boolean canUseAgain = this.bait.use();
			if(canUseAgain) // Il reste du temps ou des utilisations, donc on n'applique pas de nouvel appât1
			{
				this.mainWindow.printLog("Appât : " + this.bait.getRestingTime(), this.mainWindow.logStyleGreen, true, true);
				return;
			}
		}

		if(this.lastFail < 10) // N'applique pas d'appât si il y a eu un echec lors des 10 derniers poissons pêchés
		{
			this.mainWindow.printLog("N'applique pas d'appât car il y a eu un echec " + this.lastFail + " poissons en arrière", this.mainWindow.logStyleOrange, true, true);
			return;
		}
		
		this.bait.reset();
		if(this.nbBaitUsed >= this.nbBaitToUse)
		{
			this.bait = null;
			return;
		}
		this.nbBaitUsed++;
		this.mainWindow.printLog("Utilisation d'un nouvel appât (" + this.nbBaitUsed + "/" + this.nbBaitToUse + ")", this.mainWindow.logStyleGreen, true, true);
		
		this.mainWindow.printLog("bouge sur l'appat", this.mainWindow.logStyleLightGreen, true, true);
		this.robot.delay(this.getRandomDelay(200, 400));
		this.robot.mouseMove(this.bait.getX(), this.bait.getY());
		this.robot.delay(this.getRandomDelay(200, 400));
		this.rightClick();
		this.robot.delay(this.getRandomDelay(200, 400));
		this.mainWindow.printLog("bouge sur la canne", this.mainWindow.logStyleLightGreen, true, true);
		this.robot.mouseMove(this.fishstick.x, this.fishstick.y);
		this.robot.delay(this.getRandomDelay(400, 600));
		this.leftClick();
		this.robot.delay(this.getRandomDelay(3500, 4000));
	}
	
	private Point mousemove(int initMouseX, int initMouseY, int mousemoveX, int mousemoveY, boolean moveRight)
	{
		int moveX = 0;
		int moveY = 0;
		int sens = 1;
		if(!moveRight)
			sens = -1;
		
		int x = initMouseX;
		if(mousemoveX > 0)
			moveX = random.nextInt(mousemoveX);
		moveX = sens * moveX;
		x += moveX;

		int y = initMouseY;
		if(mousemoveY > 0)
			moveY = random.nextInt(mousemoveY);
		moveY = sens * moveY;
		y += moveY;
		
		this.mainWindow.printLog("Déplacement de la souris " +
								"(x=" + x + ", y=" + y + ") / " +
								"(dx=" + moveX + ", dy=" + moveY + ")",
								this.mainWindow.logStyleLightBlue, true, true);
		this.robot.mouseMove(x, y);
		
		return new Point(x, y);
	}
	
	private boolean waitUntilSplash(Point newPlace, int countSplash, int nbSure)
	{
		int moyenneRed;
		int moyenneGreen;
		int moyenneBlue;
		
		double pourcentDiffRed;
		double pourcentDiffGreen;
		double pourcentDiffBlue;

		int count = 0;
		double sumRed = 0;
		double sumGreen = 0;
		double sumBlue = 0;
		double moyenneMoyenneRed = 0;
		double moyenneMoyenneGreen = 0;
		double moyenneMoyenneBlue = 0;
		Color moyenneColor = new Color(0, 0, 0);
		BufferedImage image;
		
		int sure = 0;

		do
		{
			image = this.localPrintScreen(newPlace);
			this.saveToJpg(countSplash, count, image);

			moyenneRed = moyenneColor.getRed();
			moyenneGreen = moyenneColor.getGreen();
			moyenneBlue = moyenneColor.getBlue();
			
			moyenneMoyenneRed = sumRed / count;
			moyenneMoyenneGreen = sumGreen / count;
			moyenneMoyenneBlue = sumBlue / count;
			
			pourcentDiffRed = (moyenneRed - moyenneMoyenneRed) / moyenneRed * 100;
			pourcentDiffGreen = (moyenneGreen - moyenneMoyenneGreen) / moyenneGreen * 100;
			pourcentDiffBlue = (moyenneBlue - moyenneMoyenneBlue) / moyenneBlue * 100;
			
			/* --- Remous ? --- */
			this.mainWindow.printLog("current (" + (count-1) + ") : " 
					+ moyenneRed + ", " + moyenneGreen + ", " + moyenneBlue + " | "
					+ (int)pourcentDiffRed + ", " + (int)pourcentDiffGreen + ", " + (int)pourcentDiffBlue
					, this.mainWindow.logStyleGray, true, true);
//			System.out.printf("current (%d): %d, %d, %d | %.2f, %.2f, %.2f\n",
//					count-1,
//					moyenneRed, moyenneGreen, moyenneBlue,
//					pourcentDiffRed, pourcentDiffGreen, pourcentDiffBlue);
			if(count > 0 && compareColorComponantGreater(this.normalizeColor(pourcentDiffRed, pourcentDiffGreen, pourcentDiffBlue), this.locationColor.getColor()))
			{
				sure++;
				if(!this.confirmation || sure >= nbSure)
				{
					this.mainWindow.printLog("Remous " + countSplash, this.mainWindow.logStyleLightBlue, true, true);
					return true;
				}
			}
			else
				sure = 0;
			moyenneColor = parseImage(image);
			sumRed += moyenneColor.getRed();
			sumGreen += moyenneColor.getGreen();
			sumBlue += moyenneColor.getBlue();
			this.robot.delay(175);
			count++;
			if(count > 100)
				return false;
		}
		while(!this.exit);
			return false;
	}
	
	private BufferedImage localPrintScreen(Point center)
	{
		return localPrintScreen(center, 40, 20);
	}
	
	private BufferedImage localPrintScreen(Point center, int dx, int dy)
	{
		return this.robot.createScreenCapture(new Rectangle(center.x-dx, center.y-dy, dx*2, dy*2));
	}
	
//	private BufferedImage mouseLocalPrintScreen(int dx, int dy)
//	{
//		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
//		int mouseX = mousePoint.x;
//		int mouseY = mousePoint.y;
//		return this.robot.createScreenCapture(new Rectangle(mouseX-dx, mouseY-dy, dx*2, dy*2));
//	}
	
	private void saveToJpg(int splashNumber, int count, BufferedImage image)
	{
		if(!DEBUG)
			return;
		File imageFile = new File("images/test_" + splashNumber + " " + getPadding(count) + ".jpg");
		try
		{
			ImageIO.write(image, "jpg", imageFile);
			this.mainWindow.printLog("printscreen_remous_" + splashNumber + " " + count + " : ", this.mainWindow.logStyleGray, true, false);
		}
		catch(IOException e)
		{
			this.mainWindow.printLog("Impossible de faire un printscreen\n"+e.getMessage(), this.mainWindow.logStyleRed, true, true);
			e.printStackTrace();
		}
	}
	
	private Color parseImage(BufferedImage image)
	{
		double nbPix = image.getWidth() * image.getHeight();
		int pixel = image.getRGB(0, 0);
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = pixel & 0xff;
		double moyenneRed = 0;
		double moyenneGreen = 0;
		double moyenneBlue = 0;
		
		for(int x = 0; x < image.getWidth(); x++)
		{
			for(int y = 0; y < image.getHeight(); y++)
			{
				pixel = image.getRGB(x, y);
				
				red = (pixel >> 16) & 0xff;
				moyenneRed += red;
				
				green = (pixel >> 8) & 0xff;
				moyenneGreen += green;
				
				blue = pixel & 0xff;
				moyenneBlue += blue;
			}
		}
		moyenneRed /= nbPix;
		moyenneGreen /= nbPix;
		moyenneBlue /= nbPix;
		
		return new Color((int)moyenneRed, (int)moyenneGreen, (int)moyenneBlue);
	}
	
	private void leftClick()
	{
		this.leftClick("gauche");
	}
	
	private void leftClick(String str)
	{
		this.click(str, InputEvent.BUTTON1_MASK);
	}
	
	private void rightClick()
	{
		this.rightClick("droite");
	}
	
	private void rightClick(String str)
	{
		this.click(str, InputEvent.BUTTON3_MASK);
	}
	
	private void click(String str, int button)
	{
		this.robot.mousePress(button);
		this.mainWindow.printLog("click " + str, this.mainWindow.logStyleBlack, true, false);
		this.robot.delay(this.getRandomDelay(100, 150));
		this.robot.mouseRelease(button);
		this.mainWindow.printLog(" / lâche " +str, this.mainWindow.logStyleBlack, false, true);
	}
	
	private void pressKey(int key)
	{
		this.robot.keyPress(key);
		this.mainWindow.printLog("presse " + KeyEvent.getKeyText(key), this.mainWindow.logStyleBlack, true, false);
		this.robot.delay(this.getRandomDelay(100, 150));
		this.robot.keyRelease(key);
		this.mainWindow.printLog(" / lâche " + KeyEvent.getKeyText(key), this.mainWindow.logStyleBlack, false, true);
	}
	
	private String getPadding(int number)
	{
		if(number < 10)
			return "00"+number;
		if(number < 100)
			return "0"+number;
		return ""+number;
	}

	private int getRandomDelay(int minDelay, int maxDelay)
	{
		return random.nextInt(maxDelay - minDelay) + minDelay;
	}
	
	private static void deleteFileInDirectory(File directory)
	{
		if(!DEBUG)
			return;
		File[] fileList = directory.listFiles();
		for(int i = 0; i < fileList.length; i++)
		{
			if(fileList[i].isDirectory())
			{
				deleteFileInDirectory(fileList[i]);
				fileList[i].delete();
			}
			else
				fileList[i].delete();
		}
	}
	
	/**
	 * Return true if every componant of color1 > color2
	 * @param color1
	 * @param color2
	 * @return
	 */
	private static boolean compareColorComponantGreater(Color color1, Color color2)
	{
		return color1.getRed() >= color2.getRed() &&
		 	color1.getGreen() >= color2.getGreen() &&
		 	color1.getBlue() >= color2.getBlue();
	}
	
	/**
	 * Return true if every componant of color1 < color2
	 * @param color1
	 * @param color2
	 * @return
	 */
	private static boolean compareColorComponantLower(Color color1, Color color2)
	{
		return color1.getRed() <= color2.getRed() &&
		 	color1.getGreen() <= color2.getGreen() &&
		 	color1.getBlue() <= color2.getBlue();
	}
	
	private Color normalizeColor(double r, double g, double b)
	{
		int rf = (int)r;
		int gf = (int)g;
		int bf = (int)b;
		
		if(rf < 0)
			rf = 0;
		if(gf < 0)
			gf = 0;
		if(bf < 0)
			bf = 0;
		
		return new Color(rf, gf, bf);
	}

	private Robot robot;
	private boolean exit;
	private MainWindow mainWindow;
	private boolean terminated;
	private LocationColor locationColor;

	private int fishingButton;
	private int startWaitingTime;
	private int mousemoveX;
	private int mousemoveY;
	
	private double startTime;
	private double maximumDuration;
	private int maximumFish;
	private int currentFish;
	private int maximumFail;
	private int currentFail;
	private int lastFail;
	private boolean closeBot;
	private boolean saveLog;
	private int pressKey;
	private boolean shutdownComputer;
	private boolean exitRift;
	
	private Color lootButtonColorLower;
	private Color lootButtonColorHigher;
	
	private Bait bait;
	private Point fishstick;
	private int nbBaitUsed;
	private int nbBaitToUse;
	private boolean confirmation;
	
	private static final Random random = new Random();
	private static final Boolean DEBUG = false; // Boolean avec majuscule pour éviter des warnings à la con de type Dead code
}
