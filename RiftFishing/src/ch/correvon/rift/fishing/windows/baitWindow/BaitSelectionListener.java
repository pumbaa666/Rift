package ch.correvon.rift.fishing.windows.baitWindow;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class BaitSelectionListener implements ListSelectionListener
{
	// It is necessary to keep the table since it is not possible to determine the table from the event's source
	public BaitSelectionListener(JTable table, JLabel label)
	{
		this.table = table;
		this.model = (BaitModel)table.getModel();
		this.label = label;
	}

	@Override public void valueChanged(ListSelectionEvent e)
	{
//		// If cell selection is enabled, both row and column change events are fired
//		int first = -1;
//		int last = -1;
//		if(e.getSource() == this.table.getSelectionModel() && this.table.getRowSelectionAllowed())
//		{
//			// Column selection changed
//			first = e.getFirstIndex();
//			last = e.getLastIndex();
//			System.out.print("column : ");
//		}
//		else if(e.getSource() == this.table.getColumnModel().getSelectionModel() && this.table.getColumnSelectionAllowed())
//		{
//			// Row selection changed
//			first = e.getFirstIndex();
//			last = e.getLastIndex();
//			System.out.print("row : ");
//		}
//
//		if(e.getValueIsAdjusting())
//		{
//			// The mouse button has not yet been released
//		}
//
//		System.out.println("first = "+first+" / last = "+last);
		
		int selectedIndex = this.table.getSelectedRow();
		if(selectedIndex == -1)
			selectedIndex = 0;
		if(this.model.getRowCount() > 0)
			this.label.setText(this.model.getBait(selectedIndex).getName());
		else
			this.label.setText("");
	}
	
	private JTable table;
	private BaitModel model;
	private JLabel label;
}