package nbaquery.logic.hot_team_today;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.SumColumnInfo;

public class HotTeamToday {

	public TableHost tableHost;
	protected LogicWatcher base;
	protected Table table, nativeTeam;
	
	public HotTeamToday(TableHost tableHost, HotTeamTodayPerformanceSelect base){
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
		this.nativeTeam = tableHost.getTable("team");
	}
	
	public Table getTable() {	
		
		if(base.checkDepenency())
		{
			GroupQuery groupQuery = new GroupQuery(base.getTable(), new String[]{"team_name_abbr"},
					//new WinColumnInfo(),
					new SumColumnInfo("self_score", "self_score"),
					new SumColumnInfo("total_board", "total_board"),
					new SumColumnInfo("attack_board", "attack_board"),
					new SumColumnInfo("defence_board", "defence_board"),
					new SumColumnInfo("assist", "assist"),
					new SumColumnInfo("cap", "cap"),
					new SumColumnInfo("steal", "steal")
			);
			tableHost.performQuery(groupQuery, "hot_team_performance_today_group");
			table = tableHost.getTable("hot_team_performance_today_group");
			
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(table, nativeTeam,
					new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
			tableHost.performQuery(joinQuery, "hot_team_performance_today");
			table = tableHost.getTable("hot_team_performance_today");
		}
		
		return table;
	}
}
