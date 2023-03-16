/*
 * Created by JFormDesigner on Sun Jan 13 10:17:14 CET 2013
 */

package ch.correvon.rift.macro.windows.mainWindow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ch.correvon.pumbaaUtils.extendedObjects.MyJFrame;
import ch.correvon.pumbaaUtils.helpers.FileHelper;
import ch.correvon.rift.macro.keyRobot.KeyRobot;
import ch.correvon.rift.macro.keyRobot.KeyRobotModel;
import ch.correvon.rift.macro.keyboardHook.MyKeyAdapter;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.ksquared.system.keyboard.GlobalKeyListener;

/**
 * @author Loïc Correvon
 */
public class MainWindow extends MyJFrame implements TableModelListener, KeyListener
{
	public MainWindow()
	{
		super();
		this.initComponents();

		this.logStyleDefaut = this.textLog.getStyle("default");
		this.logStyleRed = this.textLog.addStyle("red", this.logStyleDefaut);
		StyleConstants.setForeground(this.logStyleRed, Color.red);
		this.textLogDocument = this.textLog.getStyledDocument();
		
	    this.defaultBackColor = this.tabPane.getBackgroundAt(1);

		this.keyAdapter = new MyKeyAdapter(this);
		this.model = new KeyRobotModel();
		this.model.addTableModelListener(this);
		this.tableKeyRobots.setModel(this.model);
		this.tableKeyRobots.addKeyListener(this);

		this.readPreferenceFile(PREFERENCE_FILE);
	}

	@Override public void tableChanged(TableModelEvent e)
	{
		int row = e.getFirstRow();
		KeyRobot keyRobot = this.model.getKeyRobot(row);
		if(keyRobot == null)
			return;

		this.keyAdapter.setKey(keyRobot, row + 1);
	}
	
