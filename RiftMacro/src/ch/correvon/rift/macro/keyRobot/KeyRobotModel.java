package ch.correvon.rift.macro.keyRobot;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class KeyRobotModel extends AbstractTableModel
{
	/* ------------------------------------------------------------ *\
	|* 		  				Constructeur							*|
	\* ------------------------------------------------------------ */
	public KeyRobotModel()
	{
		this.keyRobots = new ArrayList<>(10);
	}

	/* ------------------------------------------------------------ *\
	|* 		  				Méthodes publiques						*|
	\* ------------------------------------------------------------ */
	public void add(KeyRobot keyRobot)
	{
		this.add(this.keyRobots.size(), keyRobot);
	}

	public void add(int row, KeyRobot keyRobot)
	{
		this.keyRobots.add(row, keyRobot);
		super.fireTableRowsInserted(row, row);
	}

	public void remove(int index)
	{
		this.keyRobots.remove(index);
		if(index < 0)
			index = 0;
		if(index >= this.getRowCount())
			index = this.getRowCount() - 1;
		super.fireTableRowsDeleted(index, index);
	}

	public void remove(KeyRobot keyRobot)
	{
		int index = this.keyRobots.indexOf(keyRobot);
		if(index < 0)
			return;
		this.remove(index);
		super.fireTableRowsDeleted(index, index);
	}

	public void clear()
	{
		int nbLines = this.keyRobots.size();
		this.keyRobots.clear();
		super.fireTableRowsDeleted(0, nbLines);
	}
	
	/* ----------------------------- *\
	|* 				Get 			 *|
	\* ----------------------------- */
	@Override public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override public int getRowCount()
	{
		return this.keyRobots.size();
	}

	@Override public String getColumnName(int column)
	{
		return columnNames[column];
	}

	@Override public Class<?> getColumnClass(int column)
	{
		switch(column)
		{
			case 2 : return Integer.class;
			default: return String.class;
		}
	}

	@Override public Object getValueAt(int row, int column)
	{
		KeyRobot keyRobot = this.keyRobots.get(row);
		switch(column)
		{
			case 0 : return keyRobot.getRobotName();
			case 1 : return KeyEvent.getKeyText(keyRobot.getKey());
			case 2 : return keyRobot.getKey();
			default: return null;
		}
	}

	public KeyRobot getKeyRobot(int row)
	{
		return this.keyRobots.get(row);
	}
	
	public List<KeyRobot> getKeyRobots()
	{
		return this.keyRobots;
	}
	
	/* ----------------------------- *\
	|* 				Is	 			 *|
	\* ----------------------------- */
	@Override public boolean isCellEditable(int row, int column)
	{
		return column != 2;
	}

	/* ----------------------------- *\
	|* 				Set 			 *|
	\* ----------------------------- */
	@Override public void setValueAt(Object aValue, int row, int column)
	{
		KeyRobot keyRobot = this.keyRobots.get(row);
		switch(column)
		{
			case 0 : keyRobot.setRobotName((String)aValue);break;
			case 1 :
				String str = (String)aValue;
				if(str.length() < 1)
					return;
				char c = (char)str.getBytes()[0];
				keyRobot.setKey(KeyEvent.getExtendedKeyCodeForChar(c));
				break;
			default: break;
		}
		super.fireTableRowsUpdated(row, row);
	}

	/* ------------------------------------------------------------ *\
	|* 		  				Méthodes privées						*|
	\* ------------------------------------------------------------ */

	/* ------------------------------------------------------------ *\
	|* 		  				Attributs privés						*|
	\* ------------------------------------------------------------ */
	private List<KeyRobot> keyRobots;

	/* ------------------------------------- *\
	|* 				Statiques	 			 *|
	\* ------------------------------------- */
	private static final String[] columnNames = { "Nom", "Touche", "Code"};
	private static final long serialVersionUID = 4805777148680435549L;
}
