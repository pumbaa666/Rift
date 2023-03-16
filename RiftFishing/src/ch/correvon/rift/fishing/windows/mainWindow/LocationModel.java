package ch.correvon.rift.fishing.windows.mainWindow;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class LocationModel extends AbstractTableModel
{
	/* ------------------------------------------------------------ *\
	|* 		  				Constructeur							*|
	\* ------------------------------------------------------------ */
	public LocationModel()
	{
		this.listLocationColor = new ArrayList<LocationColor>(50);
//		this.listLocationColor.add(new LocationColor("Défaut", new Color(15, 15, 0)));
//		this.addRow(new LocationColor(NEW_LOCATION));
	}

	/* ------------------------------------------------------------ *\
	|* 		  				Méthodes publiques						*|
	\* ------------------------------------------------------------ */
	public void addRow(LocationColor locationColor)
	{
		this.insertRow(this.listLocationColor.size(), locationColor);
	}

	public void insertRow(int row, LocationColor locationColor)
	{
		this.listLocationColor.add(row, locationColor);
		super.fireTableRowsInserted(row, row);
	}

	public void removeRow(int index)
	{
		this.listLocationColor.remove(index);
		super.fireTableRowsDeleted(index, index);
	}

	public void removeRow(LocationColor locationColor)
	{
		int index = this.listLocationColor.indexOf(locationColor);
		if(index < 0)
			return;
		this.removeRow(index);
		super.fireTableRowsDeleted(index, index);
	}

	public void clear()
	{
		int nbLines = this.listLocationColor.size();
		this.listLocationColor.clear();
		super.fireTableRowsDeleted(0, nbLines);
	}
	
	public void addNewLocation()
	{
		this.addRow(new LocationColor(NEW_LOCATION));
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
		return this.listLocationColor.size();
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
		LocationColor locationColor = this.listLocationColor.get(row);
		switch(column)
		{
			case 0:
				return locationColor.getLocation();
			case 1:
				return locationColor.getColor().getRed();
			case 2:
				return locationColor.getColor().getGreen();
			case 3:
				return locationColor.getColor().getBlue();
			default:
				return null;
		}
	}

	public LocationColor getlocationColor(int row)
	{
		return new LocationColor(this.listLocationColor.get(row));
	}
	
	public ArrayList<LocationColor> getlocationColors()
	{
		ArrayList<LocationColor> clone = new ArrayList<LocationColor>(this.getRowCount());
		for(LocationColor l:this.listLocationColor)
			clone.add(new LocationColor(l));
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
		if(aValue == null)
			return;
		LocationColor locationColor = this.listLocationColor.get(row);
		Color newColor;
		Color currentColor = locationColor.getColor();
		boolean changed = false;
		try
		{
			switch(column)
			{
				case 0:
					if(locationColor.getLocation().equals(aValue))
						break;
					locationColor.setLocation((String)aValue);
					changed = true;
					break;
				case 1:
					if(locationColor.getColor().getRed() == (Integer)aValue)
						break;
					newColor = new Color((Integer)aValue, currentColor.getGreen(), currentColor.getBlue());
					locationColor.setColor(newColor);
					changed = true;
					break;
				case 2:
					if(locationColor.getColor().getGreen() == (Integer)aValue)
						break;
					newColor = new Color(currentColor.getRed(), (Integer)aValue, currentColor.getBlue());
					locationColor.setColor(newColor);
					changed = true;
					break;
				case 3:
					if(locationColor.getColor().getBlue() == (Integer)aValue)
						break;
					newColor = new Color(currentColor.getRed(), currentColor.getGreen(), (Integer)aValue);
					locationColor.setColor(newColor);
					changed = true;
					break;
			}
			if(row == this.getRowCount() - 1 && changed)
				this.addRow(new LocationColor(NEW_LOCATION));
		}
		catch(IllegalArgumentException e)
		{
			JOptionPane.showMessageDialog(null, "La composante couleur doit être comprise entre 0 et 255", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
//	public void setEditable(boolean isEditable)
//	{
//		this.isEditable = isEditable;
//	}

	/* ------------------------------------------------------------ *\
	|* 		  				Méthodes privées						*|
	\* ------------------------------------------------------------ */

	/* ------------------------------------------------------------ *\
	|* 		  				Attributs privés						*|
	\* ------------------------------------------------------------ */
	private ArrayList<LocationColor> listLocationColor;
	//private boolean isEditable;

	/* ------------------------------------- *\
	|* 				Statiques	 			 *|
	\* ------------------------------------- */
	private static final String[] columnNames = { "Lieu", "R", "G", "B" };
	private static final long serialVersionUID = 5906888148680435549L;
	private static final LocationColor NEW_LOCATION = new LocationColor("Nouvel endroit", new Color(12, 10, 0));
}
