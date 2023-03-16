/*
 * Created by JFormDesigner on Thu May 03 15:27:35 CEST 2012
 */

package ch.correvon.rift.fishing.windows.baitWindow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import ch.correvon.pumbaaUtils.helpers.ComponentHelper;
import ch.correvon.rift.fishing.tools.LearningRobot;
import ch.correvon.rift.fishing.windows.mainWindow.MainWindow;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Loïc Correvon
 */
public class BaitWindow extends JFrame implements LearningResultInterface
{
	public BaitWindow(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
		//this.mainWindowListBait = listBait;
		this.initComponents();
		this.isRunning = false;
		super.setVisible(false);
	}
	
	public void run()
	{
		super.setVisible(true);
	}
	
	private void buttonLearnActionPerformed(ActionEvent e)
	{
		this.labelLearnText.setText("<html><center>Mettez votre souris sur l'appât<br>de votre choix se trouvant dans votre sac</center></html>");
		if(this.isRunning)
			return;
		
		this.isRunning = true;
		LearningRobot robot = new LearningRobot(this, 1, this.labelLearnTime);
		robot.setInterval(6);
		robot.start();
	}

	@Override public void next(Point point, int interval, boolean isLast)
	{
		this.spinBaitX.setValue(point.x);
		this.spinBaitY.setValue(point.y);
		if(isLast)
		{
			this.isRunning = false;
			this.labelLearnText.setText("Position enregistrée : x = " + point.x + ", y = " + point.y);
			this.labelLearnTime.setText("");
		}
	}

	private void buttonAddActionPerformed(ActionEvent e)
	{
		Bait bait = new Bait(this.textBaitName.getText(), ComponentHelper.intValueOf(this.spinBaitX), ComponentHelper.intValueOf(this.spinBaitY));
		bait.setDurationTime(ComponentHelper.intValueOf(this.spinMaxTime));
		bait.setDurationNb(ComponentHelper.intValueOf(this.spinMaxUse));
		this.mainWindow.addBait(bait);
	}

	private void buttonCancelActionPerformed(ActionEvent e)
	{
		super.dispose();
	}

	@SuppressWarnings("all")
	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.labelLearnText = new JLabel();
		this.labelLearnTime = new JLabel();
		JButton buttonLearn = new JButton();
		JLabel label1 = new JLabel();
		this.textBaitName = new JTextField();
		JLabel label2 = new JLabel();
		JLabel label3 = new JLabel();
		this.spinBaitX = new JSpinner();
		JLabel label4 = new JLabel();
		this.spinBaitY = new JSpinner();
		JLabel label5 = new JLabel();
		this.spinMaxUse = new JSpinner();
		JLabel label6 = new JLabel();
		this.spinMaxTime = new JSpinner();
		JLabel label7 = new JLabel();
		this.buttonAdd = new JButton();
		JButton buttonCancel = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle("Rift Fishing - Nouvel app\u00e2t");
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"$ugap, $lcgap, 4*(default, $rgap), default, $lcgap, $ugap",
			"9dlu, 2*($lgap, 7dlu), 6*($lgap, default), $lgap, $ugap"));

		//---- labelLearnText ----
		this.labelLearnText.setForeground(new Color(255, 0, 51));
		this.labelLearnText.setText("Cliquez sur Apprendre et suivez les instructions");
		this.labelLearnText.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(this.labelLearnText, cc.xywh(3, 1, 9, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));

		//---- labelLearnTime ----
		this.labelLearnTime.setForeground(new Color(255, 0, 51));
		contentPane.add(this.labelLearnTime, cc.xywh(3, 5, 9, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

		//---- buttonLearn ----
		buttonLearn.setText("Apprendre");
		buttonLearn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonLearnActionPerformed(e);
			}
		});
		contentPane.add(buttonLearn, cc.xywh(7, 7, 5, 1));

		//---- label1 ----
		label1.setText("Nom");
		contentPane.add(label1, cc.xy(3, 9));
		contentPane.add(this.textBaitName, cc.xywh(7, 9, 5, 1));

		//---- label2 ----
		label2.setText("Position dans le sac");
		contentPane.add(label2, cc.xy(3, 11));

		//---- label3 ----
		label3.setText("X");
		contentPane.add(label3, cc.xy(5, 11));

		//---- spinBaitX ----
		this.spinBaitX.setModel(new SpinnerNumberModel(0, -9999, 9999, 1));
		contentPane.add(this.spinBaitX, cc.xy(7, 11));

		//---- label4 ----
		label4.setText("Y");
		contentPane.add(label4, cc.xy(9, 11));

		//---- spinBaitY ----
		this.spinBaitY.setModel(new SpinnerNumberModel(0, -9999, 9999, 1));
		contentPane.add(this.spinBaitY, cc.xy(11, 11));

		//---- label5 ----
		label5.setText("Nombre d'utilisation max");
		contentPane.add(label5, cc.xy(3, 13));

		//---- spinMaxUse ----
		this.spinMaxUse.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		contentPane.add(this.spinMaxUse, cc.xy(7, 13));

		//---- label6 ----
		label6.setText("Temps d'utilisation max");
		contentPane.add(label6, cc.xy(3, 15));

		//---- spinMaxTime ----
		this.spinMaxTime.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		contentPane.add(this.spinMaxTime, cc.xy(7, 15));

		//---- label7 ----
		label7.setText("minutes");
		contentPane.add(label7, cc.xy(11, 15));

		//---- buttonAdd ----
		this.buttonAdd.setText("Ajouter");
		this.buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonAddActionPerformed(e);
			}
		});
		contentPane.add(this.buttonAdd, cc.xy(7, 17));

		//---- buttonCancel ----
		buttonCancel.setText("Fermer");
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonCancelActionPerformed(e);
			}
		});
		contentPane.add(buttonCancel, cc.xy(11, 17));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private MainWindow mainWindow;
	private boolean isRunning;
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel labelLearnText;
	private JLabel labelLearnTime;
	private JTextField textBaitName;
	private JSpinner spinBaitX;
	private JSpinner spinBaitY;
	private JSpinner spinMaxUse;
	private JSpinner spinMaxTime;
	private JButton buttonAdd;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
}
