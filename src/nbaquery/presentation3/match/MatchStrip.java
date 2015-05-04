package nbaquery.presentation3.match;

import java.util.ArrayList;

import nbaquery.data.Row;

public class MatchStrip
{
	int day;
	ArrayList<Row> matches;
	
	public MatchStrip(int day, ArrayList<Row> matches)
	{
		this.day = day;
		this.matches = matches;
	}
}
