package ch.correvon.rift.auctionHouse.tools;


public class NumLockThread extends Thread
{
	// Encore un bug qui empêche de remettre le Num Lock dans son état initial de manière normale
	// Ce code je fait pas marcher mieux, quelle daube !
	@Override public void run()
	{
		try
		{
			super.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
//		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
//		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
//		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
//		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
	}
}
