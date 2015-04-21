package nbaquery.logic.progress_player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;
import nbaquery.data.query.ExpressionDeriveColumnInfo;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.data.query.Query;
import nbaquery.logic.LogicWatcher;

public class ProgressPlayer {
	
	LogicWatcher base;
	Table table,player;
	TableHost tableHost;
	
	public ProgressPlayer(TableHost tableHost,ProgressPlayerGroup base){
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
		this.player=tableHost.getTable("player");
	}
	
	public Table getTable() throws Exception{
		if(base.checkDepenency()){
			DeriveQuery query = new DeriveQuery(base.getTable(), new DeriveColumnInfo[]{
					new ExpressionDeriveColumnInfo("self_score_rate", Float.class,
							"1.0F * progress_player_group.self_score_now / " +
							"( progress_player_group.self_score_now - progress_player_group.self_score_before ) ")
					,
					new ExpressionDeriveColumnInfo("total_board_rate", Float.class,
							"1.0F * progress_player_group.total_board_now / " +
							"( progress_player_group.total_board_now - progress_player_group.total_board_before ) ")
					,
					new ExpressionDeriveColumnInfo("assist_rate", Float.class,
							"1.0F * progress_player_group.assist_now / " +
							"( progress_player_group.assist_now - progress_player_group.assist_before ) ")
					}, "self_score_now", "self_score_before", "total_board_now", "total_board_before", 
					"assist_now","assist_before" ,"player_name","team_name_abbr");
			
			tableHost.performQuery(query, "progress_player");
			Table intermediateTable = tableHost.getTable("progress_player");
			
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(intermediateTable, player, new String[]{"player_name"}, new String[]{"player_name"});
			tableHost.performQuery(joinQuery, "progress_player");
			
			table = tableHost.getTable("progress_player");
		}
		return table;
	}
}
