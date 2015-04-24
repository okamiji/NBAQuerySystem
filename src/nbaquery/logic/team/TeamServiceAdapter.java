package nbaquery.logic.team;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.average_team.AverageTeam;
import nbaquery.logic.gross_team.GrossTeam;

public class TeamServiceAdapter implements TeamService
{
	protected GrossTeam gross;
	protected AverageTeam average;
	public TableHost tableHost;
	public String[] columnNames,oneTeamColumns;
	
	public TeamServiceAdapter(TableHost tableHost,
			GrossTeam gross, AverageTeam average, String[] columnNames,String[] oneTeamColumns)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.average = average;
		this.columnNames = columnNames;
		this.oneTeamColumns=oneTeamColumns;
		
		this.gross.getTable();
		this.average.getTable();
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
			{
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) returnValue[row][column] = value.toString();
			}
		tableHost.deleteTable("team_query_result");
		return returnValue;
	}

	@Override
	public String[][] searchForSeasonHotTeams(int head) {
		String[][] strs=this.searchForTeams(true, head, false);
		String[][] result=new String[5][strs[0].length];
		for(int i=0;i<5;i++)
			for(int j=0;j<strs[i].length;j++)
				result[i][j]=strs[i][j];
		return result;
	}

	@Override
	public String[] searchForOneTeam(String teamNameAbbr, boolean isAbbr) {
		Table team=tableHost.getTable("team");
		SortQuery sort = new SortQuery(this.gross.getTable(), oneTeamColumns[0], true);
		tableHost.performQuery(sort, "team_query_result");
		Table queryResult = tableHost.getTable("team_query_result");
//		Column[] columns=queryResult.getColumns().toArray(new Column[0]);
	//	for(Column c:columns)
		//	System.out.print(" "+c.getColumnName());
		
		SelectProjectQuery query = null;
		try {
			if(isAbbr) query = new SelectProjectQuery("team_query_result.TEAM_NAME_ABBR=='%1'".replace("%1", teamNameAbbr), queryResult);
			else query = new SelectProjectQuery("team_query_result.TEAM_NAME=='%1'".replace("%1", teamNameAbbr), queryResult);
		} catch (Exception e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tableHost.performQuery(query, "team_query_result");
		queryResult = tableHost.getTable("team_query_result");
	
		NaturalJoinQuery joinQuery = new NaturalJoinQuery(queryResult, team, new String[]{"team_name"}, new String[]{"team_name"});
		tableHost.performQuery(joinQuery, "team_query_result");
		queryResult = tableHost.getTable("team_query_result");
		
		Row[] rows = queryResult.getRows();
		String[] returnValue=new String[oneTeamColumns.length];
		if(rows.length == 0) return returnValue;
		
		Column[] columns = new Column[oneTeamColumns.length];
		for(int i = 0; i < oneTeamColumns.length; i ++)
			columns[i] = queryResult.getColumn(oneTeamColumns[i]);
		for(int column = 0; column < columns.length; column ++)
		{
			Object value = columns[column].getAttribute(rows[0]);
			if(value != null) returnValue[column] = value.toString();
		}
		
		return returnValue;
	}
}
