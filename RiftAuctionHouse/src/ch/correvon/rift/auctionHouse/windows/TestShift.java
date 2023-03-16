/*
 * Created by JFormDesigner on Wed Jun 29 14:57:38 CEST 2011
 */

package ch.correvon.rift.auctionHouse.windows;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Loïc Correvon
 */
public class TestShift extends JFrame
{
	public TestShift()
	{
		initComponents();
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void run()
	{
		super.setVisible(true);
	}
	
	private void printLog(String text)
	{
		this.textAreaLog.append(text+"\n");
	}
	
	private String getIndentation()
	{
		StringBuilder indentation = new StringBuilder(10);
		for(int i = 0; i < this.indentation; i++)
			indentation.append("\t");
		return indentation.toString();
	}

	private void textInputKeyPressed(KeyEvent e)
	{
		if(e.getKeyCode() != this.lastKeyLogged)
		{
			this.lastKeyLogged = e.getKeyCode();
			printDelay();
			this.printLog("\n"+this.getIndentation()+"keyPressed : "+KeyEvent.getKeyText(e.getKeyCode()) + "(" + e.getKeyCode()+")");
		}
		
		if(e.getKeyCode() != this.lastKeyIndented)
		{
			this.lastKeyIndented = e.getKeyCode();
			this.indentation++;
		}
	}

	private void textInputKeyReleased(KeyEvent e)
	{
		this.lastKeyIndented = -1;
		this.lastKeyLogged = -1;
		this.indentation--;
		
		printDelay();
		this.printLog(this.getIndentation()+"keyReleased : "+KeyEvent.getKeyText(e.getKeyCode()) + "(" + e.getKeyCode()+")");
	}

	private void textInputKeyTyped(KeyEvent e)
	{
//		this.printLog("keyTyped : "+e.getKeyChar() + "(" + e.getKeyCode()+")");
	}

	private void buttonExitActionPerformed(ActionEvent e)
	{
		System.exit(0);
	}

	private void buttonEnterActionPerformed(ActionEvent e)
	{
		this.printLog("\n");
	}
	
	private void printDelay()
	{
		this.nowTime = System.currentTimeMillis() - lastTime;
//		nowTime -= lastTime;
		this.printLog(this.getIndentation() + nowTime + " ms");
		this.lastTime = System.currentTimeMillis();
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.textInput = new JTextField();
		JButton buttonEnter = new JButton();
		JButton buttonExit = new JButton();
		JScrollPane scrollLog = new JScrollPane();
		this.textAreaLog = new JTextArea();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle("Test Robot");
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"$ugap, $lcgap, default:grow, $lcgap, 67dlu, $lcgap, default, $lcgap, $ugap",
			"$ugap, default, $rgap, $lgap, fill:default:grow, $ugap"));

		//---- textInput ----
		this.textInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				textInputKeyPressed(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				textInputKeyReleased(e);
			}
			@Override
			public void keyTyped(KeyEvent e) {
				textInputKeyTyped(e);
			}
		});
		contentPane.add(this.textInput, cc.xy(3, 2));

		//---- buttonEnter ----
		buttonEnter.setText("Saut de ligne");
		buttonEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonEnterActionPerformed(e);
			}
		});
		contentPane.add(buttonEnter, cc.xy(5, 2));

		//---- buttonExit ----
		buttonExit.setText("Quitter");
		buttonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonExitActionPerformed(e);
			}
		});
		contentPane.add(buttonExit, cc.xy(7, 2));

		//======== scrollLog ========
		scrollLog.setViewportView(this.textAreaLog);
		contentPane.add(scrollLog, cc.xywh(3, 5, 5, 1));
		setSize(400, 300);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	private double lastTime = 0;
	private double nowTime = 0;
	
	private int indentation = 0;
	private int lastKeyIndented = -1;
	private int lastKeyLogged = -1;

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JTextField textInput;
	private JTextArea textAreaLog;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
