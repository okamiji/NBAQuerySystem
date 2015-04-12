package nbaquery.logic.hot_player_today;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.SumColumnInfo;

public class HotPlayerToday {


	public TableHost tableHost;
	protected LogicWatcher base;
	protected Table table;
	
	public HotPlayerToday(TableHost tableHost, HotPlayerTodayPerformanceSelect base){
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
	}
	
	public Table getTable() {
		if(base.checkDepenency())
		{
			GroupQuery groupQuery = new GroupQuery(base.getTable(), new String[]{"player_name"},
					new SumColumnInfo("self_score", "self_score"),
					new SumColumnInfo("total_board", "total_board"),
					new SumColumnInfo("assist", "assist"),
					new SumColumnInfo("cap", "cap"),
					new SumColumnInfo("steal", "steal")
			);
			tableHost.performQuery(groupQuery, "hot_player_performance_today");
			table = tableHost.getTable("hot_player_performance_today");
		}
		return table;
	}
	
	
}
