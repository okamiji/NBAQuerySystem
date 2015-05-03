package nbaquery.logic.hot_player_today;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.infrustructure.MatchNaturalJoinPerformance;


public class HotPlayerTodayPerformanceSelect implements LogicPipeline {

	public TableHost tableHost;
	protected LogicWatcher joinedPerformance;
	protected Table table;
	protected String date;
	protected String season;
	
	protected Table match;
	protected Column match_season;
	protected Column match_date;
	
	public HotPlayerTodayPerformanceSelect(TableHost tableHost, MatchNaturalJoinPerformance joinedPerformance){
		this.tableHost = tableHost;
		this.joinedPerformance = new LogicWatcher(joinedPerformance);
		this.match = this.tableHost.getTable("match");
		this.match_season = this.match.getColumn("match_season");
		this.match_date = this.match.getColumn("match_date");
	}
	
	@Override
	public Table getTable() {
		boolean hasMatchChanged = match.hasTableChanged(this);
		if(joinedPerformance.checkDepenency() || hasMatchChanged)
		{
			if(hasMatchChanged)
			{
				Row[] rows = match.getRows();
				if(rows.length <= 0) return null;
				Row lastMatch = rows[rows.length - 1];
				season = (String) match_season.getAttribute(lastMatch);
				date = (String) match_date.getAttribute(lastMatch);
			}
			
			try
			{
				SelectProjectQuery query = new SelectProjectQuery(
					"match_natural_join_performance.match_date='%1' and match_natural_join_performance.match_season='%2'"
						.replace("%1", date).replace("%2", season), joinedPerformance.getTable());
				tableHost.performQuery(query, "todayPerformance");
			}
			catch (Exception e)
			{
			
			}	
			table = tableHost.getTable("todayPerformance");
		}
		return table;
	}
	
}
