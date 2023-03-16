/*
 * Created by JFormDesigner on Tue Jun 28 10:27:41 CEST 2011
 */

package ch.correvon.rift.auctionHouse.windows;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.correvon.rift.auctionHouse.interfaces.PrintLog;
import ch.correvon.rift.auctionHouse.interfaces.ProcessAuto;
import ch.correvon.rift.auctionHouse.jtable.ListModel;
import ch.correvon.rift.auctionHouse.tools.AutoThread;
import ch.correvon.rift.auctionHouse.tools.MyRobot;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Loïc Correvon
 */
public class MainWindow extends JFrame implements ProcessAuto, PrintLog
{

	public MainWindow()
	{
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.initComponents();
		this.loadFile();
		
		try
		{
			this.robot = new MyRobot(this);
		}
		catch(AWTException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		this.autoT = new AutoThread(/*this.robot, */this);
		this.setRunning(false);
		this.autoT.start();

		this.robot.setShortDelay((Integer)this.spinShort.getValue());
		super.setVisible(false);
	}
	
	public void run()
	{
		this.numLockState = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
		super.setVisible(true);
	}
	
	@Override public void printLog(String text)
	{
		this.textAreaLog.append(text+"\n");
	}

	private void loadFile()
	{
		this.listModel = new ListModel();

		BufferedReader bufferReader = null;
		String line;

		try 
		{
			bufferReader = new BufferedReader(new FileReader(FILE_PATH));
			
			while((line = bufferReader.readLine()) != null)
				this.listModel.add(line);
			bufferReader.close();
		}
		catch(IOException e)
		{
			this.printLog("Erreur lors de la lecture du fichier \"" + FILE_PATH+"\"\n");
		}

		this.tableList.setModel(this.listModel);
		this.tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if(this.listModel.getRowCount() > 0)
			this.tableList.getSelectionModel().addSelectionInterval(0, 0);
	}
	
	private void exit()
	{
		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, numLockState);
		System.exit(0);
	}

	private void copyToClipboard(String text)
	{
		try
		{
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
		}
		catch(IllegalStateException e1)
		{
			this.printLog("Le presse papier n'est pas disponible");
			e1.printStackTrace();
		}
	}
	
	private boolean selectNextLine()
	{
		int currentSelected = this.tableList.getSelectionModel().getMaxSelectionIndex();
		if(this.listModel.getRowCount() > currentSelected+1)
			this.tableList.getSelectionModel().addSelectionInterval(currentSelected+1, currentSelected+1);
		else
		{
			JOptionPane.showMessageDialog(null, "Fini", "Fini", JOptionPane.INFORMATION_MESSAGE);
			this.setRunning(false);
			this.tableList.getSelectionModel().addSelectionInterval(0, 0);
			return false;
		}
		return true;
	}

	private boolean buttonNextActionPerformed(ActionEvent e)
	{
		this.robot.altTab();
		return this.process();
	}
	
	@Override public boolean process()
	{
		this.robot.delay(this.longDelay);
		this.robot.shiftHome();
		this.robot.delay(this.longDelay);
		this.copyToClipboard(this.listModel.getValueAt(this.tableList.getSelectedRow(), 0));
		this.robot.ctrlV();
		this.robot.delay(this.longDelay);
		this.robot.keyPress(KeyEvent.VK_ENTER);
		this.robot.delay(this.longDelay);
		return this.selectNextLine();
	}
	
	private void setRunning(boolean value)
	{
		this.isRunning = value;
		this.autoT.setRun(value);
	}
	
	private void buttonAutoActionPerformed(ActionEvent e)
	{
		if(!this.isRunning)
		{
			this.buttonAuto.setText("Pause");
			this.autoT.setRun(true);
			this.setRunning(true);
			
			this.robot.altTab();

			int delay = (Integer)this.spinnerSec.getValue() * 1000;
			this.autoT.setDelay(delay);
		}
		else
		{
			this.buttonAuto.setText("Auto");
			this.setRunning(false);
		}
	}

	private void buttonExitActionPerformed(ActionEvent e)
	{
		this.exit();
	}

	private void tableListKeyReleased(KeyEvent e)
	{
		if(e.getKeyCode() != KeyEvent.VK_DELETE)
			return;
		int index = this.tableList.getSelectedRow();
		if(index >= 0)
			this.listModel.remove(index);
	}

	private void buttonSaveActionPerformed(ActionEvent e)
	{
		StringBuilder stringBuilder = new StringBuilder(500);
		
		for(String element:this.listModel.getValues())
			stringBuilder.append(element + "\n");
		
		try
		{
			FileWriter fstream = new FileWriter(FILE_PATH);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(stringBuilder.toString());
			out.close();
		}
		catch(IOException ex)
		{
			this.printLog("Error: " + ex.getMessage());
		}
	}

	private void spinLongStateChanged(ChangeEvent e)
	{
		this.longDelay = (Integer)this.spinLong.getValue();
	}

	private void spinShortStateChanged(ChangeEvent e)
	{
		this.robot.setShortDelay((Integer)this.spinShort.getValue());
	}

	private void buttonClearActionPerformed(ActionEvent e)
	{
		this.textAreaLog.setText("");
	}
	
