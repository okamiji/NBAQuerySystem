package nbaquery.logic.progress_player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;
import nbaquery.data.query.ExpressionDeriveColumnInfo;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.NativeTablePipeline;

public class ProgressPlayer {
	
	LogicWatcher base, match;
	Table table;
	TableHost tableHost;
	DeriveQuery derive;
	
	public ProgressPlayer(TableHost tableHost,ProgressPlayerGroup base)
	{
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
		this.match = new LogicWatcher(new NativeTablePipeline(tableHost, "match"));
	}
	
	public Table getTable(){
		boolean baseChanged = base.checkDepenency();
		boolean matchChanged = match.checkDepenency();
		if(baseChanged || matchChanged){
			if(derive == null) try
			{
				derive = new DeriveQuery(base.getTable(), new DeriveColumnInfo[]{
					new ExpressionDeriveColumnInfo("self_score_rate", Float.class,
							"1.0F * (progress_player_group.self_score_now - progress_player_group.self_score_before )/ " +
							"progress_player_group.self_score_before ")
					,
					new ExpressionDeriveColumnInfo("total_board_rate", Float.class,
							"1.0F * (progress_player_group.total_board_now- progress_player_group.total_board_before ) / " +
							"progress_player_group.total_board_before")
					,
					new ExpressionDeriveColumnInfo("assist_rate", Float.class,
							"1.0F * (progress_player_group.assist_now - progress_player_group.assist_before ) / " +
							"progress_player_group.assist_before")
					}, "self_score_now", "self_score_before", "total_board_now", "total_board_before", 
					"assist_now","assist_before" ,"player_name","team_name_abbr");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			tableHost.performQuery(derive, "progress_player");
			table = tableHost.getTable("progress_player");
		}
		return table;
	}
}
