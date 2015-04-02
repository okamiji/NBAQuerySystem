package nbaquery.logic.team;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.average_team.AverageTeam;
import nbaquery.logic.gross_team.GrossTeam;

public class TeamServiceAdapter implements TeamService
{
	protected GrossTeam gross;
	protected AverageTeam average;
	public TableHost tableHost;
	public String[] columnNames; 
	
	public TeamServiceAdapter(TableHost tableHost,
			GrossTeam gross, AverageTeam average, String[] columnNames)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.average = average;
		this.columnNames = columnNames;
	}
	
	public void destroy()
	{
		this.gross.destroy();
	}
	
	@Override
	public String[][] searchForTeams(boolean isGross, int head, boolean isUp)
	{
		if(head < 0) head = 1;	//Team name by default.
		if(head > columnNames.length) return null;
		SortQuery sort;
		if(isGross)
			sort = new SortQuery(this.gross.getTable(), columnNames[head], isUp);
		else sort = new SortQuery(this.average.getTable(), columnNames[head], isUp);
		tableHost.performQuery(sort, "team_query_result");
		Table queryResult = tableHost.getTable("team_query_result");
		Row[] rows = queryResult.getRows();
		String[][] returnValue = new String[rows.length][columnNames.length];
		Column[] columns = new Column[columnNames.length];
		for(int i = 0; i < columnNames.length; i ++)
			columns[i] = queryResult.getColumn(columnNames[i]);
		for(int row = 0; row < rows.length; row ++)
			for(int column = 0; column < columns.length; column ++)
				returnValue[row][column] = columns[column].getAttribute(rows[row]).toString();
		tableHost.deleteTable("team_query_result");
		return returnValue;
	}
}
