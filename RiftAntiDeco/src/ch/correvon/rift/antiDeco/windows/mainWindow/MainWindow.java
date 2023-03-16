/*
 * Created by JFormDesigner on Tue Mar 08 20:27:40 CET 2011
 */

package ch.correvon.rift.antiDeco.windows.mainWindow;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import ch.correvon.rift.antiDeco.tools.Helper;
import ch.correvon.rift.antiDeco.tools.KeyboardMacro;
import ch.correvon.rift.antiDeco.tools.MacroSequence;
import ch.correvon.rift.antiDeco.tools.MacroSequenceElement;
import ch.correvon.rift.antiDeco.tools.MacroSequenceElement.MacroSequenceElementType;

/**
 * @author Loïc Correvon
 */
public class MainWindow extends JFrame
{
	public MainWindow(String title)
	{
		initComponents();
		this.isStarted = false;
		this.model = new DefaultListModel<MacroSequenceElement>();
		this.listSequences.setModel(this.model);

		this.readPreferenceFile(PREFERENCE_FILE);

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setTitle(title);
		super.setVisible(false);
	}

	public void run()
	{
		super.setVisible(true);
	}

	public void exit()
	{
		super.dispose();
	}

	/* **************** file ************** */
	private void readPreferenceFile(String path)
	{
		try
		{
			FileReader fichier = new FileReader(path);
			BufferedReader buffer = new BufferedReader(fichier);

			String line = buffer.readLine();

			String[] tabSplit;
			String[] tabSequenceSplit;
			while(line != null)
			{
				tabSplit = line.split(":", 2);
				line = buffer.readLine(); // Read it here because we don't need line anymore.

				if(tabSplit.length != 2)
					System.err.println("Erreur dans la lecture du fichier de pr�f�rence : " + line);
				else
				{
					if(tabSplit[0].equals("spinRandomStart"))
					{
						try
						{
							this.spinRandomStart.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinRandomStart.setValue(100);
						}
					}
					else if(tabSplit[0].equals("spinRandomEnd"))
					{
						try
						{
							this.spinRandomEnd.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinRandomEnd.setValue(200);
						}
					}
					else if(tabSplit[0].equals("spinRepeatStart"))
					{
						try
						{
							this.spinRepeatStart.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinRepeatStart.setValue(200);
						}
					}
					else if(tabSplit[0].equals("spinRepeatEnd"))
					{
						try
						{
							this.spinRepeatEnd.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinRepeatEnd.setValue(200);
						}
					}
					else if(tabSplit[0].equals("spinKeyDelayStart"))
					{
						try
						{
							this.spinKeyDelayStart.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinKeyDelayStart.setValue(1);
						}
					}
					else if(tabSplit[0].equals("spinKeyDelayEnd"))
					{
						try
						{
							this.spinKeyDelayEnd.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinKeyDelayEnd.setValue(1);
						}
					}
					else if(tabSplit[0].equals("spinSleepBetweenTwoSequences"))
					{
						try
						{
							this.spinSleepBetweenTwoSequences.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinSleepBetweenTwoSequences.setValue(1);
						}
					}
					else if(tabSplit[0].equals("spinSleepBetweenTwoSequencesRandom"))
					{
						try
						{
							this.spinSleepBetweenTwoSequencesRandom.setValue(new Integer(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.spinSleepBetweenTwoSequencesRandom.setValue(10);
						}
					}
					else if(tabSplit[0].equals("checkShuffleSequence"))
					{
						try
						{
							this.checkShuffleSequence.setSelected(new Boolean(tabSplit[1]));
						}
						catch(NumberFormatException e)
						{
							this.checkShuffleSequence.setSelected(true);
						}
					}
					else if(tabSplit[0].equals("macroSequence")) // TODO implement STRING type 
					{
						String errorMessage = "La macro est incorrect, elle doit être du genre [TYPE;TOUCHE;TEMPS~TEMPS;REPETER~REPETER] : " + line;

						String value = tabSplit[1];
//						int suiteStart = value.indexOf("[");
//						int suiteEnd = value.indexOf("]");
//						if(suiteStart > 0 && suiteEnd > 0)
//						{
//							String suite = value.substring(suiteStart + 1, suiteEnd);
//							String[] suiteElements = suite.split(", ");
//
//							List<MacroSequenceElement> suite = new ArrayList<>();
//							MacroSequenceElementType type = MacroSequenceElementType.KEYBOARD;
//
//							suite.add(new MacroSequenceElement(type, KeyEvent.VK_ENTER, delayStart, delayEnd));
//
//							
//							for(String elementStr:suiteElements)
//							{
//								MacroSequenceElement element = this.parseSequenceElement(elementStr, errorMessage);
//							}
//							this.model.addElement(new MacroSequenceElement(suite, repeatStart, repeatEnd));
//							return;
//						}
						
						MacroSequenceElement element = this.parseSequenceElement(value, errorMessage);
						if(element != null)
							this.model.addElement(element);
					}
				}
			}
			buffer.close();
			fichier.close();

		}
		catch(IOException e)
		{
		}
	}
	
	private MacroSequenceElement parseSequenceElement(String value, String errorMessage)
	{
		String[] tabSequenceSplit = value.split(";");
		if(tabSequenceSplit.length != 4)
		{
			System.err.println(errorMessage);
			return null;
		}
		MacroSequenceElementType type = MacroSequenceElementType.valueOf(tabSequenceSplit[0]);
		
		String key = tabSequenceSplit[1];
		String[] delay = tabSequenceSplit[2].split("~");
		if(delay.length != 2)
		{
			System.err.println(errorMessage);
			return null;
		}
		int delayStart = Integer.parseInt(delay[0]);
		int delayEnd = Integer.parseInt(delay[1]);

		String[] repeat = tabSequenceSplit[3].split("~");
		if(repeat.length != 2)
		{
			System.err.println(errorMessage);
			return null;
		}
		int repeatStart = Integer.parseInt(repeat[0]);
		int repeatEnd = Integer.parseInt(repeat[1]);
		
		return this.validateSecuenceElement(type, key, delayStart, delayEnd, repeatStart, repeatEnd);
	}

	private void writePreferenceFile(String path)
	{
		try
		{
			FileWriter file = new FileWriter(path);
			StringBuilder stringBuilder = new StringBuilder(300);
			stringBuilder.append("spinRandomStart:" + this.spinRandomStart.getValue() + "\n");
			stringBuilder.append("spinRandomEnd:" + this.spinRandomEnd.getValue() + "\n");
			stringBuilder.append("spinRepeatStart:" + this.spinRepeatStart.getValue() + "\n");
			stringBuilder.append("spinRepeatEnd:" + this.spinRepeatEnd.getValue() + "\n");
			stringBuilder.append("spinKeyDelayStart:" + this.spinKeyDelayStart.getValue() + "\n");
			stringBuilder.append("spinKeyDelayEnd:" + this.spinKeyDelayEnd.getValue() + "\n");
			stringBuilder.append("spinSleepBetweenTwoSequences:" + this.spinSleepBetweenTwoSequences.getValue() + "\n");
			stringBuilder.append("spinSleepBetweenTwoSequencesRandom:" + this.spinSleepBetweenTwoSequencesRandom.getValue() + "\n");
			stringBuilder.append("checkShuffleSequence:" + this.checkShuffleSequence.isSelected() + "\n");

			int listSize = this.model.getSize();
			for(int i = 0; i < listSize; i++)
				stringBuilder.append("macroSequence:" + this.model.getElementAt(i).toString() + "\n");

			file.write(stringBuilder.toString());
			file.close();
		}
		catch(IOException e)
		{
			System.err.println("erreur lors de l'écriture du fichier de statistique : " + path);
			e.printStackTrace();
		}
	}

	/* ******************** GUI ******************** */

	private void buttonAddSequenceActionPerformed(ActionEvent e)
	{
		String key = this.labelKeyCode.getText();
		int delayStart = Helper.getSpinnerInt(this.spinRandomStart);
		int delayEnd = Helper.getSpinnerInt(this.spinRandomEnd);
		int repeatStart = Helper.getSpinnerInt(this.spinRepeatStart);
		int repeatEnd = Helper.getSpinnerInt(this.spinRepeatEnd);
//		if(repeatEnd < repeatStart)
//		{
//			repeatEnd = repeatStart;
//			this.spinRepeatEnd.setValue(repeatStart);
//		}

		MacroSequenceElement element = this.validateSecuenceElement(this.type, key, delayStart, delayEnd, repeatStart, repeatEnd);
		if(element ==  null)
			return;
		this.model.addElement(element);
		this.textKey.requestFocus();
	}

	private void buttonAFKActionPerformed(ActionEvent e)
	{
		List<MacroSequenceElement> afk = new ArrayList<>();
		int delayStart = Integer.parseInt(this.spinRandomStart.getValue().toString());
		int delayEnd = Integer.parseInt(this.spinRandomEnd.getValue().toString());
		int repeatStart = Integer.parseInt(this.spinRepeatStart.getValue().toString());
		int repeatEnd = Integer.parseInt(this.spinRepeatEnd.getValue().toString());
		this.type = MacroSequenceElementType.KEYBOARD;

		afk.add(new MacroSequenceElement(this.type, KeyEvent.VK_ENTER, delayStart, delayEnd));
		afk.add(new MacroSequenceElement(this.type, KeyEvent.VK_DIVIDE, delayStart, delayEnd)); // VK_SLASH bug
		afk.add(new MacroSequenceElement(this.type, KeyEvent.VK_A, delayStart, delayEnd));
		afk.add(new MacroSequenceElement(this.type, KeyEvent.VK_F, delayStart, delayEnd));
		afk.add(new MacroSequenceElement(this.type, KeyEvent.VK_K, delayStart, delayEnd));
		afk.add(new MacroSequenceElement(this.type, KeyEvent.VK_ENTER, delayStart, delayEnd));
		
		this.model.addElement(new MacroSequenceElement(afk, repeatStart, repeatEnd));
	}

	private void buttonDeleteSequenceActionPerformed(ActionEvent e)
	{
		this.removeSequence();
	}

	private void buttonSaveActionPerformed(ActionEvent e)
	{
		this.writePreferenceFile(PREFERENCE_FILE);
	}

	private void buttonStartActionPerformed(ActionEvent e)
	{
		if(this.isStarted)
		{
			this.isStarted = !this.isStarted;
			this.buttonStart.setText("Démarrer");
			this.macro.exit();
		}
		else
		{
			this.isStarted = !this.isStarted;
			this.buttonStart.setText("Arrêter");

			MacroSequence sequence = new MacroSequence();

			int listSize = this.model.getSize();
			for(int i = 0; i < listSize; i++)
				sequence.add(this.model.getElementAt(i));

			if(this.macro != null)
				this.macro.exit();
			int sleepBetweenTwoSequences = Helper.getSpinnerInt(this.spinSleepBetweenTwoSequences);
			int sleepBetweenTwoSequencesRandom = Helper.getSpinnerInt(this.spinSleepBetweenTwoSequencesRandom);

			int sleepBetweenTwoKeyStart = Helper.getSpinnerInt(this.spinKeyDelayStart);
			int sleepBetweenTwoKeyEnd = Helper.getSpinnerInt(this.spinKeyDelayEnd);

			this.macro = new KeyboardMacro(sequence, sleepBetweenTwoSequences * 60000, sleepBetweenTwoSequencesRandom * 1000, sleepBetweenTwoKeyStart, sleepBetweenTwoKeyEnd, this.checkShuffleSequence.isSelected());
			this.macro.addTextLogger(this.txtpaneLogs);
			this.macro.start();

			this.writePreferenceFile(PREFERENCE_FILE);
		}
	}

	private void buttonExitActionPerformed(ActionEvent e)
	{
		this.writePreferenceFile(PREFERENCE_FILE);
		if(this.macro != null)
			this.macro.exit();
		System.exit(0);
	}

	private void textKeyKeyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		this.textKey.setText(/*modifier + */KeyEvent.getKeyText(keyCode));
		this.labelKeyCode.setText(""/*+modifierCode+"+"*/ + keyCode + "");
		this.type = MacroSequenceElementType.KEYBOARD;
	}
	
	private void textKeyMouseReleased(MouseEvent e)
	{
		int button = e.getButton();
		this.textKey.setText(Helper.getMouseEventText(button));
		this.labelKeyCode.setText("" + button + "");
		this.type = MacroSequenceElementType.MOUSE;
	}

	private MacroSequenceElement validateSecuenceElement(MacroSequenceElementType type, String key, int delayStart, int delayEnd, int repeatStart, int repeatEnd)
	{
		if(key.isEmpty())
		{
			JOptionPane.showMessageDialog(this, "Veuillez choisir une touche", "erreur", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		int keyCode;
		try
		{
			keyCode = Integer.parseInt(key);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Impossible de convertir key : " + e.getMessage(), "erreur", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if(delayStart > delayEnd)
		{
			JOptionPane.showMessageDialog(this, "Le temps de début (" + delayStart + ") ne peut pas être plus grand que le temps de fin (" + delayEnd + ")", "erreur", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if(repeatStart > repeatEnd)
		{
			JOptionPane.showMessageDialog(this, "Le nombre de répétition minimal (" + repeatStart + ") ne peut pas être plus grand que le nombre de répétition maximal (" + repeatEnd + ")", "erreur", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return new MacroSequenceElement(type, keyCode, delayStart, delayEnd, repeatStart, repeatEnd);
	}

	private void listSequencesKeyReleased(KeyEvent e)
	{
		if(e.getKeyCode() != KeyEvent.VK_DELETE)
			return;

		this.removeSequence();
	}

	private void removeSequence()
	{
		int[] indices = this.listSequences.getSelectedIndices();
		if(indices.length == 0)
			return;

		for(int i = indices.length - 1; i >= 0; i--)
			this.model.remove(indices[i]);
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - oiusdflnlsdkjfoiuyclyxfj
		JPanel panelNewSequence = new JPanel();
		JLabel label4 = new JLabel();
		this.textKey = new JTextField();
		this.labelKeyCode = new JLabel();
		JLabel label7 = new JLabel();
		this.spinRandomStart = new JSpinner();
		JLabel label8 = new JLabel();
		this.spinRandomEnd = new JSpinner();
		JLabel label9 = new JLabel();
		this.label11 = new JLabel();
		this.spinRepeatStart = new JSpinner();
		this.spinRepeatEnd = new JSpinner();
		this.label12 = new JLabel();
		this.buttonAFK = new JButton();
		JButton buttonAddSequence = new JButton();
		this.label6 = new JLabel();
		this.spinKeyDelayStart = new JSpinner();
		this.label13 = new JLabel();
		this.spinKeyDelayEnd = new JSpinner();
		this.label14 = new JLabel();
		JLabel label3 = new JLabel();
		JButton buttonDeleteSequence = new JButton();
		this.label15 = new JLabel();
		JScrollPane scrollSequences = new JScrollPane();
		this.listSequences = new JList<>();
		JScrollPane scrollPaneLogs = new JScrollPane();
		this.txtpaneLogs = new JTextPane();
		JLabel label1 = new JLabel();
		this.spinSleepBetweenTwoSequences = new JSpinner();
		JLabel label2 = new JLabel();
		this.spinSleepBetweenTwoSequencesRandom = new JSpinner();
		JLabel label10 = new JLabel();
		this.checkShuffleSequence = new JCheckBox();
		this.buttonStart = new JButton();
		this.buttonSave = new JButton();
		JButton buttonExit = new JButton();

		//======== this ========
		setTitle("Rift Anti Deco");
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"$ugap, 2*($lcgap, default), $lcgap, default:grow, $lcgap, default, 2*($lcgap, default:grow), $lcgap, default, $lcgap, $ugap",
			"$ugap, 3*($lgap, default), $lgap, fill:default:grow, 3*($lgap, default), $lgap, $ugap"));

		//======== panelNewSequence ========
		panelNewSequence.setBorder(new TitledBorder("Nouvel s\u00e9quence"));

		panelNewSequence.setLayout(new FormLayout(
			"3dlu, right:default, 5*($lcgap, default)",
			"$ugap, 3*($lgap, default), $lgap, default:grow, $lgap, 4dlu"));

		//---- label4 ----
		label4.setText("Touche");
		label4.setHorizontalAlignment(SwingConstants.RIGHT);
		panelNewSequence.add(label4, CC.xy(4, 3));

		//---- textKey ----
		this.textKey.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				textKeyKeyReleased(e);
			}
		});
		this.textKey.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				textKeyMouseReleased(e);
			}
		});
		panelNewSequence.add(this.textKey, CC.xy(8, 3));
		panelNewSequence.add(this.labelKeyCode, CC.xy(10, 3));

		//---- label7 ----
		label7.setText("Rester press\u00e9 entre");
		panelNewSequence.add(label7, CC.xy(2, 5));

		//---- spinRandomStart ----
		this.spinRandomStart.setModel(new SpinnerNumberModel(100, 0, null, 50));
		panelNewSequence.add(this.spinRandomStart, CC.xy(4, 5));

		//---- label8 ----
		label8.setText("et");
		panelNewSequence.add(label8, CC.xy(6, 5));

		//---- spinRandomEnd ----
		this.spinRandomEnd.setModel(new SpinnerNumberModel(200, 0, null, 50));
		panelNewSequence.add(this.spinRandomEnd, CC.xy(8, 5));

		//---- label9 ----
		label9.setText("ms");
		panelNewSequence.add(label9, CC.xy(10, 5));

		//---- label11 ----
		this.label11.setText("R\u00e9peter entre");
		panelNewSequence.add(this.label11, CC.xy(2, 7));

		//---- spinRepeatStart ----
		this.spinRepeatStart.setModel(new SpinnerNumberModel(1, 0, null, 1));
		panelNewSequence.add(this.spinRepeatStart, CC.xy(4, 7));

		//---- spinRepeatEnd ----
		this.spinRepeatEnd.setModel(new SpinnerNumberModel(3, 1, null, 1));
		panelNewSequence.add(this.spinRepeatEnd, CC.xy(8, 7));

		//---- label12 ----
		this.label12.setText("fois");
		panelNewSequence.add(this.label12, CC.xy(10, 7));

		//---- buttonAFK ----
		this.buttonAFK.setText("/afk");
		this.buttonAFK.addActionListener(e -> buttonAFKActionPerformed(e));
		panelNewSequence.add(this.buttonAFK, CC.xy(4, 9));

		//---- buttonAddSequence ----
		buttonAddSequence.setText("Ajouter");
		buttonAddSequence.addActionListener(e -> buttonAddSequenceActionPerformed(e));
		panelNewSequence.add(buttonAddSequence, CC.xy(8, 9));
		contentPane.add(panelNewSequence, CC.xywh(3, 3, 6, 1));

		//---- label6 ----
		this.label6.setText("Temps entre 2 touches entre");
		contentPane.add(this.label6, CC.xywh(3, 5, 3, 1));

		//---- spinKeyDelayStart ----
		this.spinKeyDelayStart.setModel(new SpinnerNumberModel(100, 0, null, 50));
		contentPane.add(this.spinKeyDelayStart, CC.xy(7, 5));

		//---- label13 ----
		this.label13.setText("et");
		contentPane.add(this.label13, CC.xy(9, 5));

		//---- spinKeyDelayEnd ----
		this.spinKeyDelayEnd.setModel(new SpinnerNumberModel(500, 0, null, 50));
		contentPane.add(this.spinKeyDelayEnd, CC.xy(11, 5));

		//---- label14 ----
		this.label14.setText("ms");
		contentPane.add(this.label14, CC.xy(13, 5));

		//---- label3 ----
		label3.setText("S\u00e9quences");
		contentPane.add(label3, CC.xy(3, 7));

		//---- buttonDeleteSequence ----
		buttonDeleteSequence.setText("Supprimer touche");
		buttonDeleteSequence.addActionListener(e -> buttonDeleteSequenceActionPerformed(e));
		contentPane.add(buttonDeleteSequence, CC.xy(5, 7));

		//---- label15 ----
		this.label15.setText("Logs");
		contentPane.add(this.label15, CC.xy(7, 7));

		//======== scrollSequences ========

		//---- listSequences ----
		this.listSequences.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				listSequencesKeyReleased(e);
			}
		});
		scrollSequences.setViewportView(this.listSequences);
		contentPane.add(scrollSequences, CC.xywh(3, 9, 3, 1));

		//======== scrollPaneLogs ========
		scrollPaneLogs.setViewportView(this.txtpaneLogs);
		contentPane.add(scrollPaneLogs, CC.xywh(7, 9, 9, 1));

		//---- label1 ----
		label1.setText("Temps entre 2 s\u00e9quences");
		contentPane.add(label1, CC.xywh(3, 11, 3, 1));

		//---- spinSleepBetweenTwoSequences ----
		this.spinSleepBetweenTwoSequences.setModel(new SpinnerNumberModel(0, 0, null, 1));
		contentPane.add(this.spinSleepBetweenTwoSequences, CC.xy(7, 11));

		//---- label2 ----
		label2.setText("min, +/-");
		contentPane.add(label2, CC.xywh(9, 11, 3, 1));

		//---- spinSleepBetweenTwoSequencesRandom ----
		this.spinSleepBetweenTwoSequencesRandom.setModel(new SpinnerNumberModel(10, 0, null, 1));
		contentPane.add(this.spinSleepBetweenTwoSequencesRandom, CC.xy(13, 11));

		//---- label10 ----
		label10.setText("sec");
		contentPane.add(label10, CC.xy(15, 11));

		//---- checkShuffleSequence ----
		this.checkShuffleSequence.setText("M\u00e9langer chaque s\u00e9quence");
		contentPane.add(this.checkShuffleSequence, CC.xywh(3, 13, 3, 1));

		//---- buttonStart ----
		this.buttonStart.setText("D\u00e9mmarer");
		this.buttonStart.addActionListener(e -> buttonStartActionPerformed(e));
		contentPane.add(this.buttonStart, CC.xywh(3, 15, 3, 1));

		//---- buttonSave ----
		this.buttonSave.setText("Sauver param.");
		this.buttonSave.addActionListener(e -> buttonSaveActionPerformed(e));
		contentPane.add(this.buttonSave, CC.xywh(7, 15, 5, 1));

		//---- buttonExit ----
		buttonExit.setText("Quitter");
		buttonExit.addActionListener(e -> buttonExitActionPerformed(e));
		contentPane.add(buttonExit, CC.xywh(13, 15, 3, 1));
		setSize(630, 515);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	KeyboardMacro macro;
	DefaultListModel<MacroSequenceElement> model;
	private MacroSequenceElementType type;
	private boolean isStarted;
	private static final String PREFERENCE_FILE = "data" + File.separator + "preferences.txt";
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - oiusdflnlsdkjfoiuyclyxfj
	private JTextField textKey;
	private JLabel labelKeyCode;
	private JSpinner spinRandomStart;
	private JSpinner spinRandomEnd;
	private JLabel label11;
	private JSpinner spinRepeatStart;
	private JSpinner spinRepeatEnd;
	private JLabel label12;
	private JButton buttonAFK;
	private JLabel label6;
	private JSpinner spinKeyDelayStart;
	private JLabel label13;
	private JSpinner spinKeyDelayEnd;
	private JLabel label14;
	private JLabel label15;
	private JList<MacroSequenceElement> listSequences;
	private JTextPane txtpaneLogs;
	private JSpinner spinSleepBetweenTwoSequences;
	private JSpinner spinSleepBetweenTwoSequencesRandom;
	private JCheckBox checkShuffleSequence;
	private JButton buttonStart;
	private JButton buttonSave;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
