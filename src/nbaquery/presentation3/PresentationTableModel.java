package nbaquery.presentation3;

import nbaquery.data.Table;
import nbaquery.presentation3.table.DefaultTableColumnModel;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;
import nbaquery.presentation3.table.DisplayTableColumnModel;
import nbaquery.presentation3.table.PagedDisplayTableModel;

public abstract class PresentationTableModel extends PagedDisplayTableModel implements DisplayTableColumnModel
{
	public final DefaultTableColumnModel columnModel = new DefaultTableColumnModel();

	@Override
	public DisplayTableColumn getColumn(int index)
	{
		return columnModel.getColumn(index);
	}

	@Override
	public int getColumnCount()
	{
		return columnModel.getColumnCount();
	}

	@Override
	public abstract void onRepaint(DisplayTable table);

	protected void updateTable(Table table)
	{
		this.setRow(table.getRows());
		this.columnModel.setTable(table);
	}
}
