package ch.correvon.rift.auctionHouse.use;

import ch.correvon.rift.auctionHouse.windows.MainWindow;
import ch.correvon.rift.auctionHouse.windows.TestShift;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new MainWindow().run();
		if(false || args.length > 0 && args[0].equals("-t"))
			new TestShift().run();
	}
}
