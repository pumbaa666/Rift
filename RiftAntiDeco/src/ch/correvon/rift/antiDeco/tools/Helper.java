package ch.correvon.rift.antiDeco.tools;

import java.util.Random;
import javax.swing.JSpinner;

public class Helper
{
	public static int getSpinnerInt(JSpinner spinner)
	{
		return Integer.parseInt(spinner.getValue().toString());
	}

	public static int getRandomNumberInRange(int min, int max)
	{
		if(min > max)
			throw new IllegalArgumentException("max must be greater than min");
		
		if(min == max)
			return min;

		return new Random().nextInt((max - min) + 1) + min;
	}
	
	public static String getMouseEventText(int button)
	{
		switch(button)
		{
			case 1: return "Left click";
			case 2: return "Middle click";
			case 3: return "Right click";
		}
		return "";
	}
}
