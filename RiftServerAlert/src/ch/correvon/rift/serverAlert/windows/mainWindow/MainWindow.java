/*
 * Created by JFormDesigner on Wed Mar 02 10:23:40 CET 2011
 */

package ch.correvon.rift.serverAlert.windows.mainWindow;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ch.correvon.rift.serverAlert.tools.Manager;
import ch.correvon.rift.serverAlert.tools.RiftPopulationEnum;
import ch.correvon.rift.serverAlert.xml.RiftServer;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.xml.sax.SAXException;

/**
 * @author Loïc Correvon
 */
public class MainWindow extends JFrame implements WindowListener
{
	public MainWindow(String title)
	{
		super(title);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.addWindowListener(this);
		this.initComponents();
		
		this.textUrl.setText("http://www.riftgame.com/en/status/eu-status.xml"); // default url
		this.readPreferenceFile(PREFERENCE_FILE);
		AutoCompleteDecorator.decorate(this.comboPopulation);
		AutoCompleteDecorator.decorate(this.comboServers);
		
		super.setVisible(false);
	}
	
	public void run()
	{
		this.manager = new Manager(this.textUrl.getText(), this);
		this.manager.setDefaultServer(this.defaultServer);
		String errorMsg = this.manager.getErrorMsg();
		
		if(errorMsg != null)
		{
			JOptionPane.showMessageDialog(this,
					"Une erreur s'est produite lors du parsing de l'url, veuillez consulter la console pour connaitre la raison"
					, "Liste vide",
					JOptionPane.ERROR_MESSAGE);
			this.comboServers.setModel(new DefaultComboBoxModel()); // vide la combo box
			return;
		}

		List<String> serversList = new ArrayList<String>(50); // Tri des serveurs par ordre alphabetique
		try
		{
			for(RiftServer server:this.manager.getHandler().getServers())
				serversList.add(server.getName());
		}
		catch(SAXException e)
		{
			e.printStackTrace();
		}
		Collections.sort(serversList);

		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(String server:serversList)
			model.addElement(server);
		
		this.comboServers.setModel(model);
		if(this.manager.getDefaultServer() != null)
			this.comboServers.setSelectedItem(this.manager.getDefaultServer());
		
		super.setVisible(true);
	}
	
	public void showWindow()
	{
		super.setVisible(true);
	}
	
