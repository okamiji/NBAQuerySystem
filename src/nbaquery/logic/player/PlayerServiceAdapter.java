package nbaquery.logic.player;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.logic.average_player.AveragePlayer;
import nbaquery.logic.gross_player.GrossPlayer;
import nbaquery.logic.hot_player_today.HotPlayerToday;
import nbaquery.logic.progress_player.ProgressPlayer;

//XXX 最基本的要求：比较运算符前后必须留1空格，逗号之后必须留一空格，没事不要初始化为null。
public class PlayerServiceAdapter extends NewPlayerServiceAdapter implements PlayerService, NewPlayerService
{
	public String[] columnNames, hotColumnNames, progressColumnNames, playerInfoColumnNames;
	
	public PlayerServiceAdapter(TableHost tableHost, GrossPlayer gross, AveragePlayer average, HotPlayerToday hot, ProgressPlayer progress,
			String[] columnNames, String[] hotColumnNames, String[] progressColumnNames, String[] playerInfoColumnNames)
	{
		
		super(tableHost, gross, average, hot, progress);
		this.hotColumnNames=hotColumnNames;
		this.progressColumnNames=progressColumnNames;
		this.columnNames = columnNames;
		this.playerInfoColumnNames=playerInfoColumnNames;
		
		this.gross.getTable();
		this.average.getTable();
		this.hot.getTable();
		this.progress.getTable();
	}
	
	@Override
	public String[][] searchForPlayers(boolean isGross, int head, boolean isUp, String position, String league)
	{
		if(head < 0) head = 1;
		if(head > columnNames.length) return null;
		
		Table queryResult = this.searchForPlayers(isGross, columnNames[head], isUp, position, league);
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
		
		//tableHost.deleteTable("player_query_result");
		return returnValue;
	}
		
	@Override
	public String[][] searchForTodayHotPlayers(int head) {
		if(head < 0) head = 1;
		if(head > hotColumnNames.length)
			return null;
		
		Table queryResult = this.searchForTodayHotPlayers(hotColumnNames[head]);
		
		Row[] rows = queryResult.getRows();
		int columnNumber=hotColumnNames.length;
		String[][] returnValue = new String[rows.length][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++){
			columns[i] = queryResult.getColumn(hotColumnNames[i]);
		}
		for(int row = 0; row < rows.length; row ++)
			for(int column = 0; column < columns.length; column ++)
			{
				if(columns[column]!=null){
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) returnValue[row][column] = value.toString();
				}
			}
		
		//tableHost.deleteTable("hot_player_query_result");
		return returnValue;
	}
	
	@Override
	public String[][] searchForProgressPlayers(int head) throws Exception {
		if(head < 0) head = 1;
		if(head > progressColumnNames.length)
			return null;
		
		Table queryResult = this.searchForProgressPlayers(progressColumnNames[head]);
		
		Row[] rows = queryResult.getRows();
		int columnNumber=progressColumnNames.length;
		String[][] returnValue = new String[rows.length][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++){
			columns[i] = queryResult.getColumn(progressColumnNames[i]);
		}
		for(int row = 0; row < rows.length; row ++){
			for(int column = 0; column < columns.length; column ++)
			{
				if(columns[column]!=null){
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) {
					returnValue[row][column] = value.toString();
				}
				}
				
			}
		}
		tableHost.deleteTable("progress_player_query_result");
		return returnValue;
	}
	
	public String[][] searchForSeasonHotPlayers(int head){
		String[][] strs=this.searchForPlayers(true, head, false, null, null);
		String[][] result=new String[5][columnNames.length];
		for(int i=0;i<5;i++)
			for(int j=0;j<strs[i].length;j++)
				result[i][j]=strs[i][j];
		return result;
	}
	
	@Override
	public String[] searchForOnePlayer(String playerName)
	{
		Table queryResult = this.searchForOnePlayerTable(playerName);
		Row[] rows = queryResult.getRows();
		int columnNumber=playerInfoColumnNames.length;
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++)
			columns[i] = queryResult.getColumn(playerInfoColumnNames[i]);
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
