package ch.correvon.rift.fishing.windows.baitWindow;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class BaitModel extends AbstractTableModel
{
	/* ------------------------------------------------------------ *\
	|* 		  				Constructeur							*|
	\* ------------------------------------------------------------ */
	public BaitModel()
	{
		this.listBait = new ArrayList<Bait>(50);
	}

	/* ------------------------------------------------------------ *\
	|* 		  				Méthodes publiques						*|
	\* ------------------------------------------------------------ */
	public void addRow(Bait bait)
	{
		this.insertRow(this.listBait.size(), bait);
	}

	public void insertRow(int row, Bait bait)
	{
		this.listBait.add(row, bait);
		super.fireTableRowsInserted(row, row);
	}

	public void removeRow(int index)
	{
		this.listBait.remove(index);
		super.fireTableRowsDeleted(index, index);
	}

	public void removeRow(Bait bait)
	{
		int index = this.listBait.indexOf(bait);
		if(index < 0)
			return;
		this.removeRow(index);
		super.fireTableRowsDeleted(index, index);
	}

	public void clear()
	{
		int nbLines = this.listBait.size();
		this.listBait.clear();
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
		return this.listBait.size();
	}

	@Override public String getColumnName(int column)
	{
		return columnNames[column];
	}

	@Override public Class<?> getColumnClass(int column)
	{
		switch(column)
		{
			case 0:
				return String.class;
			default:
				return Integer.class;
		}
	}

	@Override public Object getValueAt(int row, int column)
	{
		Bait bait = this.listBait.get(row);
		switch(column)
		{
			case 0:
				return bait.getName();
			case 1:
				return bait.getX();
			case 2:
				return bait.getY();
			case 3:
				return bait.getDurationNb();
			case 4:
				return bait.getDurationTime();
			default:
				return null;
		}
	}

	public Bait getBait(int row)
	{
		return new Bait(this.listBait.get(row));
	}
	
	public ArrayList<Bait> getBaits()
	{
//		return this.listBait;
		ArrayList<Bait> clone = new ArrayList<Bait>(this.getRowCount());
		for(Bait l:this.listBait)
			clone.add(new Bait(l));
		return clone;
	}
	
	/* ----------------------------- *\
	|* 				Is	 			 *|
	\* ----------------------------- */
	@Override public boolean isCellEditable(int row, int column)
	{
		return true;
	}

	/* ----------------------------- *\
	|* 				Set 			 *|
	\* ----------------------------- */
	@Override public void setValueAt(Object aValue, int row, int column)
	{
		Bait bait = this.listBait.get(row);
		switch(column)
		{
			case 0:
				bait.setName((String)aValue);
				break;
			case 1:
				bait.setX((Integer)aValue);
				break;
			case 2:
				bait.setY((Integer)aValue);
				break;
			case 3:
				bait.setDurationNb((Integer)aValue);
				break;
			case 4:
				bait.setDurationTime((Integer)aValue);
				break;
			default: break;
		}
	}

	/* ------------------------------------------------------------ *\
	|* 		  				Méthodes privées						*|
	\* ------------------------------------------------------------ */

	/* ------------------------------------------------------------ *\
	|* 		  				Attributs privés						*|
	\* ------------------------------------------------------------ */
	private ArrayList<Bait> listBait;

	/* ------------------------------------- *\
	|* 				Statiques	 			 *|
	\* ------------------------------------- */
	private static final String[] columnNames = { "Nom", "x", "y", "Util. max", "Durée max"};
	private static final long serialVersionUID = 4805777148680435549L;
}