	/* **************** file ************** */
	public void readPreferenceFile(String path)
	{
	    try
	    {
	        FileReader fichier = new FileReader(path);
	        BufferedReader buffer = new BufferedReader(fichier);
	        
	        String line = buffer.readLine();
	        
	        String[] tabSplit;
	        String[] tabHourSplit;
	        while(line != null)
	        {
	        	tabSplit = line.split(":", 2);
	        	if(tabSplit.length != 2)
	        		System.err.println("Erreur dans la lecture du fichier de préférence : "+line);
	        	else
	        	{
	        		if(tabSplit[0].equals("url"))
	        			this.textUrl.setText(tabSplit[1]);
	        		else if(tabSplit[0].equals("server"))
	        			this.defaultServer = tabSplit[1];
	        		else if(tabSplit[0].equals("check"))
	        		{
	        			try
						{
							this.spinCheck.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinCheck.setValue(1);
						}
	        		}
	        		else if(tabSplit[0].equals("recheck"))
	        		{
	        			try
						{
							this.spinRecheck.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinRecheck.setValue(1);
						}
	        		}
	        		else if(tabSplit[0].equals("queue"))
	        		{
	        			try
						{
							this.spinQueue.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinQueue.setValue(1);
						}
	        		}
	        		else if(tabSplit[0].equals("population"))
	        		{
	        			RiftPopulationEnum population = RiftPopulationEnum.valueOf(tabSplit[1]);
	        			this.comboPopulation.setSelectedItem(population);
	        			if(population.compareTo(RiftPopulationEnum.high) == 0)
	        				this.spinQueue.setEnabled(true);
	        		}
	        		else if(tabSplit[0].equals("timeFrom"))
	        		{
	        			tabHourSplit = tabSplit[1].split("h");
	    	        	if(tabHourSplit.length != 2)
	    	        		System.err.println("L'heure de départ est incorrecte : "+line);
	    	        	else
	    	        	{
	    	        		this.spinTimeStartHour.setValue(Integer.valueOf(tabHourSplit[0]));
	    	        		this.spinTimeStartMinute.setValue(Integer.valueOf(tabHourSplit[1]));
	    	        	}
	        		}
	        		else if(tabSplit[0].equals("timeTo"))
	        		{
	        			tabHourSplit = tabSplit[1].split("h");
	    	        	if(tabHourSplit.length != 2)
	    	        		System.err.println("L'heure de départ est incorrecte : "+line);
	    	        	else
	    	        	{
	    	        		this.spinTimeEndHour.setValue(Integer.valueOf(tabHourSplit[0]));
	    	        		this.spinTimeEndMinute.setValue(Integer.valueOf(tabHourSplit[1]));
	    	        	}
	        		}
	        		else if(tabSplit[0].equals("WOLIP"))
	        			this.textWOLIP.setText(tabSplit[1]);
	        		else if(tabSplit[0].equals("WOLMAC"))
	        			this.textWOLMAC.setText(tabSplit[1]);
	        		else if(tabSplit[0].equals("WOLPort"))
	        		{
	        			try
						{
							this.spinWOLPort.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinQueue.setValue(9);
						}
	        		}
	        	}
	        	line = buffer.readLine();
	        }
	        buffer.close();
	        fichier.close();
	        
	    }
	    catch(IOException e)
	    {
	    }
	}
	public void writePreferenceFile(String path)
	{
        try
        {
            FileWriter file = new FileWriter(path);
            StringBuilder stringBuilder = new StringBuilder(300);
            stringBuilder.append("url:"+this.textUrl.getText()+"\n");
            stringBuilder.append("server:"+this.comboServers.getSelectedItem()+"\n");
            stringBuilder.append("check:"+this.spinCheck.getValue()+"\n");
            stringBuilder.append("recheck:"+this.spinRecheck.getValue()+"\n");
            stringBuilder.append("queue:"+this.spinQueue.getValue()+"\n");
            stringBuilder.append("population:"+this.comboPopulation.getSelectedItem()+"\n");
            stringBuilder.append("timeFrom:"+this.spinTimeStartHour.getValue()+"h"+this.spinTimeStartMinute.getValue()+"\n");
            stringBuilder.append("timeTo:"+this.spinTimeEndHour.getValue()+"h"+this.spinTimeEndMinute.getValue()+"\n");
            stringBuilder.append("WOLIP:"+this.textWOLIP.getText()+"\n");
            stringBuilder.append("WOLMAC:"+this.textWOLMAC.getText()+"\n");
            stringBuilder.append("WOLPort:"+this.spinWOLPort.getValue()+"\n");
            file.write(stringBuilder.toString());
            file.close();
       	}
        catch(IOException e)
        {
       		System.err.println("erreur lors de l'écriture du fichier de statistique : "+path);
       		e.printStackTrace();
        }
	}

	public void exit()
	{
		this.writePreferenceFile(PREFERENCE_FILE);
		this.manager.exit(false);
	}

	/* **************** gui *************** */
	private void buttonOkActionPerformed(ActionEvent e)
	{
		String serverToCheck = (String)this.comboServers.getSelectedItem();
		int check = (Integer)this.spinCheck.getValue();
		int recheck = (Integer)this.spinRecheck.getValue();
		RiftPopulationEnum minPopulation = RiftPopulationEnum.valueOf(this.comboPopulation.getSelectedItem().toString());
		int minQueue = (Integer)this.spinQueue.getValue();

		GregorianCalendar gc = new GregorianCalendar(0, 0, 0, (Integer)this.spinTimeStartHour.getValue(), (Integer)this.spinTimeStartMinute.getValue(), 0);
		Time timeFrom = new Time(gc.getTimeInMillis());

		gc = new GregorianCalendar(0, 0, 0, (Integer)this.spinTimeEndHour.getValue(), (Integer)this.spinTimeEndMinute.getValue(), 0);
		Time timeTo = new Time(gc.getTimeInMillis());
		
		String wolIp = this.textWOLIP.getText();
		String wolMac = this.textWOLMAC.getText();
		int wolPort = (Integer)this.spinWOLPort.getValue();
		
		this.manager.release();
		this.manager = new Manager(this.textUrl.getText(), this);
		this.manager.launch(serverToCheck, check, recheck, minPopulation, minQueue, timeFrom, timeTo, wolIp, wolMac, wolPort);
		
		super.dispose();
	}

	private void buttonUpdateUrlActionPerformed(ActionEvent e)
	{
		this.run();
	}

	private void buttonExitActionPerformed(ActionEvent e)
	{
		this.exit();
	}

