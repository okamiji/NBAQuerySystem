package nbaquery.logic.match;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;

public class MatchServiceAdapter extends NewMatchServiceAdapter implements MatchService{

	public String[] columnNames;
	
	public MatchServiceAdapter(TableHost tableHost)
	{
		this(tableHost,new String[]{
				"match_id",//�������
				"match_season",//����
				"match_date",//��������
				"match_host_abbr",//����������д
				"match_guest_abbr",//�ͳ�������д
				"match_host_score",//��������÷�
				"match_guest_score",//�ͳ�����÷�
				"team_name_abbr",//�����д
				"player_name",//��Ա����
				"player_position",//��Աλ��
				"game_time_minute",//�ϳ�ʱ�䣨���Ӳ��֣�
				"game_time_second",//�ϳ�ʱ�䣨���Ӳ��֣�
				"shoot_score",//���ֽ�����
				"shoot_count",//���ֳ�����
				"three_shoot_score",//���ֽ�����
				"three_shoot_count",//���ֳ�����
				"foul_shoot_score",//���������
				"foul_shoot_count",//���������
				"attack_board",//��������
				"defence_board",//��������
				"total_board",//������
				"assist",//����
				"steal",//����
				"cap",//��ñ
				"miss",//ʧ��
				"foul",//����
				"self_score",//���˵÷�
				"match_host_image",//
				"match_guest_image"//
		});
	}
	
	private MatchServiceAdapter(TableHost tableHost,String[] columnNames){
		super(tableHost);
		this.columnNames=columnNames;
	}
	
	@Override
	public String[][] searchForMatchs(int head, boolean isUp) {
		if(head < 0) head = 1;
		if(head > columnNames.length) return null;

		/*
		
		Table queryResult = tableHost.getTable("match");
		
		NaturalJoinQuery joinQuery = new NaturalJoinQuery(queryResult, tableHost.getTable("team"), new String[]{"match_host_abbr"}, new String[]{"team_name_abbr"});
		tableHost.performQuery(joinQuery, "match_query_result");
		queryResult = tableHost.getTable("match_query_result");
		
		GroupQuery group2 = new GroupQuery(queryResult,new String[]{
				"match_id",//�������
				"match_season",//����
				"match_date",//��������
				"match_host_abbr",//����������д
				"match_guest_abbr",//�ͳ�������д
				"match_host_score",//��������÷�
				"match_guest_score",//�ͳ�����÷�
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
		
		group2 = new GroupQuery(queryResult,new String[]{"match_id",//�������
				"match_season",//����
				"match_date",//��������
				"match_host_abbr",//����������д
				"match_guest_abbr",//�ͳ�������д
				"match_host_score",//��������÷�
				"match_guest_score",//�ͳ�����÷�
				"match_host_image"//����logo
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
		*/
		
		Table queryResult = this.searchForMatchesTable(new String[]{columnNames[head]}, null, null, isUp);
		
		Cursor rows = queryResult.getRows();
		String[][] returnValue = new String[rows.getLength()][9];
		Column[] columns = new Column[columnNames.length];
		for(int i = 0; i < columnNames.length; i ++){
			if(queryResult.getColumn(columnNames[i])!=null)
				columns[i] = queryResult.getColumn(columnNames[i]);
			}
		for(int row = 0; row < rows.getLength(); row ++){
			rows.absolute(row);
			Row current = rows.next();
			for(int column = 0; column < 7; column ++)
				if(columns[column]!=null)
			{
				
				Object value = columns[column].getAttribute(current);
				if(value != null) returnValue[row][column] = value.toString();
			}
			for(int column = 27; column < 29; column ++)
				if(columns[column]!=null)
			{
				Object value = columns[column].getAttribute(current);
				if(value != null) returnValue[row][column-20] = value.toString();
			}
		}
	//	tableHost.deleteTable("match_query_result");
		return returnValue;
	}

	@Override
	public String[][] searchForOneMatchById(int matchID) {
		Table queryResult = searchPerformanceByID(matchID, new String[]{"team_name_abbr"}, true);
		return convertTableToStrings(queryResult);
	}
	
	@Override
	public String[][] searchForMatchsByPlayer(String player_name) {
		Table queryResult = searchMatchesByPlayer(player_name);
		//tableHost.deleteTable("match_query_result_player");
		return convertTableToStrings(queryResult);
	}
	
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
		Table table = searchPerformanceByID(matchID, new String[]{"team_name_abbr"}, true);
		try {
			query = new SelectProjectQuery("player_name='" + player_name + "'", table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_player_and_id_select");
		Table queryResult = tableHost.getTable("match_query_result_player_and_id_select");
		return convertTableToStrings(queryResult);
	}
	
	public String[][] searchOneMatchByTeamAndID(String team_name_abbr,int matchID){
		SelectProjectQuery query = null;
		Table table = searchPerformanceByID(matchID, new String[]{"team_name_abbr"}, true);
		try {
			query = new SelectProjectQuery("team_name_abbr='" + team_name_abbr + "'", table);
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
			query = new SelectProjectQuery("team_name_abbr='" + team_name_abbr + "'", table);
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
		Cursor rows = queryResult.getRows();
		int columnNumber=columnNames.length;
		String[][] returnValue = new String[rows.getLength()][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++){
			columns[i] = queryResult.getColumn(columnNames[i]);
		}
	//	System.out.println("length1___"+rows.length);
		for(int row = 0; row < rows.getLength(); row ++){
			rows.absolute(row);
			Row current = rows.next();
			for(int column = 0; column < columns.length; column ++)
			{
				if(columns[column]!=null){
				Object value = columns[column].getAttribute(current);
				if(value != null) 
					returnValue[row][column] = value.toString();
		//		System.out.print(columns[column].getColumnName()+"___"+value.getClass()+"______");
				}
			}/*System.out.println();*/}
//		System.out.println("length2___"+returnValue.length);
		return returnValue;
	}


}
