package ch.correvon.rift.fishing.windows.mainWindow;

import java.awt.Color;

public class LocationColor
{
	public LocationColor(String location, Color color)
	{
		this.location = location;
		this.color = new Color(color.getRed(), color.getGreen(), color.getBlue());
	}

	public LocationColor(LocationColor locationColor)
	{
		this(locationColor.getLocation(), locationColor.getColor());
	}

	public String getLocation()
	{
		return location;
	}
	
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(Color color)
	{
		this.color = new Color(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	@Override public String toString()
	{
		return "name=" + this.location + ",r=" + this.color.getRed() + ",g=" + this.color.getGreen() + ",b=" + this.color.getBlue();
	}
	
	public String toStringHuman()
	{
		return this.location + "(" + this.color.getRed() + ", " + this.color.getGreen() + ", " + this.color.getBlue() + ")";
	}
	
	private String location;
	private Color color;
}
