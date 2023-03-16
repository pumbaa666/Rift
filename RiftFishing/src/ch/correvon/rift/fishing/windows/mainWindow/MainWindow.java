/*
 * Created by JFormDesigner on Tue Mar 08 20:27:40 CET 2011
 */

package ch.correvon.rift.fishing.windows.mainWindow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ch.correvon.pumbaaUtils.helpers.ClipBoardHelper;
import ch.correvon.pumbaaUtils.helpers.ComponentHelper;
import ch.correvon.pumbaaUtils.helpers.FileHelper;
import ch.correvon.rift.fishing.tools.FishingRobot;
import ch.correvon.rift.fishing.tools.LearningRobot;
import ch.correvon.rift.fishing.windows.baitWindow.Bait;
import ch.correvon.rift.fishing.windows.baitWindow.BaitModel;
import ch.correvon.rift.fishing.windows.baitWindow.BaitSelectionListener;
import ch.correvon.rift.fishing.windows.baitWindow.BaitWindow;
import ch.correvon.rift.fishing.windows.baitWindow.LearningResultInterface;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Loïc Correvon
 */
public class MainWindow extends JFrame implements WindowListener, LearningResultInterface
{
	public MainWindow(String title)
	{
		this.fishingTime = 0;
		this.initComponents();
		this.myInitComponents();
		
		this.fishCaughtSession = new int[4];
		this.fishCaughtAll = new int[4];
		for(int i = 0; i < 4; i++)
		{
			this.fishCaughtSession[i] = 0;
			this.fishCaughtAll[i] = 0;
		}
		
		this.addWindowListener(this);
		
		this.readPreferenceFile(PREFERENCE_FILE);
		this.locationModel.addNewLocation();
		this.isFishstickLearningRobotRunning = false;
		
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setTitle(title);
		super.setVisible(false);
	}
	
	public void run()
	{
		super.setVisible(true);
	}
	
	/* ******************** GUI ******************** */
	private void buttonRunActionPerformed(ActionEvent e)
	{
		if(this.isRunning)
		{
			this.robot.exit();
			this.activateRunButton(false);
		}
		else
		{
			if(this.robot != null && !this.robot.isTerminated())
			{
				this.labelCurentLocation.setText("Je t'ai dit d'attendre, sacam !");
				return;
			}
			int selectedLocationIndex = this.tableLocations.getSelectedRow();
			if(selectedLocationIndex == -1)
				selectedLocationIndex = 0;
			
			LocationColor selectedLocation = this.locationModel.getlocationColor(selectedLocationIndex);
			
			Bait selectedBait = null;
			if(this.baitModel.getRowCount() > 0 && this.checkBait.isSelected())
			{
				int selectedBaitIndex = this.tableBait.getSelectedRow();
				if(selectedBaitIndex == -1)
					selectedBaitIndex = 0;
				selectedBait = this.baitModel.getBait(selectedBaitIndex);
			}
			Point fishstick = new Point(ComponentHelper.intValueOf(this.spinBaitX), ComponentHelper.intValueOf(this.spinBaitY));
			int nbBaitToUse = ComponentHelper.intValueOf(this.spinBaitNb);
			this.robot = new FishingRobot(selectedLocation, this, selectedBait, fishstick, nbBaitToUse, this.checkLocationConfirmation.isSelected());
			this.startTime = System.currentTimeMillis();
			
			/* ------------ Start panel ------------ */
			this.robot.setStartWaitingTime((Integer)(this.spinStartWaitingTime.getValue()) * 1000);
			this.robot.setFishingButton(new Integer(this.labelStartCode.getText()));
			if(this.checkStartMouveMouse.isSelected())
				this.robot.setMouseMove(ComponentHelper.intValueOf(this.spinStartMouveX), ComponentHelper.intValueOf(this.spinStartMouveY));
			this.robot.setLootButtonColor(new Color(ComponentHelper.intValueOf(this.spinStartR),ComponentHelper.intValueOf(this.spinStartG),ComponentHelper.intValueOf(this.spinStartB)));
			
			/* ------------ Stop panel ------------ */
			if(this.checkStopTime.isSelected())
			{
				double duration = ComponentHelper.intValueOf(this.spinStopHours) * 3600; // Nombre d'heures
				duration += ComponentHelper.intValueOf(this.spinStopMinutes) * 60;
				duration *= 1000; // Le tout en ms
				this.robot.setMaximumDuration(duration);
			}
			
			if(this.checkStopFish.isSelected())
				this.robot.setMaximumFish(ComponentHelper.intValueOf(this.spinStopFish));

			if(this.checkStopFail.isSelected())
				this.robot.setMaximumFail(ComponentHelper.intValueOf(this.spinStopFail));
			
			if(this.checkStopBot.isSelected())
				this.robot.setCloseBot(true);
			
			if(this.checkStopPressKey.isSelected())
				this.robot.setPressKey(this.labelStopPressCode.getText());

			if(this.checkStopSaveLog.isSelected())
				this.robot.setSaveLog(true);

			if(this.checkStopComputer.isSelected())
				this.robot.setShutdownComputer(true);

			if(this.checkStopRift.isSelected())
				this.robot.setExitRift(true);

			try
			{
				this.robot.start();
			}
			catch(Exception ex)
			{
				this.printLog("ERREUR INATENDUE\n"+ex.getMessage()+"\n"+ex.getStackTrace(), this.logStyleRed, true, true);
			}
			
			this.labelCurentLocation.setText(selectedLocation.toStringHuman() + (selectedBait!=null?" Appât : " + selectedBait.getName() : ""));

			this.isRunning = true;
			this.buttonRun.setText("Arrêter");
		}
	}
	
	public void activateRunButton(boolean ready)
	{
		if(ready)
		{
			this.labelCurentLocation.setText("Le robot est arrêté");
			this.buttonRun.setEnabled(true);
			this.buttonRun.setText("Démarrer");
			this.isRunning = false;
		}
		else
		{
			this.buttonRun.setEnabled(false);
			this.labelCurentLocation.setText("Arrêt du robot en cours, veuillez patienter svp");
		}
	}
	
	public void printLog(String text, Style style, boolean showTime, boolean newLine)
	{
		int lastLength = this.textLog.getStyledDocument().getLength();
		String formatedText = (showTime ? DATE_FORMAT.format(new Date()) : "") + " : " + text + (newLine ? "\n" : "");
		try
		{
			this.textLogDocument.insertString(lastLength, formatedText, style);
			this.textLog.setCaretPosition(lastLength);
		}
		catch(BadLocationException e)
		{
			e.printStackTrace();
		}
		if(DEBUG)
			System.out.print(formatedText);
	}
	
	public String saveLog()
	{
		String name = LOG_DIR + "\\" + new SimpleDateFormat("yyyy-MM-dd - HH.mm.ss.S").format(new Date()) + ".txt";
		boolean result = FileHelper.writeFile(name, this.textLog.getText());
		if(result)
			return "Logs sauvés dans " + name;
		return null;
	}
	
	public void shutdownComputer()
	{
		String os = System.getProperty("os.name");
		if(!os.toLowerCase().startsWith("windows"))
		{
			this.printLog("Impossible d'éteindre un ordinateur qui n'est pas sous Windows", this.logStyleRed, true, true);
			return;
		}
		Runtime runtime = Runtime.getRuntime();
		try
		{
			String[] args = { "cmd.exe", "/C", "shutdown /s /f /t 60" };
			runtime.exec(args);
		}
		catch(IOException e)
		{
			this.printLog("Erreur lors de l'arrêt de l'ordinateur : " + e.getMessage(), this.logStyleRed, true, true);
			e.printStackTrace();
		}
	}

	private void buttonExitActionPerformed(ActionEvent e)
	{
		this.exit();
	}
	
	public void exit()
	{
		this.writePreferenceFile(PREFERENCE_FILE);
		System.exit(0);
	}

