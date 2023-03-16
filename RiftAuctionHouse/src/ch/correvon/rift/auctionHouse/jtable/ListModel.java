package ch.correvon.rift.auctionHouse.jtable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ListModel extends AbstractTableModel
{
	public ListModel()
	{
		this(50);
	}

	public ListModel(int size)
	{
		this.list = new ArrayList<String>(size);
	}
	
	public void add(String object)
	{
		this.list.add(object);
	}
	
	public void remove(int index)
	{
		this.list.remove(index);
		this.fireTableRowsDeleted(index, index);
	}
	
	public List<String> getValues()
	{
		List<String> clone = new ArrayList<String>(this.list.size());
		for(String l:this.list)
			clone.add(l);
		return clone;
	}

	@Override public String getColumnName(int column)
	{
		return this.columnNames[column];
	}
	
	@Override public int getColumnCount()
	{
		return this.columnNames.length;
	}

	@Override public int getRowCount()
	{
		return this.list.size();
	}

	@Override public String getValueAt(int rowIndex, int columnIndex)
	{
		if(rowIndex >= this.getRowCount())
			return null;
		
		return list.get(rowIndex);
	}
	
	private List<String> list;
	private String columnNames[] = { "Objet" };
}
