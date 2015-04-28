package nbaquery.presentation3.table;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;

public class DefaultTableColumn implements DisplayTableColumn
{
	public int padding = 20;
	
	public final String columnDisplayName;
	public final String columnName;
	public Column tableColumn;
	
	public DefaultTableColumn(String displayName, String columnName)
	{
		this.columnDisplayName = displayName;
		this.columnName = columnName;
	}
	
	public void setTable(Table table)
	{
		if(table != this.tableColumn.getDeclaringTable())
			this.tableColumn = table.getColumn(columnName); 
	}

	@Override
	public String getColumnName()
	{
		return this.columnDisplayName;
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
