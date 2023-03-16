package ch.correvon.rift.serverAlert.tools;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RiftSystray
{
	public RiftSystray(Manager manager)
	{
		this(manager, null);
	}
	
	public RiftSystray(Manager manager, String errorMsg)
	{
		this.manager = manager;
		
		String imagePath = System.getProperty("user.dir")+"\\images\\actif.png";
	    this.imageActif = Toolkit.getDefaultToolkit().getImage(imagePath);

	    imagePath = System.getProperty("user.dir")+"\\images\\inactif.png";
	    this.imageInactif = Toolkit.getDefaultToolkit().getImage(imagePath);
	    
	    imagePath = System.getProperty("user.dir")+"\\images\\alert.png";
	    this.imageAlert = Toolkit.getDefaultToolkit().getImage(imagePath);
	    
	    imagePath = System.getProperty("user.dir")+"\\images\\ok.png";
	    this.imageOk = Toolkit.getDefaultToolkit().getImage(imagePath);
	    
	    imagePath = System.getProperty("user.dir")+"\\images\\unknown.png";
	    this.imageUnknown = Toolkit.getDefaultToolkit().getImage(imagePath);
	    
	    this.initSystray(errorMsg);
	    
	    this.delayClick = null;
	}
	
	public void alert(String message, boolean updateImage)
	{
		this.trayIcon.displayMessage("Rift Server Alert", message, TrayIcon.MessageType.WARNING);
		if(updateImage)
			this.trayIcon.setImage(this.imageAlert);
	}
	
	public void serverUnknown(String message)
	{
		this.trayIcon.displayMessage("Rift Server Alert", message, TrayIcon.MessageType.WARNING);
		this.trayIcon.setImage(this.imageUnknown);
	}
	
	public void ok()
	{
		this.trayIcon.setImage(this.imageOk);
	}
	
	public void exit()
	{
		this.tray.remove(this.trayIcon);
	}

	/**
	 * Create the tray, the menu and the menu-actions
	 */
	private void initSystray(String errorMsg)
	{
		/* Test */
		if(!SystemTray.isSupported())
		{
			System.err.println("Systray not supported");
			return;
		}
		
	    /* Création des listener */
	    MouseListener mouseListener = new MouseListener()
	    {
	        public void mouseClicked(MouseEvent e)
	        {
	        	if(e.getButton() != MouseEvent.BUTTON1) // Seulement le click gauche
	        		return;
	        	
	        	if(RiftSystray.this.delayClick == null) // Delay pour ne pas afficher l'info-bulle du status du serveur si on fait un double click pour désactiver le programme
	        	{
	        		RiftSystray.this.delayClick = new DelayClick(200);
	        		RiftSystray.this.delayClick.start();
	        	}
	        	
	        	int nbClick = e.getClickCount();
	            if(nbClick == 2)
	            {
	            	RiftSystray.this.flipTrayState();
	            	RiftSystray.this.delayClick.exit();
	            	RiftSystray.this.delayClick = null;
	            }
	        }

	        public void mouseEntered(MouseEvent e) {}
	        public void mouseExited(MouseEvent e) {}
	        public void mousePressed(MouseEvent e) {}
	        public void mouseReleased(MouseEvent e) {}
	    };
	    
	    ActionListener viewServerState = new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	RiftSystray.this.manager.serverStatusAlert(false);
	        }
	    };
	            
	    ActionListener activateListener = new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	RiftSystray.this.flipTrayState();
	        }
	    };
	            
	    ActionListener openMainWindow = new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	RiftSystray.this.manager.showWindow();
	        }
	    };
	            
	    ActionListener exitListener = new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	RiftSystray.this.manager.exit(true);
	        }
	    };

	    /* Création du menu popup */
	    PopupMenu popup = new PopupMenu();
	    this.itemActiver = new MenuItem("Activer");
	    this.itemActiver.setFont(MENU_ITEM_FONT.deriveFont(Font.BOLD));
	    this.itemActiver.addActionListener(activateListener);
	    popup.add(this.itemActiver);

	    MenuItem itemShowNotification = new MenuItem("Voir l'état du serveur");
	    itemShowNotification.setFont(MENU_ITEM_FONT);
	    itemShowNotification.addActionListener(viewServerState);
	    popup.add(itemShowNotification);

	    MenuItem itemFormatString = new MenuItem("Ouvrir la fenêtre de contrôle");
	    itemFormatString.setFont(MENU_ITEM_FONT);
	    itemFormatString.addActionListener(openMainWindow);
	    itemFormatString.setEnabled(this.manager.hasMainWindow());
	    popup.add(itemFormatString);

	    MenuItem itemQuitter = new MenuItem("Quitter");
	    itemQuitter.setFont(MENU_ITEM_FONT);
	    itemQuitter.addActionListener(exitListener);
	    popup.add(itemQuitter);

	    /* Création du systray */
	    this.tray = SystemTray.getSystemTray();

	    this.trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(""), null, popup);
	    this.trayIcon.addMouseListener(mouseListener);

	    this.trayIcon.setImageAutoSize(true);

	    try
	    {
	        this.tray.add(this.trayIcon);
	    }
	    catch (AWTException e)
	    {
	        System.err.println("TrayIcon could not be added.");
	        return;
	    }
	    this.isActif = true;
	    
	    if(errorMsg == null)
	    	this.activateTray(false);
	    else
	    	this.unactivateTray(errorMsg);
	}

	/**
	 * Switch state from Activated / Deactivated
	 */
	private void flipTrayState()
	{
    	if(this.isActif)
    		unactivateTray(null);
    	else
    		activateTray(true);
	}

	/**
	 * Activate the clipboard listener
	 */
	private void activateTray(boolean enableTimer)
	{
		this.isActif = true;
		this.trayIcon.displayMessage("Activé", "Rift Server Alert est maintenant actif", TrayIcon.MessageType.INFO);

	    this.trayIcon.setImage(this.imageActif);
	    this.itemActiver.setLabel("Desactiver");

	    if(enableTimer)
	    	this.manager.setEnableTimer(true);
	}

	/**
	 * Deactivate the clipboard listener
	 */
	private void unactivateTray(String errorMsg)
	{
		String message = "Rift Server Alert est maintenant inactif";
		if(errorMsg != null)
			message = errorMsg;

		this.isActif = false;
		this.trayIcon.displayMessage("Désactivé", message, TrayIcon.MessageType.INFO);

	    this.trayIcon.setImage(this.imageInactif);
	    this.itemActiver.setLabel("Activer");
	    
	    this.manager.setEnableTimer(false);
	}
	
	private Manager manager;

	private SystemTray tray;
	private TrayIcon trayIcon;
	
	private boolean isActif;
	
	private MenuItem itemActiver;
	
	private Image imageActif; 
	private Image imageInactif;
	private Image imageAlert;
	private Image imageOk;
	private Image imageUnknown;
	
	private DelayClick delayClick;
	
	private static final Font MENU_ITEM_FONT = new Font("Times new roman", Font.PLAIN, 12);
	
	private class DelayClick extends Thread
	{
		public DelayClick(int delay)
		{
			this.delay = delay;
			this.exit = false;
		}
		
		@Override public void run()
		{
			try
			{
				super.sleep(this.delay);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			if(!this.exit)
				RiftSystray.this.manager.serverStatusAlert(false);
			RiftSystray.this.delayClick = null;
		}
		
		public void exit()
		{
			this.exit = true;
		}
		
		private int delay;
		private boolean exit;
	}
}
