package nbaquery.logic.progress_player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;
import nbaquery.data.query.ExpressionDeriveColumnInfo;
import nbaquery.data.query.Query;
import nbaquery.logic.LogicWatcher;

public class ProgressPlayer {
	
	LogicWatcher base;
	Table table,progressPlayerGroup;
	TableHost tableHost;
	
	public ProgressPlayer(TableHost tableHost,ProgressPlayerGroup base){
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
		this.progressPlayerGroup=base.getTable();
	}
	
	public Table getTable() throws Exception{
		if(base.checkDepenency()){
			DeriveQuery query = new DeriveQuery(base.getTable(), new DeriveColumnInfo[]{
					new ExpressionDeriveColumnInfo("self_score_accuracy", Float.class,
							"1.0F * progress_player_group.self_score_now / " +
							"( progress_player_group.self_score_now - progress_player_group.self_score_before ) ")
					,
					new ExpressionDeriveColumnInfo("total_board_accuracy", Float.class,
							"1.0F * progress_player_group.total_board_now / " +
							"( progress_player_group.total_board_now - progress_player_group.total_board_before ) ")
					,
					new ExpressionDeriveColumnInfo("assist_accuracy", Float.class,
							"1.0F * progress_player_group.assist_now / " +
							"( progress_player_group.assist_now - progress_player_group.assist_before ) ")
					}, "self_score_now", "self_score_before", "total_board_now", "total_board_before", 
					"assist_now","assist_before" ,"player_name");
			
			tableHost.performQuery(query, "progress_player");
			table = tableHost.getTable("progress_player");
		}
		return table;
	}
}
