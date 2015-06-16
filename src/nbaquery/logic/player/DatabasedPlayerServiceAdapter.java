package nbaquery.logic.player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.logic.average_player.AveragePlayer;
import nbaquery.logic.gross_player.GrossPlayer;
import nbaquery.logic.hot_player_today.HotPlayerToday;
import nbaquery.logic.progress_player.ProgressPlayer;

public class DatabasedPlayerServiceAdapter extends NewPlayerServiceAdapter
{
	MutableSqlTable progress_player_group;
	MutableSqlTable derived_player_performance;
	MutableSqlTable average_player;
	MutableSqlTable gross_player;
	
	public DatabasedPlayerServiceAdapter(TableHost tableHost, GrossPlayer gross, AveragePlayer average,
			HotPlayerToday hot, ProgressPlayer progress)
	{
		super(tableHost, gross, average, hot, progress);
		progress_player_group = (MutableSqlTable) tableHost.getTable("progress_player_group");
		derived_player_performance = (MutableSqlTable) tableHost.getTable("derived_player_performance");
		average_player = (MutableSqlTable) tableHost.getTable("average_player_group");
		gross_player = (MutableSqlTable) tableHost.getTable("gross_player_derive");
	}
	
	public boolean shouldRedoQuery(Object host)
	{
		if(super.shouldRedoQuery(host)) return true;
		boolean isChanged = false;
		if(!progress_player_group.getTableLocked()) 
			if(progress_player_group.hasTableChanged(host)) isChanged |= true;
		if(!derived_player_performance.getTableLocked())
			if(derived_player_performance.hasTableChanged(host)) isChanged |= true;
		if(!average_player.getTableLocked())
			if(average_player.hasTableChanged(host)) isChanged |= true;
		if(!gross_player.getTableLocked())
			if(gross_player.hasTableChanged(host)) isChanged |= true;
		return isChanged;
	}
	
	public Table searchForPlayers(boolean isGross, String[] head, boolean[] isUp,
			String position, String league)
	{
		Table table;
		if(isGross) table = this.gross.getTable();
		else table = this.average.getTable();
		
		SortQuery sort = null;
		
		for(int i = head.length - 1; i > 0; i --)
		{
			sort = new SortQuery(table, head[i], isUp[i]);
			tableHost.performQuery(sort, "player_query_result_sort_" + i);
			table = tableHost.getTable("player_query_result_sort_" + i);
		}
		
		if(position != null || league != null) try
		{
			String thePosition = String.format("player_position='%s'", position);
			String theLeague = String.format("team_match_area=\"%s\"", league);
			
			String statement = null;
			if(position != null && league != null) statement = String.format("%s and %s", thePosition, theLeague);
			else if(position != null) statement = thePosition;
			else statement = theLeague;
			
			SelectProjectQuery selectPosition = new SelectProjectQuery(statement, table);
			tableHost.performQuery(selectPosition, "player_query_result_select");
			table = tableHost.getTable("player_query_result_select");
			
			//Filter top 50 in this case.
			sort = new SortQuery(table, head[0], 50, isUp[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(sort == null) sort = new SortQuery(table, head[0], isUp[0]);
		
		tableHost.performQuery(sort, "player_query_result");
		return tableHost.getTable("player_query_result");
	}
}
