package nbaquery.logic.team;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.GroupColumnInfo;

public class WinColumnInfo extends GroupColumnInfo
{
	public WinColumnInfo()
	{
		super("win", Integer.class);
	}

	Column match_host_abbr;
	Column match_host_score;
	Column match_guest_score;
	Column team_name_abbr;
	
	@Override
	public void retrieve(Table originalTable, Table resultTable)
	{
		match_host_abbr = originalTable.getColumn("match_host_abbr");
		match_host_score = originalTable.getColumn("match_host_score");
		match_guest_score = originalTable.getColumn("match_guest_score");
		team_name_abbr = originalTable.getColumn("team_name_abbr");
	}

	@Override
	public void collapse(Row[] rows, Row resultRow)
	{
		if(rows.length > 0)
		{
			Row row = rows[0];
			String host = (String) match_host_abbr.getAttribute(row);
			String team = (String) team_name_abbr.getAttribute(row);
			Integer host_score = (Integer) match_host_score.getAttribute(row);
			Integer guest_score = (Integer) match_guest_score.getAttribute(row);
			if(host.equals(team)) super.getGroupColumn().setAttribute(resultRow, host_score > guest_score? 1 : 0);
			else super.getGroupColumn().setAttribute(resultRow, host_score < guest_score? 1 : 0);
		}
	}
}