package nbaquery.presentation3.match;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;

import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;

public class CalendarColumn implements DisplayTableColumn
{
	public static final String[] dayOfWeek = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
	public String columnName;
	public int width = 70;
	
	public CalendarColumn(int index)
	{
		columnName = dayOfWeek[index];
	}
	
	@Override
	public String getColumnName()
	{
		return columnName;
	}
	
	@Override
	public int getWidth(Graphics g)
	{
		return width;
	}

	@SuppressWarnings("serial")
	JLabel calendarDisplay = new JLabel()
	{
		{
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setVerticalAlignment(JLabel.CENTER);
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		}
	};
	
	static Color hasRecordLabel = Color.BLACK;
	static Color noRecordLabel = new Color(0.7f, 0.7f, 0.7f, 1.0f);
	
	@Override
	public Component render(DisplayTable table, Object value, int row,
			int column)
	{
		calendarDisplay.setText("");
		calendarDisplay.setForeground(noRecordLabel);
		if(value != null)
		{
			MatchStrip strip = (MatchStrip)value;
			calendarDisplay.setText(Integer.toString(strip.day));
			if(strip.matches != null) calendarDisplay.setForeground(hasRecordLabel);
		}
		return calendarDisplay;
	}
	
}
