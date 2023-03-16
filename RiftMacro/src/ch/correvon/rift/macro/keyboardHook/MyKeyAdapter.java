package ch.correvon.rift.macro.keyboardHook;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import ch.correvon.rift.macro.keyRobot.KeyRobot;
import ch.correvon.rift.macro.robots.Robot1;
import ch.correvon.rift.macro.robots.Robot2;
import ch.correvon.rift.macro.robots.Robot3;
import ch.correvon.rift.macro.robots.Robot4;
import ch.correvon.rift.macro.robots.Robot5;
import ch.correvon.rift.macro.robots._RobotExecutor;
import ch.correvon.rift.macro.windows.mainWindow.MainWindow;
import de.ksquared.system.keyboard.KeyAdapter;
import de.ksquared.system.keyboard.KeyEvent;

/**
 * http://ksquared.de/blog/2011/07/java-global-system-hook/
 */
public class MyKeyAdapter extends KeyAdapter
{
	public MyKeyAdapter(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
		this.keyRobots = new ArrayList<>(10);
		this.keyRobots.add(null);
	}
	
	public void setKey(KeyRobot keyRobot, int robotClassNumber)
	{
		Class<? extends _RobotExecutor> robot;
		switch(robotClassNumber)
		{
			case 1 : robot = Robot1.class; break;
			case 2 : robot = Robot2.class; break;
			case 3 : robot = Robot3.class; break;
			case 4 : robot = Robot4.class; break;
			case 5 : robot = Robot5.class; break;
			default : this.mainWindow.printError("MyKeyAdapter.setKey : robotClass (" + robotClassNumber + ") is too damn high ! "); return;
		}
		keyRobot.setRobotClass(robot);
		
		if(this.keyRobots.size() > robotClassNumber)
			this.terminateRobot(robotClassNumber);
		this.keyRobots.add(robotClassNumber, keyRobot);
	}
	
	public void terminateAllRobots()
	{
		for(KeyRobot keyRobot:this.keyRobots)
			if(keyRobot != null && keyRobot.getRobot() != null)
				keyRobot.getRobot().exit();
	}
	
	public void removeKey(int robotClass)
	{
		this.terminateRobot(robotClass);
	}
	
	private void terminateRobot(int index)
	{
		KeyRobot replacedKeyRobot = this.keyRobots.remove(index);
		if(replacedKeyRobot.getRobot() != null)
			replacedKeyRobot.getRobot().exit();
	}
	
	@Override
	public void keyReleased(KeyEvent event)
	{
		for(KeyRobot keyRobot:this.keyRobots)
		{
			if(keyRobot == null) // Le 1er keyRobot de la liste est null (Le n° des classes RobotX commence à 1, pas à 0)
				continue;
			
			if(event.getVirtualKeyCode() != keyRobot.getKey()) // Si ce n'est pas la touche correspondante, pas la peine de continuer
				continue;
			
			if(_RobotExecutor.isKeyLocked(keyRobot.getKey())) // Si la touche est vérouillée, on la consumme et on sort
			{
				_RobotExecutor.consume(keyRobot.getKey());
				continue;
			}
			
			_RobotExecutor robot = keyRobot.getRobot();
			if(robot != null && robot.isAlive()) // Si le robot est déjà en train de tourner
			{
				if(event.isCtrlPressed()) // On l'annule si ctrl est pressé
				{
					keyRobot.getRobot().exit();
					continue;
				}
				this.mainWindow.printError("Le robot tourne encore, attendez avant de le relancer"); // Sinon on met juste un message d'avertissement et on sort
				continue;
			}
			
			if(event.isCtrlPressed()) // Si ctrl est pressé, on ne lance pas de nouveau robot (ctrl est utilisé pour les terminer)
				continue;
			
			// Tout bon, on peut créer et démarrer un nouveau robot
			Class<? extends _RobotExecutor> robotClass = keyRobot.getRobotClass();
			try
			{
				Constructor<? extends _RobotExecutor> robotConstructor = robotClass.getDeclaredConstructor(String.class, MainWindow.class);
				robot = robotConstructor.newInstance(keyRobot.getRobotName(), this.mainWindow);
				robot.start();
				keyRobot.setRobot(robot);
			}
			catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private List<KeyRobot> keyRobots;
	private MainWindow mainWindow;
}
