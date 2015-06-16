package nbaquery.logic.player;

import nbaquery.data.TableHost;
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
}
