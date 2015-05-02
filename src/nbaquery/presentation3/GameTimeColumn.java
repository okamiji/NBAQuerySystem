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
		return "上场时间";
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
		Object minute = rowObject.getDeclaredTable().getColumn("game_time_minute").getAttribute(rowObject);
		Object second = rowObject.getDeclaredTable().getColumn("game_time_second").getAttribute(rowObject);
		if(minute == null || second == null) textLabel.setText("");
		else if(minute instanceof Float)
		{
			//Then second should be float
			float minuteFloat = (float) minute;
			float secondFloat = (float) second;
			
			float totalSeconds = minuteFloat * 60.0f + secondFloat;
			int integerSeconds = (int) totalSeconds;
			//float remainderSeconds = totalSeconds - integerSeconds;
			
			textLabel.setText("%m'%s\""
					.replace("%m", Integer.toString(integerSeconds / 60))
					.replace("%s", Integer.toString(integerSeconds % 60)));
		}
		else if(minute instanceof Integer)
		{
			//Then second should be integer.
			int minuteInt = (int) minute;
			int secondInt = (int) second;
			
			int totalSeconds = minuteInt * 60 + secondInt;
			
			textLabel.setText("%m'%s\""
					.replace("%m", Integer.toString(totalSeconds / 60))
					.replace("%s", Integer.toString(totalSeconds % 60)));
		}
		return textLabel;
	}
	
}
