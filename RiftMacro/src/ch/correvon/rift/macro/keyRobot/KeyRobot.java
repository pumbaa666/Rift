package ch.correvon.rift.macro.keyRobot;

import ch.correvon.rift.macro.robots._RobotExecutor;

public class KeyRobot
{
	public KeyRobot(int key)
	{
		this(key, null);
	}
	
	public KeyRobot(int key, Class<_RobotExecutor> robotClass)
	{
		this.robotClass = robotClass;
		this.key = key;
		this.robot = null;
	}
	
	public _RobotExecutor getRobot()
	{
		return this.robot;
	}
	
	public void setRobot(_RobotExecutor robot)
	{
		this.robot = robot;
	}
	
	public Class<? extends _RobotExecutor> getRobotClass()
	{
		return this.robotClass;
	}
	
	public void setRobotClass(Class<? extends _RobotExecutor> robotClass)
	{
		this.robotClass = robotClass;
	}
	
	public int getKey()
	{
		return this.key;
	}
	
	public void setKey(int key)
	{
		this.key = key;
	}
	
	public String getRobotName()
	{
		return this.robotName;
	}
	
	public void setRobotName(String name)
	{
		this.robotName = name;
	}

	private _RobotExecutor robot;
	private int key;
	private String robotName;
	private Class<? extends _RobotExecutor> robotClass;
}
