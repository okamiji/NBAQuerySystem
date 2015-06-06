package nbaquery.presentation3.match;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;
import nbaquery.presentation3.table.DisplayTableColumnModel;
import nbaquery.presentation3.table.DisplayTableModel;

public class MatchTableModel implements DisplayTableModel, DisplayTableColumnModel
{
	public final ConcurrentHashMap<String, ArrayList<Row>> sortedMatch
		= new ConcurrentHashMap<String, ArrayList<Row>>();
	
	int year;	//The bigger number.
	int month;	//Start from 0.
	
	MatchStrip[][] values;
	CalendarColumn[] dayOfWeek = new CalendarColumn[7];
	
	int columnWidth;
	public MatchTableModel(int columnWidth)
	{
		this.columnWidth = columnWidth;
		for(int i = 0; i < 7; i ++)
		{
			dayOfWeek[i] = new CalendarColumn(i);
			dayOfWeek[i].width = columnWidth;
		}
	}
	
	@Override
	public Object getValueAt(DisplayTable table, int row, int column)
	{
		return values[row][column];
	}

	@Override
	public int getRowCount()
	{
		if(values == null) return 0;
		return values.length;
	}
	
	@Override
	public void onRepaint(DisplayTable table)
	{
		
	}
	
	public void setTable(Table theTable)
	{
		sortedMatch.clear();
		Column season = theTable.getColumn("match_season");
		Column date = theTable.getColumn("match_date");
		for(Row row : theTable)
		{
			String key = String.format("%s %s", (String) season.getAttribute(row), (String) date.getAttribute(row));
			ArrayList<Row> currentRows;
			if((currentRows = sortedMatch.get(key)) == null)
				sortedMatch.put(key, (currentRows = new ArrayList<Row>()));
			
			currentRows.add(row);
		}
	}
	
	Calendar calendar = Calendar.getInstance();
	
	public void switchToDate(int year, int month)
	{	
		ArrayList<MatchStrip[]> model = new ArrayList<MatchStrip[]>();
		
		calendar.set(year, month, 1);
		int firstDayInMonth = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
		
		int thisMonthDate = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.add(Calendar.MONTH, 1);
		int nextMonthDate = calendar.get(Calendar.DAY_OF_YEAR);
		
		int monthDays = nextMonthDate - thisMonthDate;
		if(monthDays < 0)
		{
			calendar.add(Calendar.DATE, -1);
			monthDays += calendar.get(Calendar.DAY_OF_YEAR);
		}
	
		monthDays -= 1;
		
		int actualCounter = 0;
		while(actualCounter < monthDays + firstDayInMonth)
		{
			MatchStrip[] sevenDay = new MatchStrip[7];
			for(int i = 0; i < 7; i ++)
			{
				int dayInMonth = actualCounter + i - firstDayInMonth;
				if(dayInMonth < 0 || dayInMonth > monthDays) sevenDay[i] = null;
				else sevenDay[i] = new MatchStrip(dayInMonth + 1, this.sortedMatch.get(formatDate(year, month, dayInMonth)));
			}
			actualCounter += 7;
			model.add(sevenDay);
		}
		values = model.toArray(new MatchStrip[0][0]);
	}
	
	public static String formatDate(int year, int month, int day)
	{
		String string;
		String before = ("00" + (year - 2001));
		String medium = ("00" + (year - 2000));
		String after = ("00" + (year - 1999));
		
		String monthStr = "00" + (month + 1);
		String dayStr = "00" + (day + 1);
		if(month >= 6)	//6 = Month July
			string = String.format("%s-%s %s-%s",
					medium.substring(medium.length() - 2), after.substring(after.length() - 2),
					monthStr.substring(monthStr.length() - 2), dayStr.substring(dayStr.length() - 2));
		else
			string = String.format("%s-%s %s-%s", before.substring(
					medium.length() - 2), medium.substring(after.length() - 2),
					monthStr.substring(monthStr.length() - 2), dayStr.substring(dayStr.length() - 2));
		return string;
	}

	@Override
	public DisplayTableColumn getColumn(int index)
	{
		return dayOfWeek[index];
	}

	@Override
	public int getColumnCount()
	{
		return 7;
	}
}
