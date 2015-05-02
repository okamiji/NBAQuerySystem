package nbaquery.presentation3;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;

public class DualTableColumn implements DisplayTableColumn
{
	public final String columnName;
	public final String format;
	
	public final String columnOne;
	public final String columnTwo;
	public int padding = 0;
	
	public DualTableColumn(String columnName, String columnOne, String columnTwo, String format)
	{
		this.columnName = columnName;
		this.columnOne = columnOne;
		this.columnTwo = columnTwo;
		this.format = format;
	}
	
	@Override
	public String getColumnName()
	{
		return this.columnName;
	}

	@Override
	public int getWidth(Graphics g)
	{
		return (int) g.getFontMetrics().getStringBounds(format, g).getWidth() + padding;
	}

	JLabel textLabel = new JLabel();
	{
		textLabel.setHorizontalAlignment(JLabel.CENTER);
	}
	
	@Override
	public Component render(DisplayTable table, Object value, int row, int column)
	{
		Row rowObject = (Row) value;
		Table dataTable = rowObject.getDeclaredTable();
		Column columnOne = dataTable.getColumn(this.columnOne);
		Column columnTwo = dataTable.getColumn(this.columnTwo);
		Object valueOne = columnOne.getAttribute(rowObject);
		Object valueTwo = columnTwo.getAttribute(rowObject);
		
		String valueOneString = "";
		if(valueOne != null) valueOneString = valueOne.toString();
		if(valueOne instanceof Float) if(valueOneString.length() > 3) 
			valueOneString = valueOneString.substring(0, 3);
		
		String valueTwoString = "";
		if(valueTwo != null) valueTwoString = valueTwo.toString();
		if(valueTwo instanceof Float) if(valueTwoString.length() > 3)
			valueTwoString = valueTwoString.substring(0, 3);
		textLabel.setText(format.replace("%1", valueOneString).replace("%2", valueTwoString));
		
		return textLabel;
	}

	
	
}
