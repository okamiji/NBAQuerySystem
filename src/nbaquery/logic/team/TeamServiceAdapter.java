package nbaquery.logic.team;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.average_team.AverageTeam;
import nbaquery.logic.gross_team.GrossTeam;
import nbaquery.logic.hot_team_today.HotTeamToday;

public class TeamServiceAdapter extends NewTeamServiceAdapter implements TeamService, NewTeamService
{
	public String[] columnNames,oneTeamColumns;
	
	public TeamServiceAdapter(TableHost tableHost,
			GrossTeam gross, AverageTeam average, HotTeamToday hot)
	{
		this(tableHost, gross, average, hot, new String[]
		{
				"match_season", //"����"
				"team_name", //"�������"
				"game",//"��������"
				"shoot_score",//"Ͷ��������"
				"shoot",//"Ͷ�����ִ���"
				"three_shoot_score",//"����������"
				"three_shoot",//"���ֳ�����"
				"foul_shoot_score",//"����������"
				"foul_shoot",//"���������"
				"attack_board",//"����������"
				"defence_board",//"����������"
				"total_board",//"������"
				"assist",//"������"
				"steal",//"������"
				"cap",//"��ñ��"
				"miss",//"ʧ����"
				"foul",//"������"
				"self_score",//"�����÷�"
				"shoot_rate",//"Ͷ��������"
				"three_shoot_rate",//"����������"
				"foul_shoot_rate",//"����������"
				"win_rate",//"ʤ��"
				"attack_round",//"�����غ�"
				"attack_efficiency",//"����Ч��"
				"defence_efficiency",//"����Ч��"
				"attack_board_efficiency",//"��������Ч��"
				"defence_board_efficiency",//"��������Ч��"
				"steal_efficiency",//"����Ч��"
				"assist_efficiency",//"����Ч��"
				"team_logo"	//"�����־"
		},
		 new String[]
		{
				"match_season", //"����"
				"team_name", //"�������"
				"game",//"��������"
				"shoot_score",//"Ͷ��������"
				"shoot",//"Ͷ�����ִ���"
				"three_shoot_score",//"����������"
				"three_shoot",//"���ֳ�����"
				"foul_shoot_score",//"����������"
				"foul_shoot",//"���������"
				"attack_board",//"����������"
				"defence_board",//"����������"
				"total_board",//"������"
				"assist",//"������"
				"steal",//"������"
				"cap",//"��ñ��"
				"miss",//"ʧ����"
				"foul",//"������"
				"self_score",//"�����÷�"
				"shoot_rate",//"Ͷ��������"
				"three_shoot_rate",//"����������"
				"foul_shoot_rate",//"����������"
				"win_rate",//"ʤ��"
				"attack_round",//"�����غ�"
				"attack_efficiency",//"����Ч��"
				"defence_efficiency",//"����Ч��"
				"attack_board_efficiency",//"��������Ч��"
				"defence_board_efficiency",//"��������Ч��"
				"steal_efficiency",//"����Ч��"
				"assist_efficiency",//"����Ч��"
				"team_logo"	,//"�����־"
				
				"team_name_abbr",//������д 
				"team_location",//�������ڵ� 
				"team_match_area",//���� 
				"team_sector",//���� 
				"team_host",//���� 
				"team_foundation",//����ʱ��(���)	
		});
	}
	
	private TeamServiceAdapter(TableHost tableHost,
			GrossTeam gross, AverageTeam average, HotTeamToday hot, String[] columnNames,String[] oneTeamColumns)
	{
		super(tableHost, gross, average, hot);
		this.columnNames = columnNames;
		this.oneTeamColumns=oneTeamColumns;
	}
	
	@Override
	public String[][] searchForTeams(boolean isGross, int head, boolean isUp)
	{
		if(head < 0) head = 1;	//Team name by default.
		if(head > columnNames.length) return null;
		
		/*
		SortQuery sort;
		if(isGross)
			sort = new SortQuery(this.gross.getTable(), columnNames[head], isUp);
		else sort = new SortQuery(this.average.getTable(), columnNames[head], isUp);
		tableHost.performQuery(sort, "team_query_result");
		Table queryResult = tableHost.getTable("team_query_result");
		*/
		Table queryResult = this.searchForTeams(isGross, new String[]{columnNames[head]}, isUp);
		
		Cursor rows = queryResult.getRows();
		String[][] returnValue = new String[rows.getLength()][columnNames.length];
		Column[] columns = new Column[columnNames.length];
		for(int i = 0; i < columnNames.length; i ++)
			columns[i] = queryResult.getColumn(columnNames[i]);
		
		for(int row = 0; row < rows.getLength(); row ++)
		{
			Row current = rows.next();
			for(int column = 0; column < columns.length; column ++)
			{
				Object value = columns[column].getAttribute(current);
				if(value != null) returnValue[row][column] = value.toString();
			}
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
		
		Cursor rows = queryResult.getRows();
		String[] returnValue=new String[oneTeamColumns.length];
		if(rows.getLength() == 0) return returnValue;
		
		Row zeroth = rows.next();
		Column[] columns = new Column[oneTeamColumns.length];
		for(int i = 0; i < oneTeamColumns.length; i ++)
			columns[i] = queryResult.getColumn(oneTeamColumns[i]);
		for(int column = 0; column < columns.length; column ++)
		{
			Object value = columns[column].getAttribute(zeroth);
			if(value != null) returnValue[column] = value.toString();
		}
		
		return returnValue;
	}
}
