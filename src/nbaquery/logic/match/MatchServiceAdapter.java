package nbaquery.logic.match;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.infrustructure.DirectMatchNaturalJoinPerformance;

public class MatchServiceAdapter implements MatchService{

	TableHost tableHost;
	public String[] columnNames;
	
	public MatchServiceAdapter(TableHost tableHost,String[] columnNames){
		this.tableHost=tableHost;
		this.columnNames=columnNames;
	}
	
	@Override
	public String[][] searchForMatchs(int head, boolean isUp) {
		if(head < 0) head = 1;
		if(head > columnNames.length) return null;
		SortQuery sort = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		sort= new SortQuery(table, columnNames[head], isUp);
		
		tableHost.performQuery(sort, "match_query_result");
		Table queryResult = tableHost.getTable("match_query_result");
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
		tableHost.deleteTable("match_query_result");
		return returnValue;
	}

	@Override
	public String[][] searchForOneMatchById(int matchID) {
		Table queryResult = searchByID(matchID);
		tableHost.deleteTable("match_query_result_id");
		return convertTableToStrings(queryResult);
	}
	
	public Table searchByID(int matchID){
		SelectProjectQuery query = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		try {
			query = new SelectProjectQuery("match_natural_join_performance.MATCH_ID='" + matchID + "'", table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_id");
		Table queryResult = tableHost.getTable("match_query_result_id");
		return queryResult;
	}
	
	@Override
	public String[][] searchForMatchsByPlayer(String player_name) {
		Table queryResult = searchByPlayer(player_name);
		tableHost.deleteTable("match_query_result_player");
		return convertTableToStrings(queryResult);
	}
	
	public Table searchByPlayer(String player_name){
		SelectProjectQuery query = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		try {
			query = new SelectProjectQuery("match_natural_join_performance.PLAYER_NAME='" + player_name + "'", table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_player");
		Table queryResult = tableHost.getTable("match_query_result_player");
		return queryResult;
	}
	
	
	@Override
	public String[][] searchForMatchsByDate(String date) {
		Table queryResult = searchByDate(date);
		tableHost.deleteTable("match_query_result_date");
		return  convertTableToStrings(queryResult);
	}
	
	public Table searchByDate(String date){
		SelectProjectQuery query = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		try {
			query = new SelectProjectQuery("match_natural_join_performance.MATCH_DATE='" + date + "'", table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_date");
		Table queryResult = tableHost.getTable("match_query_result_date");
		return queryResult;
	}
	
	public String[][] searchOneMatchByPlayerAndID(String player_name,int matchID){
		SelectProjectQuery query = null;
		Table table = searchByID(matchID);
		try {
			query = new SelectProjectQuery("match_query_result_id.PLAYER_NAME='" + player_name + "'", table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_player_and_id");
		Table queryResult = tableHost.getTable("match_query_result_player_and_id");
		tableHost.deleteTable("match_query_result_player_and_id");
		return convertTableToStrings(queryResult);
	}
	
	public String[][] searchOneMatchByTeamAndID(String team_name_abbr,int matchID){
		SelectProjectQuery query = null;
		Table table = searchByID(matchID);
		try {
			query = new SelectProjectQuery("match_query_result_id.TEAM_NAME_ABBR='" + team_name_abbr + "'", table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_team_and_id");
		Table queryResult = tableHost.getTable("match_query_result_team_and_id");
		tableHost.deleteTable("match_query_result_team_and_id");
		return convertTableToStrings(queryResult);
	}
	
	public Table searchByTeam(String team_name_abbr){
		SelectProjectQuery query = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		try {
			query = new SelectProjectQuery("match_natural_join_performance.team_name_abbr='" + team_name_abbr + "'", table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_team");
		Table queryResult = tableHost.getTable("match_query_result_team");
		return queryResult;
	}
	
	public String[][] convertTableToStrings(Table queryResult){
		Row[] rows = queryResult.getRows();
		int columnNumber=columnNames.length;
		String[][] returnValue = new String[rows.length][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++){
			columns[i] = queryResult.getColumn(columnNames[i]);
		}
		for(int row = 0; row < rows.length; row ++)
			for(int column = 0; column < columns.length; column ++)
			{
				if(columns[column]!=null){
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) 
					returnValue[row][column] = value.toString();
				
				}
			}
		return returnValue;
	}


}
