package nbaquery.logic.infrustructure;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicPipeline;

public class MatchNaturalJoinPerformance implements LogicPipeline
{
	public TableHost tableHost;
	protected NaturalJoinQuery joinQuery; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	protected Table match;
	protected Table performance;
	
	public MatchNaturalJoinPerformance(TableHost tableHost)
	{
		this.tableHost = tableHost;
		match = tableHost.getTable("match");
		performance = tableHost.getTable("performance");
		this.joinQuery = new NaturalJoinQuery(match, performance, new String[]{"match_id"}, new String[]{"match_id"});
	}
	
	public Table getTable()
	{
		boolean matchChanged = match.hasTableChanged(this);
		boolean performanceChanged = performance.hasTableChanged(this);
		if(matchChanged || performanceChanged)
		{
			tableHost.performQuery(joinQuery, "match_natural_join_performance");
			table = tableHost.getTable("match_natural_join_performance");
		}
		return table;
	}
}