	/*
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
				{
					@Override protected Void doInBackground()
					{
						buttonAutoActionPerformed(e);
						return null;
					}
				};
				worker.execute();
	*/
	
	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.tabbedPane1 = new JTabbedPane();
		this.panel1 = new JPanel();
		this.scrollPane1 = new JScrollPane();
		this.tableList = new JTable();
		JButton buttonNext = new JButton();
		this.buttonAuto = new JButton();
		this.spinnerSec = new JSpinner();
		JLabel labelSec = new JLabel();
		JButton buttonSave = new JButton();
		JButton buttonExit = new JButton();
		this.panel3 = new JPanel();
		JLabel labelDelayLong = new JLabel();
		this.spinLong = new JSpinner();
		JLabel labelMs1 = new JLabel();
		JLabel labelDelayShort = new JLabel();
		this.spinShort = new JSpinner();
		JLabel labelMs2 = new JLabel();
		this.panel2 = new JPanel();
		JScrollPane scrollLog = new JScrollPane();
		this.textAreaLog = new JTextArea();
		JButton buttonClear = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle("Rift Auction House 0.4.1");
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"$ugap, default:grow, $ugap",
			"$ugap, default, $ugap"));

		//======== tabbedPane1 ========

		//======== panel1 ========
		this.panel1.setLayout(new FormLayout(
			"default, $lcgap, 31dlu, $lcgap, default",
			"default, $lgap, 15dlu, 2*($lgap, default), $lgap, 17dlu:grow, 2*($lgap, default)"));

		//======== scrollPane1 ========

		//---- tableList ----
		this.tableList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				tableListKeyReleased(e);
			}
		});
		this.scrollPane1.setViewportView(this.tableList);
		this.panel1.add(this.scrollPane1, cc.xywh(1, 1, 1, 13));

		//---- buttonNext ----
		buttonNext.setText("Suivant");
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonNextActionPerformed(e);
			}
		});
		this.panel1.add(buttonNext, cc.xywh(3, 1, 3, 1));

		//---- buttonAuto ----
		this.buttonAuto.setText("Auto");
		this.buttonAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonAutoActionPerformed(e);
			}
		});
		this.panel1.add(this.buttonAuto, cc.xywh(3, 5, 3, 1));

		//---- spinnerSec ----
		this.spinnerSec.setModel(new SpinnerNumberModel(1, 1, 60, 1));
		this.panel1.add(this.spinnerSec, cc.xy(3, 7));

		//---- labelSec ----
		labelSec.setText("sec");
		this.panel1.add(labelSec, cc.xy(5, 7));

		//---- buttonSave ----
		buttonSave.setText("Sauver");
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonSaveActionPerformed(e);
			}
		});
		this.panel1.add(buttonSave, cc.xywh(3, 11, 3, 1));

		//---- buttonExit ----
		buttonExit.setText("Quitter");
		buttonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonExitActionPerformed(e);
			}
		});
		this.panel1.add(buttonExit, cc.xywh(3, 13, 3, 1));
		this.tabbedPane1.addTab("Robot", this.panel1);


		//======== panel3 ========
		this.panel3.setLayout(new FormLayout(
			"2*(default, $lcgap), default",
			"default, $lgap, default"));

		//---- labelDelayLong ----
		labelDelayLong.setText("Delay long");
		this.panel3.add(labelDelayLong, cc.xy(1, 1));

		//---- spinLong ----
		this.spinLong.setModel(new SpinnerNumberModel(200, 10, 10000, 10));
		this.spinLong.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				spinLongStateChanged(e);
			}
		});
		this.panel3.add(this.spinLong, cc.xy(3, 1));

		//---- labelMs1 ----
		labelMs1.setText("ms");
		this.panel3.add(labelMs1, cc.xy(5, 1));

		//---- labelDelayShort ----
		labelDelayShort.setText("Delay court");
		this.panel3.add(labelDelayShort, cc.xy(1, 3));

		//---- spinShort ----
		this.spinShort.setModel(new SpinnerNumberModel(50, 10, 1000, 10));
		this.spinShort.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				spinShortStateChanged(e);
			}
		});
		this.panel3.add(this.spinShort, cc.xy(3, 3));

		//---- labelMs2 ----
		labelMs2.setText("ms");
		this.panel3.add(labelMs2, cc.xy(5, 3));
		this.tabbedPane1.addTab("Param\u00e9trage", this.panel3);


		//======== panel2 ========
		this.panel2.setLayout(new FormLayout(
			"default:grow, default",
			"fill:default:grow, $rgap, fill:default"));

		//======== scrollLog ========
		scrollLog.setViewportView(this.textAreaLog);
		this.panel2.add(scrollLog, cc.xywh(1, 1, 2, 1));

		//---- buttonClear ----
		buttonClear.setText("Effacer");
		buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClearActionPerformed(e);
			}
		});
		this.panel2.add(buttonClear, cc.xy(2, 3));
		this.tabbedPane1.addTab("Log", this.panel2);

		contentPane.add(this.tabbedPane1, cc.xy(2, 2));
		setSize(270, 430);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private ListModel listModel;
	private MyRobot robot;
	private boolean isRunning;
	private AutoThread autoT;
	private int longDelay = 100;
	private boolean numLockState;
	
	private static final String FILE_PATH = "./data/objects.txt";

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JTabbedPane tabbedPane1;
	private JPanel panel1;
	private JScrollPane scrollPane1;
	private JTable tableList;
	private JButton buttonAuto;
	private JSpinner spinnerSec;
	private JPanel panel3;
	private JSpinner spinLong;
	private JSpinner spinShort;
	private JPanel panel2;
	private JTextArea textAreaLog;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