	/* **************** listener *********** */
	@Override public void windowIconified(WindowEvent e)
	{
		super.setVisible(false);
	}
	@Override public void windowActivated(WindowEvent e){}
	@Override public void windowClosed(WindowEvent e){}
	@Override public void windowClosing(WindowEvent e){}
	@Override public void windowDeactivated(WindowEvent e){}
	@Override public void windowDeiconified(WindowEvent e){}
	@Override public void windowOpened(WindowEvent e){}

	private void comboPopulationItemStateChanged(ItemEvent e)
	{
		int selectedIndex = this.comboPopulation.getSelectedIndex();
		this.spinQueue.setEnabled(selectedIndex == RiftPopulationEnum.values().length-1);
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		JLabel labelUrl = new JLabel();
		this.textUrl = new JTextField();
		JButton buttonUpdateUrl = new JButton();
		JLabel labelServer = new JLabel();
		this.comboServers = new JComboBox();
		JLabel labelCheck = new JLabel();
		this.spinCheck = new JSpinner();
		JLabel labelMinuteCheck = new JLabel();
		JLabel labelRecheck = new JLabel();
		this.spinRecheck = new JSpinner();
		JLabel labelMinutesRecheck = new JLabel();
		JLabel labelPopulation = new JLabel();
		this.comboPopulation = new JComboBox();
		JLabel labelQueue = new JLabel();
		this.spinQueue = new JSpinner();
		JLabel labelActiv = new JLabel();
		this.spinTimeStartHour = new JSpinner();
		JLabel labelTimeStart = new JLabel();
		this.spinTimeStartMinute = new JSpinner();
		JLabel labelAnd = new JLabel();
		this.spinTimeEndHour = new JSpinner();
		JLabel labelTimeEnd = new JLabel();
		this.spinTimeEndMinute = new JSpinner();
		JLabel labelWOLIP = new JLabel();
		this.textWOLIP = new JTextField();
		JLabel labelWOLPort = new JLabel();
		this.spinWOLPort = new JSpinner();
		JLabel labelWOLMAC = new JLabel();
		this.textWOLMAC = new JTextField();
		JButton buttonOk = new JButton();
		JButton buttonExit = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"$ugap, $lcgap, default, $lcgap, default:grow, 2*($lcgap, default), $lcgap, center:default, 3*($lcgap, default), $lcgap, $ugap",
			"$ugap, 8*($lgap, fill:default), $lgap, default, $lgap, $ugap"));
		((FormLayout)contentPane.getLayout()).setRowGroups(new int[][] {{3, 5, 7, 9, 11, 13, 15, 17, 19}});

		//---- labelUrl ----
		labelUrl.setText("url");
		contentPane.add(labelUrl, cc.xy(3, 3));
		contentPane.add(this.textUrl, cc.xywh(5, 3, 7, 1));

		//---- buttonUpdateUrl ----
		buttonUpdateUrl.setText("update");
		buttonUpdateUrl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonUpdateUrlActionPerformed(e);
			}
		});
		contentPane.add(buttonUpdateUrl, cc.xywh(13, 3, 5, 1));

		//---- labelServer ----
		labelServer.setText("serveur");
		contentPane.add(labelServer, cc.xy(3, 5));

		//---- comboServers ----
		this.comboServers.setEditable(true);
		contentPane.add(this.comboServers, cc.xywh(5, 5, 5, 1));

		//---- labelCheck ----
		labelCheck.setText("V\u00e9rifier toute les");
		contentPane.add(labelCheck, cc.xy(3, 7));

		//---- spinCheck ----
		this.spinCheck.setModel(new SpinnerNumberModel(1, 1, 1440, 1));
		contentPane.add(this.spinCheck, cc.xywh(5, 7, 5, 1));

		//---- labelMinuteCheck ----
		labelMinuteCheck.setText("minutes");
		contentPane.add(labelMinuteCheck, cc.xy(11, 7));

		//---- labelRecheck ----
		labelRecheck.setText("<html>Une fois alert\u00e9<br>alerter \u00e0 nouveau apr\u00e8s</html>");
		contentPane.add(labelRecheck, cc.xy(3, 9));

		//---- spinRecheck ----
		this.spinRecheck.setModel(new SpinnerNumberModel(10, 1, 1440, 1));
		contentPane.add(this.spinRecheck, cc.xywh(5, 9, 5, 1));

		//---- labelMinutesRecheck ----
		labelMinutesRecheck.setText("minutes");
		contentPane.add(labelMinutesRecheck, cc.xy(11, 9));

		//---- labelPopulation ----
		labelPopulation.setText("<html>Alerter lorsque<br>la population atteint</html>");
		contentPane.add(labelPopulation, cc.xy(3, 11));

		//---- comboPopulation ----
		this.comboPopulation.setEditable(true);
		this.comboPopulation.setModel(new DefaultComboBoxModel(new String[] {
			"low",
			"medium",
			"high"
		}));
		this.comboPopulation.setSelectedIndex(2);
		this.comboPopulation.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				comboPopulationItemStateChanged(e);
			}
		});
		contentPane.add(this.comboPopulation, cc.xywh(5, 11, 5, 1));

		//---- labelQueue ----
		labelQueue.setText("queue");
		contentPane.add(labelQueue, cc.xy(11, 11));

		//---- spinQueue ----
		this.spinQueue.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
		this.spinQueue.setEnabled(false);
		contentPane.add(this.spinQueue, cc.xywh(13, 11, 5, 1));

		//---- labelActiv ----
		labelActiv.setText("Actif entre");
		contentPane.add(labelActiv, cc.xy(3, 13));

		//---- spinTimeStartHour ----
		this.spinTimeStartHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
		contentPane.add(this.spinTimeStartHour, cc.xy(5, 13));

		//---- labelTimeStart ----
		labelTimeStart.setText("h");
		contentPane.add(labelTimeStart, cc.xy(7, 13));

		//---- spinTimeStartMinute ----
		this.spinTimeStartMinute.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		contentPane.add(this.spinTimeStartMinute, cc.xy(9, 13));

		//---- labelAnd ----
		labelAnd.setText("et");
		contentPane.add(labelAnd, cc.xy(11, 13));

		//---- spinTimeEndHour ----
		this.spinTimeEndHour.setModel(new SpinnerNumberModel(23, 0, 23, 1));
		contentPane.add(this.spinTimeEndHour, cc.xy(13, 13));

		//---- labelTimeEnd ----
		labelTimeEnd.setText("h");
		contentPane.add(labelTimeEnd, cc.xy(15, 13));

		//---- spinTimeEndMinute ----
		this.spinTimeEndMinute.setModel(new SpinnerNumberModel(59, 0, 59, 1));
		contentPane.add(this.spinTimeEndMinute, cc.xy(17, 13));

		//---- labelWOLIP ----
		labelWOLIP.setText("Wake-on-lan : ip");
		contentPane.add(labelWOLIP, cc.xy(3, 15));
		contentPane.add(this.textWOLIP, cc.xywh(5, 15, 5, 1));

		//---- labelWOLPort ----
		labelWOLPort.setText("port");
		contentPane.add(labelWOLPort, cc.xy(11, 15));

		//---- spinWOLPort ----
		this.spinWOLPort.setModel(new SpinnerNumberModel(9, 1, 65535, 1));
		contentPane.add(this.spinWOLPort, cc.xywh(13, 15, 5, 1));

		//---- labelWOLMAC ----
		labelWOLMAC.setText("Wake-on-lan : mac");
		contentPane.add(labelWOLMAC, cc.xy(3, 17));
		contentPane.add(this.textWOLMAC, cc.xywh(5, 17, 5, 1));

		//---- buttonOk ----
		buttonOk.setText("Ok");
		buttonOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonOkActionPerformed(e);
			}
		});
		contentPane.add(buttonOk, cc.xy(3, 19));

		//---- buttonExit ----
		buttonExit.setText("Quitter");
		buttonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonExitActionPerformed(e);
			}
		});
		contentPane.add(buttonExit, cc.xywh(13, 19, 5, 1));
		setSize(505, 400);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private Manager manager;
	private String defaultServer;
	//private List<RiftServer> serversList;
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JTextField textUrl;
	private JComboBox comboServers;
	private JSpinner spinCheck;
	private JSpinner spinRecheck;
	private JComboBox comboPopulation;
	private JSpinner spinQueue;
	private JSpinner spinTimeStartHour;
	private JSpinner spinTimeStartMinute;
	private JSpinner spinTimeEndHour;
	private JSpinner spinTimeEndMinute;
	private JTextField textWOLIP;
	private JSpinner spinWOLPort;
	private JTextField textWOLMAC;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	private static final String PREFERENCE_FILE = "data\\preferences.txt";
}
