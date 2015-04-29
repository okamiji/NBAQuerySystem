package nbaquery.logic.player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.average_player.AveragePlayer;
import nbaquery.logic.gross_player.GrossPlayer;
import nbaquery.logic.hot_player_today.HotPlayerToday;
import nbaquery.logic.progress_player.ProgressPlayer;

public class NewPlayerServiceAdapter implements NewPlayerService
{
	protected GrossPlayer gross;
	protected AveragePlayer average;
	protected HotPlayerToday hot;
	protected ProgressPlayer progress;
	protected TableHost tableHost;
	
	public NewPlayerServiceAdapter(TableHost tableHost, GrossPlayer gross, AveragePlayer average,
			HotPlayerToday hot, ProgressPlayer progress)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.average = average;
		this.hot=hot;
		this.progress = progress;
		
		this.gross.getTable();
		this.average.getTable();
		this.hot.getTable();
		this.progress.getTable();
	}
	
	
	@Override
	public Table searchForPlayers(boolean isGross, String head, boolean isUp,
			String position, String league)
	{
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
			
			//Filter top 50 in this case.
			sort = new SortQuery(table, head, 50, isUp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(sort == null) sort = new SortQuery(table, head, isUp);
		
		tableHost.performQuery(sort, "player_query_result");
		return tableHost.getTable("player_query_result");
	}

	@Override
	public Table searchForTodayHotPlayers(String head)
	{
		Table table = this.hot.getTable();
		SortQuery sort = new SortQuery(table, head, 5, true);		//"5" stands for top 5 here.
		tableHost.performQuery(sort, "hot_player_query_result");
		return tableHost.getTable("hot_player_query_result");
	}

	@Override
	public Table searchForProgressPlayers(String head)
	{
		Table table = this.progress.getTable();
		SortQuery sort = new SortQuery(table, head, 5, true);		//"5" stands for top 5 here. 
		
		tableHost.performQuery(sort, "progress_player_query_result");
		return tableHost.getTable("progress_player_query_result");
	}

	@Override
	public Table searchForSeasonHotPlayers(String head)
	{
		return this.searchForPlayers(true, head, false, null, null);
	}

	@Override
	public Table searchForOnePlayerTable(String playerName)
	{
		Table player = tableHost.getTable("player");
		SelectProjectQuery query = null;
		try
		{
			query = new SelectProjectQuery("player.PLAYER_NAME=='%1'".replace("%1", playerName), player);
		}
		catch (Exception e)
		{
			
		}
		
		tableHost.performQuery(query, "one_player_query_result");
		return tableHost.getTable("one_player_query_result");
	}
}