	private void textFishingButtonKeyReleased(KeyEvent e)
	{
		this.labelStartCode.setText(e.getKeyCode()+"");
		this.textStartFishingButton.setText(KeyEvent.getKeyText(e.getKeyCode()));
	}

	private void buttonLogClearActionPerformed(ActionEvent e)
	{
		this.textLog.setText("");
	}

	private void buttonLogCopyActionPerformed(ActionEvent e)
	{
		ClipBoardHelper.writeClipboard(this.textLog.getText());
	}
	
	public void fishCaught(int state)
	{
		if(state > 3)
			return;
		this.fishCaughtSession[state]++;
		this.fishCaughtAll[state]++;
		switch(state)
		{
			case 0 : 
				this.labelStatsSessionFail.setText(this.fishCaughtSession[state]+"");
				this.labelStatsAllFail.setText(this.fishCaughtAll[state]+"");
				break;
			case 1 : 
				this.labelStatsSessionFish1.setText(this.fishCaughtSession[state]+"");
				this.labelStatsAllFish1.setText(this.fishCaughtAll[state]+"");
				break;
			case 2 : 
				this.labelStatsSessionFish2.setText(this.fishCaughtSession[state]+"");
				this.labelStatsAllFish2.setText(this.fishCaughtAll[state]+"");
				break;
			case 3 : 
				this.labelStatsSessionFish3.setText(this.fishCaughtSession[state]+"");
				this.labelStatsAllFish3.setText(this.fishCaughtAll[state]+"");
				break;
			default :
				break;
		}
		
		this.labelStatsSessionTot.setText(this.getTotalFish(this.fishCaughtSession)+"");
		this.labelStatsAllTot.setText(this.getTotalFish(this.fishCaughtAll)+"");
		
		long currentFishingTime = (System.currentTimeMillis() - this.startTime);
		this.sessionFishingTime += currentFishingTime;
		this.fishingTime += currentFishingTime;

		this.formatFishingTime(this.labelStatsSessionTime, this.sessionFishingTime);
		this.formatFishingTime(this.labelStatsAllTime, this.fishingTime);
		this.startTime = System.currentTimeMillis();
	}
	
	private int getTotalFish(int[] tab)
	{
		int tot = 0;
		for(int i = 1; i < 4; i++) // Commence à 1 pour ne pas prendre les fails
			tot += tab[i];
		return tot;
	}
	
