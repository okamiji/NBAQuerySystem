package nbaquery.logic.player;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.average_player.AveragePlayer;
import nbaquery.logic.gross_player.GrossPlayer;
import nbaquery.logic.hot_player_today.HotPlayerToday;

//XXX 最基本的要求：比较运算符前后必须留1空格，逗号之后必须留一空格，没事不要初始化为null。
public class PlayerServiceAdapter implements PlayerService
{
	protected GrossPlayer gross;
	protected AveragePlayer average;
	protected HotPlayerToday hot;
	public TableHost tableHost;
	public String[] columnNames,hotColumnNames, progressColumnNames,playerInfoColumnNames;
	
	public PlayerServiceAdapter(TableHost tableHost, GrossPlayer gross, AveragePlayer average,String[] columnNames,
			String[] hotColumnNames,String[] progressColumnNames,String[] playerInfoColumnNames)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.average = average;
		this.hotColumnNames=hotColumnNames;
		this.progressColumnNames=progressColumnNames;
		this.columnNames = columnNames;
		this.playerInfoColumnNames=playerInfoColumnNames;
	}

	@Override
	public String[][] searchForPlayers(boolean isGross, int head, boolean isUp, String position, String league)
	{
		if(head < 0) head = 1;
		if(head > columnNames.length) return null;
		
		Table table;
		String tableName;
		if(isGross)
		{
			table = this.gross.getTable();
			tableName = "gross_player";
		}
		else
		{
			table = this.average.getTable();
			tableName = "average_player";
		}
		
		SortQuery sort = null;
		if(position != null || league != null) try
		{
			String thePosition = tableName + ".player_position='"+ position + "'";
			String theLeague = tableName + ".team_match_area='" + league + "'";
			
			String statement = null;
			if(position != null && league != null) statement = thePosition + " and " + theLeague;
			else if(position != null) statement = thePosition;
			else statement = theLeague;
			
			SelectProjectQuery selectPosition = new SelectProjectQuery(statement, table);
			tableHost.performQuery(selectPosition, "player_query_result");
			table = tableHost.getTable("player_query_result");
			
			sort = new SortQuery(table, columnNames[head], 50, isUp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(sort == null) sort = new SortQuery(table, columnNames[head], isUp);
		
		tableHost.performQuery(sort, "player_query_result");
		Table queryResult = tableHost.getTable("player_query_result");
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
		tableHost.deleteTable("player_query_result");
		return returnValue;
	}
		
	@Override
	public String[][] searchForTodayHotPlayers(int head) {
		if(head < 0) head = 1;
		if(head > hotColumnNames.length) return null;
		Table table;
		SortQuery sort = null;
		table=this.hot.getTable();
		sort = new SortQuery(table, hotColumnNames[head],5,false);
		tableHost.performQuery(sort, "player_query_result");
		Table queryResult = tableHost.getTable("player_query_result");
		Row[] rows = queryResult.getRows();
		int columnNumber=hotColumnNames.length;
		String[][] returnValue = new String[rows.length][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++)
			columns[i] = queryResult.getColumn(hotColumnNames[i]);
		for(int row = 0; row < rows.length; row ++)
			for(int column = 0; column < columns.length; column ++)
			{
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) returnValue[row][column] = value.toString();
			}
		tableHost.deleteTable("player_query_result");
		return returnValue;
	}
	
	@Override
	public String[][] searchForProgressPlayers(int head) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String[][] searchForSeasonHotPlayers(int head){
		String[][] strs=this.searchForPlayers(true, head, false, null, null);
		String[][] result=new String[5][];
		for(int i=0;i<5;i++)
			for(int j=0;j<strs[i].length;j++)
				result[i][j]=strs[i][j];
		return result;
	}

	@Override
	public String[] searchForOnePlayer(String playerName) {		
		SelectProjectQuery query = null;
		Table player = tableHost.getTable("player");
		try {
			query = new SelectProjectQuery("player.PLAYER_NAME=='" + playerName + "'", player);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "player_query_result");
		Table queryResult = tableHost.getTable("player_query_result");
		Row[] rows = queryResult.getRows();
		int columnNumber=playerInfoColumnNames.length;
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++)
			columns[i] = queryResult.getColumn(hotColumnNames[i]);
		if(rows.length == 1)
		{
			String[] returnValue = new String[columnNumber];
			for(int column = 0; column < columns.length; column ++)
			{
				Object value = columns[column].getAttribute(rows[0]);
				if(value != null) returnValue[column] = value.toString();
			}
			
			return returnValue;
		}
		else return null;
	}

}
