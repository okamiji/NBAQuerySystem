package nbaquery.presentation3.table;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;

import nbaquery.data.Column;
import nbaquery.data.Row;

public class DefaultTableColumn implements DisplayTableColumn
{
	public int padding = 20;
	
	public final String columnName;
	public final Column tableColumn;
	
	public DefaultTableColumn(String columnName, Column tableColumn)
	{
		this.columnName = columnName;
		this.tableColumn = tableColumn;
	}

	@Override
	public String getColumnName()
	{
		return this.columnName;
	}

	@Override
	public int getWidth(Graphics g)
	{
		return (int)(g.getFontMetrics().getStringBounds(columnName, g).getWidth() + padding);
	}

	protected JLabel displayComponent = new JLabel();
	
	@Override
	public Component render(DisplayTable table, Object value, int row, int column)
	{
		displayComponent.setText((this.tableColumn.getAttribute(((Row)value))).toString());
		return displayComponent;
	}
}
