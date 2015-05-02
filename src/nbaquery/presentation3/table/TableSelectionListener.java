package nbaquery.presentation3.table;

import java.awt.Point;

public interface TableSelectionListener
{
	public void onSelect(DisplayTable table, int row, int column, Object value, Point mousePoint);
}
