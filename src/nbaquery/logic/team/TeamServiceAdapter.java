package nbaquery.logic.team;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.NativeTablePipeline;
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
	public String[] searchForOneTeam(String teamName) {
		Table team=tableHost.getTable("team");
		SortQuery sort = new SortQuery(this.gross.getTable(), columnNames[0], true);
		tableHost.performQuery(sort, "team_query_result");
		Table queryResult = tableHost.getTable("team_query_result");
		
		SelectProjectQuery query = null;
		try {
			query = new SelectProjectQuery("team_query_result.TEAM_NAME=='"+teamName+"'",team);
		} catch (Exception e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sort=new SortQuery(team, "team_name");
		tableHost.performQuery(sort, "team_query_result");
		queryResult = tableHost.getTable("team_query_result");
		Row[] rows = queryResult.getRows();
		Object[] values=rows[0].getAttributes();
		String[] returnValue=new String[values.length];
		for(int i = 0; i < values.length; i ++){
			Object value=values[i];
			if(value != null)
				returnValue[i]=value.toString();
		}
		return returnValue;
	}
}