	@Override public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() != KeyEvent.VK_DELETE)
			return;

		this.tableKeyRobots.editingCanceled(null);
		int[] rows = this.tableKeyRobots.getSelectedRows();

		for(int i = rows.length - 1; i >= 0; i--)
			this.model.remove(rows[i]);
	}

	@Override public void keyPressed(KeyEvent arg0){}
	@Override public void keyTyped(KeyEvent arg0){}
	
	@Override public void windowClosing(WindowEvent windowevent)
	{
		this.exit();
	}

	@Override public void run()
	{
		super.setVisible(true);
		new GlobalKeyListener().addKeyListener(this.keyAdapter);
	}

	public void printLog(String str)
	{
		print(str, this.logStyleDefaut);
	}

	public void printError(String str)
	{
		this.blinkLogTab();
		print(str, this.logStyleRed);
	}

	private void print(String str, Style style)
	{
		int lastLength = this.textLog.getStyledDocument().getLength();
		String formatedText = DATE_FORMAT.format(new Date()) + " : " + str + "\n";
		try
		{
			this.textLogDocument.insertString(lastLength, formatedText, style);
			this.textLog.setCaretPosition(lastLength);
		}
		catch(BadLocationException e)
		{
			e.printStackTrace();
		}
	}

	private void buttonExitActionPerformed(ActionEvent e)
	{
		this.exit();
	}
	
	private void exit()
	{
		this.keyAdapter.terminateAllRobots();
		this.writePreferenceFile(PREFERENCE_FILE);
		super.dispose();
		System.exit(0);
	}

	private void readPreferenceFile(String file)
	{
		ArrayList<String> preferences = FileHelper.readFile(file);
		String[] split;
		int code;
		int i = -1;
		for(String preference:preferences)
		{
			i++;
			split = preference.split(":");
			if(split.length != 2)
			{
				this.printError("Erreur à la ligne n°" + i + " : " + preference + " / 2 opérandes attendues");
				continue;
			}
			try
			{
				code = Integer.parseInt(split[1]);
			}
			catch(NumberFormatException e)
			{
				this.printError("Erreur à la ligne n°" + i + " : " + preference + " / Nombre attendu");
				continue;
			}

			KeyRobot keyRobot = new KeyRobot(code);
			keyRobot.setRobotName(split[0]);
			this.model.add(keyRobot);
		}
	}

	private void writePreferenceFile(String file)
	{
		StringBuilder sb = new StringBuilder(100);
		for(KeyRobot keyRobot:this.model.getKeyRobots())
			sb.append(keyRobot.getRobotName() + ":" + keyRobot.getKey() + "\n");
		FileHelper.writeFile(file, sb.toString());
	}

	private void buttonAddRobotActionPerformed(ActionEvent e)
	{
		this.model.add(new KeyRobot(0));
	}

	private void buttonClearLogActionPerformed(ActionEvent e)
	{
		this.textLog.setText("");
	}

	private void blinkLogTab()
	{
		if(this.tabPane.getSelectedIndex() == LOG_PANE)
			return;
		
		if(this.blinkTimer != null && this.blinkTimer.isRunning())
			return;
		
		this.blinkTimer = new Timer(500, new ActionListener()
		{
			boolean blinkFlag = false;

			@Override public void actionPerformed(ActionEvent e)
			{
				blink(blinkFlag);
				blinkFlag = !blinkFlag;
			}
		});
		this.blinkTimer.setInitialDelay(0);
		this.blinkTimer.start();
	}
	
	private void blink(boolean blinkFlag)
	{
		if(blinkFlag)
			this.tabPane.setBackgroundAt(LOG_PANE, Color.red);
		else
			this.tabPane.setBackgroundAt(LOG_PANE, this.defaultBackColor);
		this.tabPane.repaint();
	}

	private void tabPaneStateChanged(ChangeEvent e)
	{
		if(this.tabPane.getSelectedIndex() != LOG_PANE)
			return;
		
		this.tabPane.setBackgroundAt(LOG_PANE, this.defaultBackColor);
		if(this.blinkTimer != null)
			this.blinkTimer.stop();
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Jupli Nuiras
		this.tabPane = new JTabbedPane();
		JPanel panelRobot = new JPanel();
		JLabel label1 = new JLabel();
		this.scrollKeyRobots = new JScrollPane();
		this.tableKeyRobots = new JTable();
		JButton buttonAddRobot = new JButton();
		JPanel panelLogs = new JPanel();
		JButton buttonClearLog = new JButton();
		JScrollPane scrollLog = new JScrollPane();
		this.textLog = new JTextPane();
		JButton buttonExit = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle("RiftMacro");
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"$ugap, $lcgap, default:grow, $lcgap, default, $lcgap, $ugap",
			"$ugap, $lgap, fill:default:grow, $lgap, default, $lgap, $ugap"));

		//======== tabPane ========
		this.tabPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tabPaneStateChanged(e);
			}
		});

		//======== panelRobot ========

		panelRobot.setLayout(new FormLayout(
			"default:grow, $lcgap, default",
			"default, $lgap, default"));

		//---- label1 ----
		label1.setText("Ajouter");
		label1.setHorizontalAlignment(SwingConstants.RIGHT);
		panelRobot.add(label1, cc.xy(1, 1));

		//======== scrollKeyRobots ========
		this.scrollKeyRobots.setViewportView(this.tableKeyRobots);
		panelRobot.add(this.scrollKeyRobots, cc.xywh(1, 3, 3, 1));

		//---- buttonAddRobot ----
		buttonAddRobot.setText("+");
		buttonAddRobot.setMargin(new Insets(2, 2, 2, 2));
		buttonAddRobot.setMinimumSize(new Dimension(17, 13));
		buttonAddRobot.setToolTipText("Ajouter un robot");
		buttonAddRobot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAddRobotActionPerformed(e);
			}
		});
		panelRobot.add(buttonAddRobot, cc.xy(3, 1));
		this.tabPane.addTab("Robots", panelRobot);


		//======== panelLogs ========
		panelLogs.setLayout(new FormLayout(
			"default:grow, default",
			"default, fill:default:grow"));

		//---- buttonClearLog ----
		buttonClearLog.setText("Effacer");
		buttonClearLog.setMargin(new Insets(2, 2, 2, 2));
		buttonClearLog.setMaximumSize(new Dimension(51, 20));
		buttonClearLog.setMinimumSize(new Dimension(51, 20));
		buttonClearLog.setPreferredSize(new Dimension(51, 20));
		buttonClearLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonClearLogActionPerformed(e);
			}
		});
		panelLogs.add(buttonClearLog, cc.xy(2, 1));

		//======== scrollLog ========
		scrollLog.setViewportView(this.textLog);
		panelLogs.add(scrollLog, cc.xywh(1, 2, 2, 1));
		this.tabPane.addTab("Logs", panelLogs);

		contentPane.add(this.tabPane, cc.xywh(3, 3, 3, 1));

		//---- buttonExit ----
		buttonExit.setText("Quitter");
		buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonExitActionPerformed(e);
			}
		});
		contentPane.add(buttonExit, cc.xy(5, 5));
		setSize(460, 335);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private MyKeyAdapter keyAdapter;
	private KeyRobotModel model;

	private Style logStyleDefaut;
	private Style logStyleRed;
	private StyledDocument textLogDocument;
	
	private Timer blinkTimer;
    private final Color defaultBackColor;

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.S");
	private static final String PREFERENCE_FILE = "data\\preferences.txt";
	private static final int LOG_PANE = 1;
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Jupli Nuiras
	private JTabbedPane tabPane;
	private JScrollPane scrollKeyRobots;
	private JTable tableKeyRobots;
	private JTextPane textLog;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
