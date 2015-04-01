package nbaquery.logic.player;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.gross_player.GrossPlayer;

public class PlayerServiceAdapter implements PlayerService
{
	protected GrossPlayer gross;
	public TableHost tableHost;
	public String[] columnNames; 
	
	public PlayerServiceAdapter(TableHost tableHost, GrossPlayer gross, String[] columnNames)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.columnNames = columnNames;
	}
	
	public void destroy()
	{
		this.gross.destroy();
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
			table = null;
			tableName = "average_player";
		}
		
		if(position != null || league != null) try
		{
			String thePosition = tableName + ".player_position='"+ position + "'";
			String theLeague = tableName + ".team_sector='" + league + "'";
			
			String statement = null;
			if(position != null && league != null) statement = thePosition + " and " + theLeague;
			else if(position != null) statement = thePosition;
			else statement = theLeague;
			
			SelectProjectQuery selectPosition = new SelectProjectQuery(statement, table);
			tableHost.performQuery(selectPosition, "player_query_result");
			table = tableHost.getTable("player_query_result");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		SortQuery sort;
		sort = new SortQuery(table, columnNames[head], 50, isUp);
		
		tableHost.performQuery(sort, "player_query_result");
		Table queryResult = tableHost.getTable("player_query_result");
		Row[] rows = queryResult.getRows();
		String[][] returnValue = new String[rows.length][columnNames.length];
		Column[] columns = new Column[columnNames.length];
		for(int i = 0; i < columnNames.length; i ++)
			columns[i] = queryResult.getColumn(columnNames[i]);
		for(int row = 0; row < rows.length; row ++)
			for(int column = 0; column < columns.length; column ++)
				returnValue[row][column] = columns[column].getAttribute(rows[row]).toString();
		tableHost.deleteTable("player_query_result");
		return returnValue;
	}
}
