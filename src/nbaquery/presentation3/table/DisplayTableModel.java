package nbaquery.presentation3.table;

public interface DisplayTableModel
{
	public Object getValueAt(DisplayTable table, int row, int column);

	public int getRowCount();
	
	public void onRepaint(DisplayTable table);
}
