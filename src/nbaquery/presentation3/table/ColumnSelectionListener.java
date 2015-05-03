package nbaquery.presentation3.table;

import java.awt.Point;

public interface ColumnSelectionListener
{
	public void onSelect(DisplayTable table, int column, Point mousePoint);
}