	private void formatFishingTime(JLabel label, long time)
	{
		if(time < 1)
		{
			label.setText("00:00:00");
			return;
		}

		Date date;
		try
		{
			date = FISHING_TIME_FORMAT_MS.parse(time+"");
			label.setText(FISHING_TIME_FORMAT.format(date));
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
	}

	private void buttonStatsSessionRazActionPerformed(ActionEvent e)
	{
		for(int i = 0; i < 4; i++)
			this.fishCaughtSession[i] = 0;
		this.labelStatsSessionFail.setText("0");
		this.labelStatsSessionFish1.setText("0");
		this.labelStatsSessionFish2.setText("0");
		this.labelStatsSessionFish3.setText("0");
		this.labelStatsSessionTot.setText("0");
		
		this.sessionFishingTime = 0;
		this.labelStatsSessionTime.setText("00:00:00");
	}

	private void buttonStatsAllRazActionPerformed(ActionEvent e)
	{
		for(int i = 0; i < 4; i++)
			this.fishCaughtAll[i] = 0;
		this.labelStatsAllFail.setText("0");
		this.labelStatsAllFish1.setText("0");
		this.labelStatsAllFish2.setText("0");
		this.labelStatsAllFish3.setText("0");
		this.labelStatsAllTot.setText("0");
		
		this.fishingTime = 0;
		this.labelStatsAllTime.setText("00:00:00");
	}

	private void buttonBaitAddActionPerformed(ActionEvent e)
	{
		new BaitWindow(this).run();
	}
	
	public void addBait(Bait bait)
	{
		for(Bait b:this.baitModel.getBaits())
			if(b.equals(bait))
				return;
		this.baitModel.addRow(bait);
		int last = this.baitModel.getRowCount()-1;
		this.tableBait.getSelectionModel().setSelectionInterval(last, last);
	}

	private void buttonBaitAddFishstickActionPerformed(ActionEvent e)
	{
		this.labelBaitLearnText.setText("Mettez votre souris sur votre canne à pêche");
		if(this.isFishstickLearningRobotRunning)
			return;
		
		this.isFishstickLearningRobotRunning = true;
		LearningRobot robot = new LearningRobot(this, 1, this.labelBaitLearnTime);
		robot.start();
	}
	
	@Override public void next(Point point, int interval, boolean isLast)
	{
		this.spinBaitX.setValue(point.x);
		this.spinBaitY.setValue(point.y);
		if(isLast)
		{
			this.isFishstickLearningRobotRunning = false;
			this.labelBaitLearnText.setText("Position enregistrée : x = " + point.x + ", y = " + point.y);
			this.labelBaitLearnTime.setText("");
		}
	}
	
	private void tableBaitKeyReleased(KeyEvent e)
	{
		TableCellEditor cell = this.tableBait.getCellEditor();
		if(cell != null)
			this.tableBait.getCellEditor().stopCellEditing();
		
		if(e.getKeyCode() != KeyEvent.VK_DELETE)
			return;
		
		int[] selectedIndices = this.tableBait.getSelectedRows();
		if(selectedIndices.length == 0)
			return;
		for(int i = selectedIndices.length - 1; i >= 0; i--)
			this.baitModel.removeRow(selectedIndices[i]);
	}

	private void tableLocationsKeyReleased(KeyEvent e)
	{
		TableCellEditor cell = this.tableLocations.getCellEditor();
		if(cell != null)
			this.tableLocations.getCellEditor().stopCellEditing();
		
		if(e.getKeyCode() != KeyEvent.VK_DELETE)
			return;
		
		int[] selectedIndices = this.tableLocations.getSelectedRows();
		if(selectedIndices.length == 0)
			return;
		for(int i = selectedIndices.length - 1; i >= 0; i--)
			if(this.locationModel.getRowCount() > 1)
				this.locationModel.removeRow(selectedIndices[i]);
	}

	private void buttonLogSaveActionPerformed(ActionEvent e)
	{
		String result = this.saveLog();
		if(result != null)
			this.printLog(result, this.logStyleBlue, true, true);
		else
			this.printLog("Erreur lors de la sauvegarde des logs", this.logStyleBlue, true, true);
	}

	private void textStopPressKeyKeyReleased(KeyEvent e)
	{
		this.labelStopPressCode.setText(e.getKeyCode()+"");
		this.textStopPressKey.setText(KeyEvent.getKeyText(e.getKeyCode()));
	}

	@SuppressWarnings("all")
	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Jupli Nuiras
		this.buttonRun = new JButton();
		this.labelCurentLocation = new JLabel();
		this.tabbedPane = new JTabbedPane();
		JPanel panelLocation = new JPanel();
		JScrollPane scrollTableLocation = new JScrollPane();
		this.tableLocations = new JTable();
		this.checkLocationConfirmation = new JCheckBox();
		JPanel panelStart = new JPanel();
		JLabel label1 = new JLabel();
		this.textStartFishingButton = new JTextField();
		JLabel label3 = new JLabel();
		this.labelStartCode = new JLabel();
		JLabel label2 = new JLabel();
		this.spinStartWaitingTime = new JSpinner();
		JLabel label11 = new JLabel();
		this.checkStartMouveMouse = new JCheckBox();
		JLabel label10 = new JLabel();
		this.spinStartMouveX = new JSpinner();
		JLabel label12 = new JLabel();
		this.spinStartMouveY = new JSpinner();
		this.label13 = new JLabel();
		this.label26 = new JLabel();
		this.spinStartR = new JSpinner();
		this.spinStartG = new JSpinner();
		this.spinStartB = new JSpinner();
		JPanel panelStop = new JPanel();
		this.label8 = new JLabel();
		this.checkStopTime = new JCheckBox();
		this.spinStopHours = new JSpinner();
		JLabel label4 = new JLabel();
		this.spinStopMinutes = new JSpinner();
		JLabel label5 = new JLabel();
		this.checkStopFish = new JCheckBox();
		this.spinStopFish = new JSpinner();
		JLabel label6 = new JLabel();
		this.checkStopFail = new JCheckBox();
		this.spinStopFail = new JSpinner();
		JLabel label7 = new JLabel();
		JLabel label9 = new JLabel();
		this.checkStopPressKey = new JCheckBox();
		this.textStopPressKey = new JTextField();
		this.label40 = new JLabel();
		this.labelStopPressCode = new JLabel();
		this.checkStopBot = new JCheckBox();
		this.checkStopRift = new JCheckBox();
		this.checkStopComputer = new JCheckBox();
		this.checkStopSaveLog = new JCheckBox();
		JPanel panelBait = new JPanel();
		this.checkBait = new JCheckBox();
		this.spinBaitNb = new JSpinner();
		JLabel label25 = new JLabel();
		this.labelBaitUsed = new JLabel();
		this.labelBaitLearnText = new JLabel();
		JScrollPane scrollBaitTable = new JScrollPane();
		this.tableBait = new JTable();
		JButton buttonBaitAdd = new JButton();
		this.label22 = new JLabel();
		this.label23 = new JLabel();
		this.spinBaitX = new JSpinner();
		JButton buttonBaitAddFishstick = new JButton();
		this.label24 = new JLabel();
		this.spinBaitY = new JSpinner();
		this.labelBaitLearnTime = new JLabel();
		JPanel panelStat = new JPanel();
		JLabel label20 = new JLabel();
		JSeparator separator2 = new JSeparator();
		JLabel label21 = new JLabel();
		JSeparator separator1 = new JSeparator();
		JLabel label14 = new JLabel();
		this.labelStatsSessionTime = new JLabel();
		this.labelStatsAllTime = new JLabel();
		JLabel label15 = new JLabel();
		this.labelStatsSessionTot = new JLabel();
		this.labelStatsAllTot = new JLabel();
		JLabel label16 = new JLabel();
		this.labelStatsSessionFish1 = new JLabel();
		this.labelStatsAllFish1 = new JLabel();
		JLabel label17 = new JLabel();
		this.labelStatsSessionFish2 = new JLabel();
		this.labelStatsAllFish2 = new JLabel();
		JLabel label18 = new JLabel();
		this.labelStatsSessionFish3 = new JLabel();
		this.labelStatsAllFish3 = new JLabel();
		JLabel label19 = new JLabel();
		this.labelStatsSessionFail = new JLabel();
		this.labelStatsAllFail = new JLabel();
		JButton buttonStatsSessionRaz = new JButton();
		JButton buttonStatsAllRaz = new JButton();
		JPanel panelLog = new JPanel();
		this.scrollLog = new JScrollPane();
		this.textLog = new JTextPane();
		JButton buttonLogCopy = new JButton();
		JButton buttonLogSave = new JButton();
		JButton buttonLogClear = new JButton();
		JButton buttonExit = new JButton();

		//======== this ========
		setTitle("Rift Fishing");
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"$ugap, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, $ugap",
			"$ugap, $lgap, default, $lgap, default:grow, $lgap, default, $lgap, $ugap"));

		//---- buttonRun ----
		this.buttonRun.setText("D\u00e9marrer");
		this.buttonRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRunActionPerformed(e);
			}
		});
		contentPane.add(this.buttonRun, CC.xy(3, 3));
		contentPane.add(this.labelCurentLocation, CC.xywh(5, 3, 3, 1));

		//======== tabbedPane ========

		//======== panelLocation ========

		// JFormDesigner evaluation mark
		panelLocation.setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), panelLocation.getBorder())); panelLocation.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		panelLocation.setLayout(new FormLayout(
			"default:grow",
			"default, $lgap, default"));

		//======== scrollTableLocation ========

		//---- tableLocations ----
		this.tableLocations.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				tableLocationsKeyReleased(e);
			}
		});
		scrollTableLocation.setViewportView(this.tableLocations);
		panelLocation.add(scrollTableLocation, CC.xy(1, 1));

		//---- checkLocationConfirmation ----
		this.checkLocationConfirmation.setText("Confirmation");
		this.checkLocationConfirmation.setToolTipText("Le robot va bien s'assure qu'il voit un remous avant de cliquer. A activer au Pic du pain de fer par exemple");
		panelLocation.add(this.checkLocationConfirmation, CC.xy(1, 3));
		this.tabbedPane.addTab("Endroits", panelLocation);


		//======== panelStart ========
		panelStart.setLayout(new FormLayout(
			"$ugap, 6*($lcgap, default), default, $lcgap, $ugap",
			"$ugap, 3*($lgap, default), $lgap, 12dlu, $lgap, $ugap"));

		//---- label1 ----
		label1.setText("Bouton de p\u00eache");
		panelStart.add(label1, CC.xywh(3, 3, 3, 1, CC.RIGHT, CC.DEFAULT));

		//---- textStartFishingButton ----
		this.textStartFishingButton.setText("1");
		this.textStartFishingButton.setToolTipText("Quel touche le robot doit-il presser pour lancer la canne \u00e0 p\u00eache ?");
		this.textStartFishingButton.setHorizontalAlignment(SwingConstants.RIGHT);
		this.textStartFishingButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				textFishingButtonKeyReleased(e);
			}
		});
		panelStart.add(this.textStartFishingButton, CC.xy(7, 3));

		//---- label3 ----
		label3.setText("Code : ");
		panelStart.add(label3, CC.xy(11, 3));

		//---- labelStartCode ----
		this.labelStartCode.setText("49");
		panelStart.add(this.labelStartCode, CC.xywh(12, 3, 2, 1));

		//---- label2 ----
		label2.setText("Temps au lancement");
		panelStart.add(label2, CC.xywh(3, 5, 3, 1, CC.RIGHT, CC.DEFAULT));

		//---- spinStartWaitingTime ----
		this.spinStartWaitingTime.setModel(new SpinnerNumberModel(1, 1, 60, 1));
		this.spinStartWaitingTime.setToolTipText("Temps avant la 1\u00e8re action du robot pour placer la souris correctement");
		panelStart.add(this.spinStartWaitingTime, CC.xy(7, 5));

		//---- label11 ----
		label11.setText("sec");
		panelStart.add(label11, CC.xy(11, 5));

		//---- checkStartMouveMouse ----
		this.checkStartMouveMouse.setText("D\u00e9placer souris");
		this.checkStartMouveMouse.setToolTipText("D\u00e9place l\u00e9g\u00e8rement la souris \u00e0 chaque poisson p\u00each\u00e9 autour du point initial");
		panelStart.add(this.checkStartMouveMouse, CC.xy(3, 7));

		//---- label10 ----
		label10.setText("X");
		panelStart.add(label10, CC.xy(5, 7));

		//---- spinStartMouveX ----
		this.spinStartMouveX.setModel(new SpinnerNumberModel(20, 0, 999, 1));
		panelStart.add(this.spinStartMouveX, CC.xy(7, 7));

		//---- label12 ----
		label12.setText("Y");
		panelStart.add(label12, CC.xy(9, 7));

		//---- spinStartMouveY ----
		this.spinStartMouveY.setModel(new SpinnerNumberModel(20, 0, 999, 1));
		panelStart.add(this.spinStartMouveY, CC.xy(11, 7));

		//---- label13 ----
		this.label13.setText("pixels");
		panelStart.add(this.label13, CC.xy(13, 7));

		//---- label26 ----
		this.label26.setText("Bouton de ramassage");
		panelStart.add(this.label26, CC.xywh(3, 9, 3, 1));

		//---- spinStartR ----
		this.spinStartR.setModel(new SpinnerNumberModel(85, 5, 250, 1));
		panelStart.add(this.spinStartR, CC.xy(7, 9));

		//---- spinStartG ----
		this.spinStartG.setModel(new SpinnerNumberModel(110, 10, 245, 1));
		panelStart.add(this.spinStartG, CC.xy(11, 9));

		//---- spinStartB ----
		this.spinStartB.setModel(new SpinnerNumberModel(100, 10, 245, 1));
		panelStart.add(this.spinStartB, CC.xy(13, 9));
		this.tabbedPane.addTab("D\u00e9marrage", panelStart);


		//======== panelStop ========
		panelStop.setLayout(new FormLayout(
			"$rgap, 5*(default, $lcgap), default:grow, $lcgap, $ugap",
			"$rgap, default, 3*($lgap, 10dlu), $lgap, 4dlu, $lgap, default, 5*($lgap, 10dlu), $lgap, $ugap"));

		//---- label8 ----
		this.label8.setText("Condition");
		panelStop.add(this.label8, CC.xywh(2, 2, 3, 1));

		//---- checkStopTime ----
		this.checkStopTime.setText("Apr\u00e8s");
		panelStop.add(this.checkStopTime, CC.xy(2, 4));

		//---- spinStopHours ----
		this.spinStopHours.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		panelStop.add(this.spinStopHours, CC.xy(4, 4));

		//---- label4 ----
		label4.setText("h");
		panelStop.add(label4, CC.xy(6, 4));

		//---- spinStopMinutes ----
		this.spinStopMinutes.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		panelStop.add(this.spinStopMinutes, CC.xy(8, 4));

		//---- label5 ----
		label5.setText("m de p\u00eache");
		panelStop.add(label5, CC.xywh(10, 4, 3, 1));

		//---- checkStopFish ----
		this.checkStopFish.setText("Apr\u00e8s");
		panelStop.add(this.checkStopFish, CC.xy(2, 6));

		//---- spinStopFish ----
		this.spinStopFish.setModel(new SpinnerNumberModel(0, 0, 9999, 1));
		panelStop.add(this.spinStopFish, CC.xy(4, 6));

		//---- label6 ----
		label6.setText("poissons p\u00each\u00e9s");
		panelStop.add(label6, CC.xywh(6, 6, 7, 1));

		//---- checkStopFail ----
		this.checkStopFail.setText("Apr\u00e8s");
		panelStop.add(this.checkStopFail, CC.xy(2, 8));

		//---- spinStopFail ----
		this.spinStopFail.setModel(new SpinnerNumberModel(0, 0, 9999, 1));
		panelStop.add(this.spinStopFail, CC.xy(4, 8));

		//---- label7 ----
		label7.setText("\u00e9checs \u00e0 la suite");
		panelStop.add(label7, CC.xywh(6, 8, 7, 1));

		//---- label9 ----
		label9.setText("Action");
		panelStop.add(label9, CC.xywh(2, 12, 3, 1));

		//---- checkStopPressKey ----
		this.checkStopPressKey.setText("Presser cette touche");
		panelStop.add(this.checkStopPressKey, CC.xywh(2, 14, 5, 1));

		//---- textStopPressKey ----
		this.textStopPressKey.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				textStopPressKeyKeyReleased(e);
			}
		});
		panelStop.add(this.textStopPressKey, CC.xy(8, 14));

		//---- label40 ----
		this.label40.setText("Code :");
		panelStop.add(this.label40, CC.xy(10, 14));
		panelStop.add(this.labelStopPressCode, CC.xy(12, 14));

		//---- checkStopBot ----
		this.checkStopBot.setText("Eteindre robot");
		panelStop.add(this.checkStopBot, CC.xywh(2, 16, 3, 1));

		//---- checkStopRift ----
		this.checkStopRift.setText("Eteindre Rift");
		panelStop.add(this.checkStopRift, CC.xywh(2, 18, 3, 1));

		//---- checkStopComputer ----
		this.checkStopComputer.setText("Eteindre ordinateur (Windows)");
		panelStop.add(this.checkStopComputer, CC.xywh(2, 20, 7, 1));

		//---- checkStopSaveLog ----
		this.checkStopSaveLog.setText("Sauver les logs");
		this.checkStopSaveLog.setToolTipText("Sauvegarde les logs dans le dossier logs");
		panelStop.add(this.checkStopSaveLog, CC.xywh(2, 22, 3, 1));
		this.tabbedPane.addTab("Arr\u00eat", panelStop);


		//======== panelBait ========
		panelBait.setLayout(new FormLayout(
			"$rgap, default, $lcgap, right:default, 3*($lcgap, default), $lcgap, default:grow, $lcgap, default, $rgap",
			"$rgap, 10dlu, $lgap, default, $lgap, 13dlu, $lgap, default, $lgap, 11dlu, $lgap, 10dlu, $lgap, $ugap"));
		((FormLayout)panelBait.getLayout()).setRowGroups(new int[][] {{2, 6}});

		//---- checkBait ----
		this.checkBait.setText("Appliquer");
		panelBait.add(this.checkBait, CC.xy(2, 2));

		//---- spinBaitNb ----
		this.spinBaitNb.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		panelBait.add(this.spinBaitNb, CC.xy(6, 2));

		//---- label25 ----
		label25.setText("x");
		panelBait.add(label25, CC.xy(8, 2));

		//---- labelBaitUsed ----
		this.labelBaitUsed.setForeground(new Color(51, 204, 0));
		panelBait.add(this.labelBaitUsed, CC.xywh(10, 2, 3, 1));

		//---- labelBaitLearnText ----
		this.labelBaitLearnText.setForeground(Color.red);
		panelBait.add(this.labelBaitLearnText, CC.xywh(2, 10, 13, 1));

		//======== scrollBaitTable ========

		//---- tableBait ----
		this.tableBait.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				tableBaitKeyReleased(e);
			}
		});
		scrollBaitTable.setViewportView(this.tableBait);
		panelBait.add(scrollBaitTable, CC.xywh(2, 4, 13, 1));

		//---- buttonBaitAdd ----
		buttonBaitAdd.setText("+ app\u00e2t");
		buttonBaitAdd.setToolTipText("Ajouter un app\u00e2t \u00e0 la liste");
		buttonBaitAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonBaitAddActionPerformed(e);
			}
		});
		panelBait.add(buttonBaitAdd, CC.xy(14, 2));

		//---- label22 ----
		this.label22.setText("Canne \u00e0 p\u00eache");
		panelBait.add(this.label22, CC.xy(2, 6));

		//---- label23 ----
		this.label23.setText("X");
		panelBait.add(this.label23, CC.xy(4, 6));

		//---- spinBaitX ----
		this.spinBaitX.setModel(new SpinnerNumberModel(0, -9999, 9999, 1));
		panelBait.add(this.spinBaitX, CC.xy(6, 6));

		//---- buttonBaitAddFishstick ----
		buttonBaitAddFishstick.setText("+ canne");
		buttonBaitAddFishstick.setToolTipText("Indiquez l'endroit de votre canne dans votre sac");
		buttonBaitAddFishstick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonBaitAddFishstickActionPerformed(e);
			}
		});
		panelBait.add(buttonBaitAddFishstick, CC.xy(14, 6));

		//---- label24 ----
		this.label24.setText("Y");
		panelBait.add(this.label24, CC.xy(4, 8));

		//---- spinBaitY ----
		this.spinBaitY.setModel(new SpinnerNumberModel(0, -9999, 9999, 1));
		panelBait.add(this.spinBaitY, CC.xy(6, 8));

		//---- labelBaitLearnTime ----
		this.labelBaitLearnTime.setForeground(new Color(255, 0, 51));
		panelBait.add(this.labelBaitLearnTime, CC.xywh(2, 12, 13, 1));
		this.tabbedPane.addTab("App\u00e2ts", panelBait);


		//======== panelStat ========
		panelStat.setLayout(new FormLayout(
			"$ugap, $lcgap, 16dlu, $lcgap, 40dlu, $lcgap, 7dlu, $lcgap, 25dlu, $lcgap, center:4dlu, $lcgap, default, $lcgap, $ugap",
			"$ugap, $lgap, default, $lgap, 6dlu, 6*($lgap, default), $lgap, 9dlu"));
		((FormLayout)panelStat.getLayout()).setColumnGroups(new int[][] {{9, 13}});

		//---- label20 ----
		label20.setText("Session");
		panelStat.add(label20, CC.xy(9, 3));

		//---- separator2 ----
		separator2.setOrientation(SwingConstants.VERTICAL);
		panelStat.add(separator2, CC.xywh(11, 3, 1, 15));

		//---- label21 ----
		label21.setText("En tout");
		panelStat.add(label21, CC.xywh(13, 3, 2, 1));
		panelStat.add(separator1, CC.xywh(3, 5, 12, 1));

		//---- label14 ----
		label14.setText("Temps de p\u00eache");
		panelStat.add(label14, CC.xywh(3, 7, 3, 1));

		//---- labelStatsSessionTime ----
		this.labelStatsSessionTime.setText("0");
		panelStat.add(this.labelStatsSessionTime, CC.xy(9, 7));

		//---- labelStatsAllTime ----
		this.labelStatsAllTime.setText("0");
		panelStat.add(this.labelStatsAllTime, CC.xywh(13, 7, 2, 1));

		//---- label15 ----
		label15.setText("Objets p\u00each\u00e9s");
		panelStat.add(label15, CC.xywh(3, 9, 3, 1));

		//---- labelStatsSessionTot ----
		this.labelStatsSessionTot.setText("0");
		panelStat.add(this.labelStatsSessionTot, CC.xy(9, 9));

		//---- labelStatsAllTot ----
		this.labelStatsAllTot.setText("0");
		panelStat.add(this.labelStatsAllTot, CC.xy(13, 9));

		//---- label16 ----
		label16.setText("En 1 coup");
		panelStat.add(label16, CC.xy(5, 11));

		//---- labelStatsSessionFish1 ----
		this.labelStatsSessionFish1.setText("0");
		panelStat.add(this.labelStatsSessionFish1, CC.xy(9, 11));

		//---- labelStatsAllFish1 ----
		this.labelStatsAllFish1.setText("0");
		panelStat.add(this.labelStatsAllFish1, CC.xywh(13, 11, 2, 1));

		//---- label17 ----
		label17.setText("En 2 coups");
		panelStat.add(label17, CC.xy(5, 13));

		//---- labelStatsSessionFish2 ----
		this.labelStatsSessionFish2.setText("0");
		panelStat.add(this.labelStatsSessionFish2, CC.xy(9, 13));

		//---- labelStatsAllFish2 ----
		this.labelStatsAllFish2.setText("0");
		panelStat.add(this.labelStatsAllFish2, CC.xywh(13, 13, 2, 1));

		//---- label18 ----
		label18.setText("En 3 coups");
		panelStat.add(label18, CC.xy(5, 15));

		//---- labelStatsSessionFish3 ----
		this.labelStatsSessionFish3.setText("0");
		panelStat.add(this.labelStatsSessionFish3, CC.xy(9, 15));

		//---- labelStatsAllFish3 ----
		this.labelStatsAllFish3.setText("0");
		panelStat.add(this.labelStatsAllFish3, CC.xywh(13, 15, 2, 1));

		//---- label19 ----
		label19.setText("Echecs");
		panelStat.add(label19, CC.xywh(3, 17, 3, 1));

		//---- labelStatsSessionFail ----
		this.labelStatsSessionFail.setText("0");
		panelStat.add(this.labelStatsSessionFail, CC.xy(9, 17));

		//---- labelStatsAllFail ----
		this.labelStatsAllFail.setText("0");
		panelStat.add(this.labelStatsAllFail, CC.xywh(13, 17, 2, 1));

		//---- buttonStatsSessionRaz ----
		buttonStatsSessionRaz.setText("R.A.Z");
		buttonStatsSessionRaz.setToolTipText("Remise \u00e0 z\u00e9ro de la session");
		buttonStatsSessionRaz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatsSessionRazActionPerformed(e);
			}
		});
		panelStat.add(buttonStatsSessionRaz, CC.xy(9, 19));

		//---- buttonStatsAllRaz ----
		buttonStatsAllRaz.setText("R.A.Z");
		buttonStatsAllRaz.setToolTipText("Remise \u00e0 z\u00e9ro de tout vos poissons p\u00each\u00e9s");
		buttonStatsAllRaz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatsAllRazActionPerformed(e);
			}
		});
		panelStat.add(buttonStatsAllRaz, CC.xy(13, 19));
		this.tabbedPane.addTab("Stats", panelStat);


		//======== panelLog ========
		panelLog.setLayout(new FormLayout(
			"3*(default, $lcgap), default:grow",
			"fill:default:grow, $lgap, default"));
		((FormLayout)panelLog.getLayout()).setColumnGroups(new int[][] {{1, 3, 5}});

		//======== scrollLog ========

		//---- textLog ----
		this.textLog.setEditable(false);
		this.scrollLog.setViewportView(this.textLog);
		panelLog.add(this.scrollLog, CC.xywh(1, 1, 7, 1));

		//---- buttonLogCopy ----
		buttonLogCopy.setText("Copier");
		buttonLogCopy.setToolTipText("Copie les logs dans le presse-papier (ctrl-v pour les coller)");
		buttonLogCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonLogCopyActionPerformed(e);
			}
		});
		panelLog.add(buttonLogCopy, CC.xy(1, 3));

		//---- buttonLogSave ----
		buttonLogSave.setText("Sauver");
		buttonLogSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonLogSaveActionPerformed(e);
			}
		});
		panelLog.add(buttonLogSave, CC.xy(3, 3));

		//---- buttonLogClear ----
		buttonLogClear.setText("Effacer");
		buttonLogClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonLogClearActionPerformed(e);
			}
		});
		panelLog.add(buttonLogClear, CC.xy(5, 3));
		this.tabbedPane.addTab("Log", panelLog);

		contentPane.add(this.tabbedPane, CC.xywh(3, 5, 5, 1));

		//---- buttonExit ----
		buttonExit.setText("Quitter");
		buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonExitActionPerformed(e);
			}
		});
		contentPane.add(buttonExit, CC.xy(7, 7));
		setSize(485, 435);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	private void myInitComponents()
	{
		/* ---------- Log Style ------------- */
		Style defaut = this.textLog.getStyle("default");
		this.logStyleGray = this.textLog.addStyle("gray", defaut);
		//StyleConstants.setFontFamily(this.logStyleGray, "Comic sans MS");
		//StyleConstants.setFontSize(this.logStyleGray, 14);
		StyleConstants.setForeground(this.logStyleGray, Color.gray);

		this.logStyleBlack = this.textLog.addStyle("black", defaut);
		StyleConstants.setForeground(this.logStyleBlack, Color.black);

		this.logStyleBlue = this.textLog.addStyle("blue", defaut);
		StyleConstants.setForeground(this.logStyleBlue, Color.blue);

		this.logStyleLightBlue = this.textLog.addStyle("lightblue", defaut);
		StyleConstants.setForeground(this.logStyleLightBlue, new Color(100, 100, 200));

		this.logStyleRed = this.textLog.addStyle("red", defaut);
		StyleConstants.setForeground(this.logStyleRed, Color.red);

		this.logStyleGreen = this.textLog.addStyle("green", defaut);
		StyleConstants.setForeground(this.logStyleGreen, Color.green);
		
		this.logStyleLightGreen = this.textLog.addStyle("lightgreen", defaut);
		StyleConstants.setForeground(this.logStyleLightGreen, new Color(100, 200, 100));
		
		this.logStyleOrange = this.textLog.addStyle("orange", defaut);
		StyleConstants.setForeground(this.logStyleOrange, Color.orange);
		
		this.textLogDocument = this.textLog.getStyledDocument();

		/* ------------ Table model ----------- */
		this.locationModel = new LocationModel();
		this.tableLocations.setModel(this.locationModel);
		
		
		TableColumnModel columnModel = this.tableLocations.getColumnModel();
		int width = 30;
		columnModel.getColumn(1).setMaxWidth(width);
		columnModel.getColumn(2).setMaxWidth(width);
		columnModel.getColumn(3).setMaxWidth(width);
		
		this.baitModel = new BaitModel();
		this.tableBait.setModel(this.baitModel);
		BaitSelectionListener selectionListener = new BaitSelectionListener(this.tableBait, this.labelBaitUsed);
		this.tableBait.getSelectionModel().addListSelectionListener(selectionListener);
		this.tableBait.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);

		columnModel = this.tableBait.getColumnModel();
		columnModel.getColumn(1).setMaxWidth(40); // x
		columnModel.getColumn(2).setMaxWidth(40); // y
		columnModel.getColumn(3).setMaxWidth(70); // Util. max
		columnModel.getColumn(4).setMaxWidth(70); // Temps max
	}

	@Override public void windowClosing(WindowEvent arg0)
	{
		this.exit();
	}

	@Override public void windowActivated(WindowEvent arg0){}
	@Override public void windowClosed(WindowEvent arg0){}
	@Override public void windowDeactivated(WindowEvent arg0){}
	@Override public void windowDeiconified(WindowEvent arg0){}
	@Override public void windowIconified(WindowEvent arg0){}
	@Override public void windowOpened(WindowEvent arg0){}

	/* ******************** Preferences ******************** */
	private void readPreferenceFile(String file)
	{
		ArrayList<String> preferences = FileHelper.readFile(file);
		String[] returnParameters;
		
		for(String preference:preferences)
		{
			try
			{
				/* ------------ Window ------------ */
				if(preference.startsWith(SELECTED_TAB))
				{
					returnParameters = this.decodePreference(SELECTED_TAB, null, preference);
					if(returnParameters != null)
					{
						int selectedTab = new Integer(returnParameters[0]);
						if(selectedTab > 0 && selectedTab <= this.tabbedPane.getTabCount())
							this.tabbedPane.setSelectedIndex(selectedTab);
					}
				}
				else if(preference.startsWith(WINDOW_LOCATION))
				{
					returnParameters = this.decodePreference(WINDOW_LOCATION, new String[]{"x", "y"}, preference);
					if(returnParameters != null)
					{
						int x = new Integer(returnParameters[0]);
						int y = new Integer(returnParameters[1]);
						super.setLocation(x, y);
					}
				}
				else if(preference.startsWith(WINDOW_SIZE))
				{
					returnParameters = this.decodePreference(WINDOW_SIZE, new String[]{"w", "h"}, preference);
					if(returnParameters != null)
					{
						int w = new Integer(returnParameters[0]);
						int h = new Integer(returnParameters[1]);
						super.setSize(w, h);
					}
				}
				/* ------------ Location panel ------------ */
				else if(preference.startsWith(LOCATION_COLORS))
				{
					returnParameters = this.decodePreference(LOCATION_COLORS, new String[]{"name", "r", "g", "b"}, preference);
					if(returnParameters != null)
					{
						String name = returnParameters[0];
						int r = new Integer(returnParameters[1]);
						int g = new Integer(returnParameters[2]);
						int b = new Integer(returnParameters[3]);
						this.locationModel.addRow(new LocationColor(name, new Color(r, g, b)));
					}
				}
				else if(preference.startsWith(LOCATION_CONFIRMATION))
				{
					returnParameters = this.decodePreference(LOCATION_CONFIRMATION, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkLocationConfirmation.setSelected(val);
					}
				}
				else if(preference.startsWith(SELECTED_LOCATION))
				{
					returnParameters = this.decodePreference(SELECTED_LOCATION, null, preference);
					if(returnParameters != null)
					{
						int val = new Integer(returnParameters[0]);
						if(val == -1)
							val = 0;
						if(this.locationModel.getRowCount() > val)
							this.tableLocations.getSelectionModel().setSelectionInterval(val, val);
					}
				}
				/* ------------ Start panel ------------ */
				else if(preference.startsWith(START_FISHING_BUTTON_KEY))
				{
					returnParameters = this.decodePreference(START_FISHING_BUTTON_KEY, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.textStartFishingButton.setText(val+"");
					}
				}
				else if(preference.startsWith(START_FISHING_BUTTON_CODE))
				{
					returnParameters = this.decodePreference(START_FISHING_BUTTON_CODE, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.labelStartCode.setText(val+"");
					}
				}
				else if(preference.startsWith(START_WAITING_TIME))
				{
					returnParameters = this.decodePreference(START_WAITING_TIME, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinStartWaitingTime.setValue(val);
					}
				}
				else if(preference.startsWith(START_MOUSE_MOVE))
				{
					returnParameters = this.decodePreference(START_MOUSE_MOVE, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStartMouveMouse.setSelected(val);
					}
				}
				else if(preference.startsWith(START_MOUSE_MOUVE_X))
				{
					returnParameters = this.decodePreference(START_MOUSE_MOUVE_X, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinStartMouveX.setValue(val);
					}
				}
				else if(preference.startsWith(START_MOUSE_MOUVE_Y))
				{
					returnParameters = this.decodePreference(START_MOUSE_MOUVE_Y, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinStartMouveY.setValue(val);
					}
				}
				else if(preference.startsWith(START_LOOT_COLOR))
				{
					returnParameters = this.decodePreference(START_LOOT_COLOR, new String[]{"r", "g", "b"}, preference);
					if(returnParameters != null)
					{
						int r = new Integer(returnParameters[0]);
						int g = new Integer(returnParameters[1]);
						int b = new Integer(returnParameters[2]);
						this.spinStartR.setValue(r);
						this.spinStartG.setValue(g);
						this.spinStartB.setValue(b);
					}
				}
				/* ------------ Stop panel ------------ */
				else if(preference.startsWith(STOP_TIME))
				{
					returnParameters = this.decodePreference(STOP_TIME, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStopTime.setSelected(val);
					}
				}
				else if(preference.startsWith(STOP_HOURS))
				{
					returnParameters = this.decodePreference(STOP_HOURS, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinStopHours.setValue(val);
					}
				}
				else if(preference.startsWith(STOP_MINUTES))
				{
					returnParameters = this.decodePreference(STOP_MINUTES, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinStopMinutes.setValue(val);
					}
				}
				else if(preference.startsWith(STOP_FISH))
				{
					returnParameters = this.decodePreference(STOP_FISH, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStopFish.setSelected(val);
					}
				}
				else if(preference.startsWith(STOP_FISH_NB))
				{
					returnParameters = this.decodePreference(STOP_FISH_NB, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinStopFish.setValue(val);
					}
				}
				else if(preference.startsWith(STOP_FAIL))
				{
					returnParameters = this.decodePreference(STOP_FAIL, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStopFail.setSelected(val);
					}
				}
				else if(preference.startsWith(STOP_FAIL_NB))
				{
					returnParameters = this.decodePreference(STOP_FAIL_NB, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinStopFail.setValue(val);
					}
				}
				else if(preference.startsWith(STOP_PRESS_KEY))
				{
					returnParameters = this.decodePreference(STOP_PRESS_KEY, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStopPressKey.setSelected(val);
					}
				}
				else if(preference.startsWith(STOP_PRESS_KEY_VAL))
				{
					returnParameters = this.decodePreference(STOP_PRESS_KEY_VAL, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.textStopPressKey.setText(val+"");
					}
				}
				else if(preference.startsWith(STOP_PRESS_KEY_CODE))
				{
					returnParameters = this.decodePreference(STOP_PRESS_KEY_CODE, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.labelStopPressCode.setText(val+"");
					}
				}
				else if(preference.startsWith(STOP_BOT))
				{
					returnParameters = this.decodePreference(STOP_BOT, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStopBot.setSelected(val);
					}
				}
				else if(preference.startsWith(STOP_RIFT))
				{
					returnParameters = this.decodePreference(STOP_RIFT, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStopRift.setSelected(val);
					}
				}
				else if(preference.startsWith(STOP_COMPUTER))
				{
					returnParameters = this.decodePreference(STOP_COMPUTER, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStopComputer.setSelected(val);
					}
				}
				else if(preference.startsWith(STOP_SAVE_LOG))
				{
					returnParameters = this.decodePreference(STOP_SAVE_LOG, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkStopSaveLog.setSelected(val);
					}
				}
				/* ------------ Bait panel ------------ */
				else if(preference.startsWith(BAIT_USE))
				{
					returnParameters = this.decodePreference(BAIT_USE, null, preference);
					if(returnParameters != null)
					{
						Boolean val = new Boolean(returnParameters[0]);
						this.checkBait.setSelected(val);
					}
				}
				else if(preference.startsWith(BAIT_NB))
				{
					returnParameters = this.decodePreference(BAIT_NB, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinBaitNb.setValue(val);
					}
				}
				else if(preference.startsWith(BAIT))
				{
					returnParameters = this.decodePreference(BAIT, new String[]{"name", "x", "y", "durationTime", "durationNb"}, preference);
					if(returnParameters != null)
					{
						Bait bait = new Bait(returnParameters[0], new Integer(returnParameters[1]), new Integer(returnParameters[2]));
						bait.setDurationTime(new Integer(returnParameters[3]));
						bait.setDurationNb(new Integer(returnParameters[4]));
						this.baitModel.addRow(bait);
					}
				}
				else if(preference.startsWith(BAIT_FISHSTICK_X))
				{
					returnParameters = this.decodePreference(BAIT_FISHSTICK_X, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinBaitX.setValue(val);
					}
				}
				else if(preference.startsWith(BAIT_FISHSTICK_Y))
				{
					returnParameters = this.decodePreference(BAIT_FISHSTICK_Y, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.spinBaitY.setValue(val);
					}
				}
				else if(preference.startsWith(SELECTED_BAIT))
				{
					returnParameters = this.decodePreference(SELECTED_BAIT, null, preference);
					if(returnParameters != null)
					{
						int val = new Integer(returnParameters[0]);
						if(val == -1)
							val = 0;
						if(this.baitModel.getRowCount() > val)
							this.tableBait.getSelectionModel().setSelectionInterval(val, val);
					}
				}
				/* ------------ Stat panel ------------ */
				else if(preference.startsWith(STATS_TIME))
				{
					returnParameters = this.decodePreference(STATS_TIME, null, preference);
					if(returnParameters != null)
					{
						this.fishingTime = new Long(returnParameters[0]);
						this.formatFishingTime(this.labelStatsSessionTime, 0);
						this.formatFishingTime(this.labelStatsAllTime, this.fishingTime);
					}
				}
				else if(preference.startsWith(STATS_FISH1))
				{
					returnParameters = this.decodePreference(STATS_FISH1, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.fishCaughtAll[1] = val;
						this.labelStatsAllFish1.setText(val+"");
					}
				}
				else if(preference.startsWith(STATS_FISH2))
				{
					returnParameters = this.decodePreference(STATS_FISH2, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.fishCaughtAll[2] = val;
						this.labelStatsAllFish2.setText(val+"");
					}
				}
				else if(preference.startsWith(STATS_FISH3))
				{
					returnParameters = this.decodePreference(STATS_FISH3, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.fishCaughtAll[3] = val;
						this.labelStatsAllFish3.setText(val+"");
					}
				}
				else if(preference.startsWith(STATS_FAIL))
				{
					returnParameters = this.decodePreference(STATS_FAIL, null, preference);
					if(returnParameters != null)
					{
						Integer val = new Integer(returnParameters[0]);
						this.fishCaughtAll[0] = val;
						this.labelStatsAllFail.setText(val+"");
					}
				}
			}
			catch(NumberFormatException e)
			{
				System.err.println("Erreur lors de la lecture du paramètre " + preference);
			}
		}
		this.labelStatsAllTot.setText(this.getTotalFish(this.fishCaughtAll)+"");
	}
	
	private String[] decodePreference(String preferenceName, String[] parametersName, String preference)
	{
		String[] split1;
		String[] split2;
		int nbParameters = parametersName == null ? 1 : parametersName.length;
		String[] returnParameters = new String[nbParameters];

		preference = preference.substring(preferenceName.length());
		if(parametersName == null)
		{
			returnParameters[0] = preference;
			return returnParameters;
		}
		
		split1 = preference.split(",");
		if(split1.length != nbParameters)
		{
			System.err.println("Problème lors de la lecture de " + preferenceName + split1.length + " paramètres trouvés au lieu de " + nbParameters);
			return null;
		}
		for(int i = 0; i < nbParameters; i++)
		{
			split2 = split1[i].split(parametersName[i]+"=");
			returnParameters[i] = split2[1];
		}
		
		return returnParameters;
	}
	
	private void writePreferenceFile(String file)
	{
		StringBuilder sb = new StringBuilder(5000);
		
		/* ------------ Window ------------ */
		sb.append(SELECTED_TAB);
		sb.append(this.tabbedPane.getSelectedIndex() + "\n");
		
		sb.append(WINDOW_LOCATION);
		sb.append("x=" + (int)super.getLocation().getX() + ",");
		sb.append("y=" + (int)super.getLocation().getY());
		sb.append("\n");
		
		sb.append(WINDOW_SIZE);
		sb.append("w=" + (int)super.getSize().getWidth() + ",");
		sb.append("h=" + (int)super.getSize().getHeight());
		sb.append("\n");
		
		/* ------------ Location panel ------------ */
		for(LocationColor l:this.locationModel.getlocationColors())
		{
			if(!l.toString().startsWith("name=Nouvel endroit,r="))
				sb.append(LOCATION_COLORS + l.toString() + "\n");
		}
		
		sb.append(LOCATION_CONFIRMATION + this.checkLocationConfirmation.isSelected() + "\n");
		sb.append(SELECTED_LOCATION + this.tableLocations.getSelectedRow() + "\n");
		
		/* ------------ Start panel ------------ */
		sb.append(START_FISHING_BUTTON_KEY + this.textStartFishingButton.getText() + "\n");
		sb.append(START_FISHING_BUTTON_CODE + this.labelStartCode.getText() + "\n");
		sb.append(START_WAITING_TIME + ComponentHelper.intValueOf(this.spinStartWaitingTime) + "\n");
		sb.append(START_MOUSE_MOVE + this.checkStartMouveMouse.isSelected() + "\n");
		sb.append(START_MOUSE_MOUVE_X + ComponentHelper.intValueOf(this.spinStartMouveX) + "\n");
		sb.append(START_MOUSE_MOUVE_Y + ComponentHelper.intValueOf(this.spinStartMouveY) + "\n");
		sb.append(START_LOOT_COLOR + "r="+ComponentHelper.intValueOf(this.spinStartR) + "," + "g="+ComponentHelper.intValueOf(this.spinStartG) + "," + "b="+ComponentHelper.intValueOf(this.spinStartB) + "\n");
		
		/* ------------ Stop panel ------------ */
		sb.append(STOP_TIME + this.checkStopTime.isSelected() + "\n");
		sb.append(STOP_HOURS + ComponentHelper.intValueOf(this.spinStopHours) + "\n");
		sb.append(STOP_MINUTES + ComponentHelper.intValueOf(this.spinStopMinutes) + "\n");
		sb.append(STOP_FISH + this.checkStopFish.isSelected() + "\n");
		sb.append(STOP_FISH_NB + ComponentHelper.intValueOf(this.spinStopFish) + "\n");
		sb.append(STOP_FAIL + this.checkStopFail.isSelected() + "\n");
		sb.append(STOP_FAIL_NB + ComponentHelper.intValueOf(this.spinStopFail) + "\n");
		sb.append(STOP_PRESS_KEY + this.checkStopPressKey.isSelected() + "\n");
		sb.append(STOP_PRESS_KEY_VAL + this.textStopPressKey.getText() + "\n");
		sb.append(STOP_PRESS_KEY_CODE + this.labelStopPressCode.getText() + "\n");
		sb.append(STOP_BOT + this.checkStopBot.isSelected() + "\n");
		sb.append(STOP_RIFT + this.checkStopRift.isSelected() + "\n");
		sb.append(STOP_COMPUTER + this.checkStopComputer.isSelected() + "\n");
		sb.append(STOP_SAVE_LOG + this.checkStopSaveLog.isSelected() + "\n");
		
		/* ------------ Bait panel ------------ */
		sb.append(BAIT_USE + this.checkBait.isSelected() + "\n");
		sb.append(BAIT_NB + ComponentHelper.intValueOf(this.spinBaitNb) + "\n");
		for(Bait b:this.baitModel.getBaits())
			sb.append(BAIT + b.toString() + "\n");
		sb.append(BAIT_FISHSTICK_X + ComponentHelper.intValueOf(this.spinBaitX) + "\n");
		sb.append(BAIT_FISHSTICK_Y + ComponentHelper.intValueOf(this.spinBaitY) + "\n");
		sb.append(SELECTED_BAIT + this.tableBait.getSelectedRow() + "\n");
		
		/* ------------ Stat panel ------------ */
		sb.append(STATS_TIME + this.fishingTime + "\n");
		sb.append(STATS_FISH1 + this.fishCaughtAll[1] + "\n");
		sb.append(STATS_FISH2 + this.fishCaughtAll[2] + "\n");
		sb.append(STATS_FISH3 + this.fishCaughtAll[3] + "\n");
		sb.append(STATS_FAIL + this.fishCaughtAll[0] + "\n");

		FileHelper.writeFile(file, sb.toString());
	}

	/* ******************** Variables ******************** */
	private FishingRobot robot;
	private boolean isRunning;
	private LocationModel locationModel;
	private BaitModel baitModel;
	private StyledDocument textLogDocument;
	public Style logStyleGray;
	public Style logStyleBlack;
	public Style logStyleBlue;
	public Style logStyleLightBlue;
	public Style logStyleRed;
	public Style logStyleGreen;
	public Style logStyleLightGreen;
	public Style logStyleOrange;
	
	private int[] fishCaughtSession;
	private int[] fishCaughtAll;
	private long startTime;
	private long sessionFishingTime;
	private long fishingTime;
	
	private boolean isFishstickLearningRobotRunning;
	
	private static final boolean DEBUG = true;
	private static final String PREFERENCE_FILE = "data\\preferences.txt";
	private static final String LOG_DIR = "logs";
	
	private static final String SELECTED_TAB = "SELECTED_TAB:";
	
	private static final String WINDOW_LOCATION = "WINDOW_LOCATION:";
	private static final String WINDOW_SIZE = "WINDOW_SIZE:";
	private static final String LOCATION_COLORS = "LOCATION_COLORS:";
	private static final String LOCATION_CONFIRMATION = "LOCATION_CONFIRMATION:";
	private static final String SELECTED_LOCATION = "SELECTED_LOCATION:";

	private static final String START_FISHING_BUTTON_KEY = "START_FISHING_BUTTON_KEY:";
	private static final String START_FISHING_BUTTON_CODE = "START_FISHING_BUTTON_CODE:";
	private static final String START_WAITING_TIME = "START_WAITING_TIME:";
	private static final String START_MOUSE_MOVE = "START_MOUSE_MOVE:";
	private static final String START_MOUSE_MOUVE_X = "START_MOUSE_MOUVE_X:";
	private static final String START_MOUSE_MOUVE_Y = "START_MOUSE_MOUVE_Y:";
	private static final String START_LOOT_COLOR = "START_LOOT_COLOR:";
	
	private static final String STOP_TIME = "STOP_TIME:";
	private static final String STOP_HOURS = "STOP_HOURS:";
	private static final String STOP_MINUTES = "STOP_MINUTES:";

	private static final String STOP_FISH = "STOP_FISH:";
	private static final String STOP_FISH_NB = "STOP_FISH_NB:";
	private static final String STOP_FAIL = "STOP_FAIL:";
	private static final String STOP_FAIL_NB = "STOP_FAIL_NB:";
	private static final String STOP_BOT = "STOP_BOT:";
	private static final String STOP_PRESS_KEY = "STOP_PRESS_KEY:";
	private static final String STOP_PRESS_KEY_CODE = "STOP_PRESS_KEY_CODE:";
	private static final String STOP_PRESS_KEY_VAL = "STOP_PRESS_KEY_VAL:";
	private static final String STOP_RIFT = "STOP_RIFT:";
	private static final String STOP_COMPUTER = "STOP_COMPUTER:";
	private static final String STOP_SAVE_LOG = "STOP_SAVE_LOG:";
	
	private static final String BAIT = "BAIT:";
	private static final String BAIT_USE = "BAIT_USE:";
	private static final String BAIT_NB = "BAIT_NB:";
	private static final String BAIT_FISHSTICK_X = "BAIT_FISHSTICK_X:";
	private static final String BAIT_FISHSTICK_Y = "BAIT_FISHSTICK_Y:";
	private static final String SELECTED_BAIT = "SELECTED_BAIT:";
	
	private static final String STATS_TIME = "STATS_TIME:";
	private static final String STATS_FISH1 = "STATS_FISH1:";
	private static final String STATS_FISH2 = "STATS_FISH2:";
	private static final String STATS_FISH3 = "STATS_FISH3:";
	private static final String STATS_FAIL = "STATS_FAIL:";
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.S");
	private static final SimpleDateFormat FISHING_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat FISHING_TIME_FORMAT_MS = new SimpleDateFormat("S");
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Jupli Nuiras
	private JButton buttonRun;
	private JLabel labelCurentLocation;
	private JTabbedPane tabbedPane;
	private JTable tableLocations;
	private JCheckBox checkLocationConfirmation;
	private JTextField textStartFishingButton;
	private JLabel labelStartCode;
	private JSpinner spinStartWaitingTime;
	private JCheckBox checkStartMouveMouse;
	private JSpinner spinStartMouveX;
	private JSpinner spinStartMouveY;
	private JLabel label13;
	private JLabel label26;
	private JSpinner spinStartR;
	private JSpinner spinStartG;
	private JSpinner spinStartB;
	private JLabel label8;
	private JCheckBox checkStopTime;
	private JSpinner spinStopHours;
	private JSpinner spinStopMinutes;
	private JCheckBox checkStopFish;
	private JSpinner spinStopFish;
	private JCheckBox checkStopFail;
	private JSpinner spinStopFail;
	private JCheckBox checkStopPressKey;
	private JTextField textStopPressKey;
	private JLabel label40;
	private JLabel labelStopPressCode;
	private JCheckBox checkStopBot;
	private JCheckBox checkStopRift;
	private JCheckBox checkStopComputer;
	private JCheckBox checkStopSaveLog;
	private JCheckBox checkBait;
	private JSpinner spinBaitNb;
	private JLabel labelBaitUsed;
	private JLabel labelBaitLearnText;
	private JTable tableBait;
	private JLabel label22;
	private JLabel label23;
	private JSpinner spinBaitX;
	private JLabel label24;
	private JSpinner spinBaitY;
	private JLabel labelBaitLearnTime;
	private JLabel labelStatsSessionTime;
	private JLabel labelStatsAllTime;
	private JLabel labelStatsSessionTot;
	private JLabel labelStatsAllTot;
	private JLabel labelStatsSessionFish1;
	private JLabel labelStatsAllFish1;
	private JLabel labelStatsSessionFish2;
	private JLabel labelStatsAllFish2;
	private JLabel labelStatsSessionFish3;
	private JLabel labelStatsAllFish3;
	private JLabel labelStatsSessionFail;
	private JLabel labelStatsAllFail;
	private JScrollPane scrollLog;
	private JTextPane textLog;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
