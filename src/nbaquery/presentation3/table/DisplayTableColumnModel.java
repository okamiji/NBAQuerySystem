package nbaquery.presentation3.table;

public interface DisplayTableColumnModel
{
	public DisplayTableColumn getColumn(int index);
	
	public int getColumnCount();
	
	public void onRepaint(DisplayTable table);
}
