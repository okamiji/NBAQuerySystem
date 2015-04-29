package nbaquery.logic.match;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;

public class MatchServiceAdapter implements MatchService{

	TableHost tableHost;
	public String[] columnNames;
	
	public MatchServiceAdapter(TableHost tableHost,String[] columnNames){
		this.tableHost = tableHost;
		this.columnNames=columnNames;
	}
	
	@Override
	public String[][] searchForMatchs(int head, boolean isUp) {
		if(head < 0) head = 1;
		if(head > columnNames.length) return null;
		
		Table queryResult = tableHost.getTable("match");
		
		NaturalJoinQuery joinQuery = new NaturalJoinQuery(queryResult, tableHost.getTable("team"), new String[]{"match_host_abbr"}, new String[]{"team_name_abbr"});
		tableHost.performQuery(joinQuery, "match_query_result");
		queryResult = tableHost.getTable("match_query_result");
		
		GroupQuery group2 = new GroupQuery(queryResult,new String[]{
				"match_id",//比赛编号
				"match_season",//赛季
				"match_date",//比赛日期
				"match_host_abbr",//主场队伍缩写
				"match_guest_abbr",//客场队伍缩写
				"match_host_score",//主场队伍得分
				"match_guest_score",//客场队伍得分
				},
		new GroupColumnInfo("match_host_image", String.class)
		{
			Column team_logo;

			@Override
			public void retrieve(Table originalTable,
					Table resultTable)
			{
				team_logo = originalTable.getColumn("team_logo");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow)
			{
				String sum = team_logo.getAttribute(rows[0]).toString();
				this.getGroupColumn().setAttribute(resultRow, sum);	
			}
			
		});
		tableHost.performQuery(group2, "match_query_result");
		queryResult = tableHost.getTable("match_query_result");
		
		joinQuery = new NaturalJoinQuery(queryResult, tableHost.getTable("team"), new String[]{"match_guest_abbr"}, new String[]{"team_name_abbr"});
		tableHost.performQuery(joinQuery, "match_query_result");
		queryResult = tableHost.getTable("match_query_result");
		
		group2 = new GroupQuery(queryResult,new String[]{"match_id",//比赛编号
				"match_season",//赛季
				"match_date",//比赛日期
				"match_host_abbr",//主场队伍缩写
				"match_guest_abbr",//客场队伍缩写
				"match_host_score",//主场队伍得分
				"match_guest_score",//客场队伍得分
				"match_host_image"//主队logo
				},
		new GroupColumnInfo("match_guest_image", String.class)
		{
			Column team_logo;

			@Override
			public void retrieve(Table originalTable,
					Table resultTable)
			{
				team_logo = originalTable.getColumn("team_logo");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow)
			{
				Object sum= team_logo.getAttribute(rows[0]).toString();
				getGroupColumn().setAttribute(resultRow, sum);	
			}
			
		});
		tableHost.performQuery(group2, "match_query_result");
		queryResult = tableHost.getTable("match_query_result");
		
		SortQuery sort = new SortQuery(queryResult, columnNames[head], isUp);
		tableHost.performQuery(sort, "match_query_result");
		queryResult = tableHost.getTable("match_query_result");
		
		Row[] rows = queryResult.getRows();
		String[][] returnValue = new String[rows.length][9];
		Column[] columns = new Column[columnNames.length];
		for(int i = 0; i < columnNames.length; i ++){
			if(queryResult.getColumn(columnNames[i])!=null)
				columns[i] = queryResult.getColumn(columnNames[i]);
			}
		for(int row = 0; row < rows.length; row ++){
			for(int column = 0; column < 7; column ++)
				if(columns[column]!=null)
			{
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) returnValue[row][column] = value.toString();
			}
			for(int column = 27; column < 29; column ++)
				if(columns[column]!=null)
			{
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) returnValue[row][column-20] = value.toString();
			}
		}
		tableHost.deleteTable("match_query_result");
		return returnValue;
	}

	@Override
	public String[][] searchForOneMatchById(int matchID) {
		Table queryResult = searchByID(matchID);
		return convertTableToStrings(queryResult);
	}
	
	public Table searchByID(Integer matchID){
		SelectProjectQuery query = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		try {
			query = new SelectProjectQuery("match_natural_join_performance.MATCH_ID="+matchID, table);
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
	
	//XXX you sb
	@Override
	public String[][] searchForMatchsByDateAndSeason(String date,String season) {
		Table queryResult = searchByDate(date);
		SelectProjectQuery query = null;
		try {
			query = new SelectProjectQuery("match_query_result_date.MATCH_SEASON='" + season + "'", queryResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_date");
		queryResult = tableHost.getTable("match_query_result_date");
		tableHost.deleteTable("match_query_result_date");
		return  convertTableToStrings(queryResult);
	}
	
	public Table searchByDate(String date){
		SelectProjectQuery query = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		try {
			query = new SelectProjectQuery("match_natural_join_performance.match_date=\"" + date + "\"", table);
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
		return convertTableToStrings(queryResult);
	}
	
	public String[][] searchForMatchsByTeam(String team_name_abbr){
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
		return convertTableToStrings(queryResult);
	}
	
	public String[][] convertTableToStrings(Table queryResult){
		Row[] rows = queryResult.getRows();
		int columnNumber=columnNames.length;
		String[][] returnValue = new String[rows.length][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++){
			columns[i] = queryResult.getColumn(columnNames[i]);
		}
	//	System.out.println("length1___"+rows.length);
		for(int row = 0; row < rows.length; row ++){
			for(int column = 0; column < columns.length; column ++)
			{
				if(columns[column]!=null){
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) 
					returnValue[row][column] = value.toString();
		//		System.out.print(columns[column].getColumnName()+"___"+value.getClass()+"______");
				}
			}System.out.println();}
//		System.out.println("length2___"+returnValue.length);
		return returnValue;
	}


}
