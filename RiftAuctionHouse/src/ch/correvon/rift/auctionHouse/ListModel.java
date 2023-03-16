package ch.correvon.rift.auctionHouse;

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
		return this.list; // TODO clone
	}

	@Override public int getColumnCount()
	{
		return 1;
	}

	@Override public int getRowCount()
	{
		return this.list.size();
	}

	@Override public String getValueAt(int rowIndex, int columnIndex)
	{
		if(rowIndex >= getRowCount())
			return null;
		
		//System.out.println("return "+list.get(rowIndex));
		return list.get(rowIndex);
	}
	
	private List<String> list;
}
