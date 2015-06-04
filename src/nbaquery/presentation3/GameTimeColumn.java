package nbaquery.presentation3;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;

import nbaquery.data.Row;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;

public class GameTimeColumn implements DisplayTableColumn
{

	@Override
	public String getColumnName()
	{
		return "\u6BD4\u8D5B\u65F6\u95F4";
	}

	@Override
	public int getWidth(Graphics g)
	{
		return 60;
	}

	JLabel textLabel = new JLabel();
	{
		textLabel.setHorizontalAlignment(JLabel.CENTER);
	}
	@Override
	public Component render(DisplayTable table, Object value, int row,
			int column)
	{		
		Row rowObject = (Row)value;
		Object second = rowObject.getDeclaredTable().getColumn("game_time").getAttribute(rowObject);
		if(second == null) textLabel.setText("");
		else if(second instanceof Float)
		{
			float secondValue = ((Float)second);
			int integerSeconds = (int)secondValue;
			
			textLabel.setText("%m'%s\""
					.replace("%m", Integer.toString(integerSeconds / 60))
					.replace("%s", Integer.toString(integerSeconds % 60)));
		}
		else if(second instanceof Integer)
		{
			int secondInt = (int) second;
			
			textLabel.setText("%m'%s\""
					.replace("%m", Integer.toString(secondInt / 60))
					.replace("%s", Integer.toString(secondInt % 60)));
		}
		return textLabel;
	}
	
}
