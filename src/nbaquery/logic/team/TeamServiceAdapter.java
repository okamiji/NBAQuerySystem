package nbaquery.logic.team;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.gross_team.GrossTeam;

public class TeamServiceAdapter implements TeamService
{
	protected GrossTeam gross;
	public TableHost tableHost;
	public String[] grossColumns; 
	
	public TeamServiceAdapter(TableHost tableHost, GrossTeam gross, String[] grossColumns)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.grossColumns = grossColumns;
	}
	
	public void destroy()
	{
		this.gross.destroy();
	}
	
	@Override
	public String[][] searchForTeams(boolean isGross, int head, boolean isUp)
	{
		if(head < 0) head = 1;	//Team name by default.
		if(head > grossColumns.length) return null;
		if(isGross)
		{
			SortQuery sort = new SortQuery(this.gross.getTable(), grossColumns[head], isUp);
			tableHost.performQuery(sort, "team_query_result");
			Table queryResult = tableHost.getTable("team_query_result");
			Row[] rows = queryResult.getRows();
			String[][] returnValue = new String[rows.length][grossColumns.length];
			Column[] columns = new Column[grossColumns.length];
			for(int i = 0; i < grossColumns.length; i ++)
				columns[i] = queryResult.getColumn(grossColumns[i]);
			for(int row = 0; row < rows.length; row ++)
				for(int column = 0; column < columns.length; column ++)
					returnValue[row][column] = columns[column].getAttribute(rows[row]).toString();
			tableHost.deleteTable("team_query_result");
			return returnValue;
		}
		else return null;
	}
}
