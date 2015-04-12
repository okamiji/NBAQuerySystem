package nbaquery.logic.player;

import java.lang.reflect.Array;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.NativeTablePipeline;
import nbaquery.logic.average_player.AveragePlayer;
import nbaquery.logic.gross_player.GrossPlayer;
import nbaquery.logic.hot_player_today.HotPlayerToday;

public class PlayerServiceAdapter implements PlayerService
{
	protected GrossPlayer gross;
	protected AveragePlayer average;
	protected HotPlayerToday hot;
	public TableHost tableHost;
	public String[] columnNames; 
	
	public PlayerServiceAdapter(TableHost tableHost, GrossPlayer gross, AveragePlayer average,String[] columnNames)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.average = average;
		this.hot=hot;
		this.columnNames = columnNames;
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
	//待讨论：用Int head还是String head 因为columnNames的问题
	@Override
	public String[][] searchForTodayHotPlayers(int head) {
		if(head < 0) head = 1;
		if(head > columnNames.length) return null;
		Table table;
		SortQuery sort = null;
		table=this.hot.getTable();
		sort = new SortQuery(table, columnNames[head],5,false);
		tableHost.performQuery(sort, "player_query_result");
		Table queryResult = tableHost.getTable("player_query_result");
		Row[] rows = queryResult.getRows();
		int columnNumber=6;//6=姓名+参数*5
		String[][] returnValue = new String[rows.length][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++)
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
		Table player=tableHost.getTable("player");
		try {
			query = new SelectProjectQuery("player.PLAYER_NAME=="+"'"+playerName+"'",player);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tableHost.performQuery(query, "player_query_result");
		Table queryResult = tableHost.getTable("player_query_result");
		Row[] rows = queryResult.getRows();
		Object[] values=rows[0].getAttributes();
		String[] returnValue=new String[values.length];
		for(int i = 0; i < values.length; i ++){
			Object value=values[i];
			if(value != null){
				returnValue[i]=value.toString();
				System.out.println(returnValue[i]);
			}
			}
		return returnValue;
	}
	
	
	
}
