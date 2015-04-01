package nbaquery.logic.infrustructure;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;

public class MatchNaturalJoinPerformance
{
	public TableHost tableHost;
	protected NaturalJoinQuery joinQuery; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	public MatchNaturalJoinPerformance(TableHost tableHost)
	{
		this.tableHost = tableHost;
		this.joinQuery = new NaturalJoinQuery(tableHost.getTable("match"), tableHost.getTable("performance"), new String[]{"match_id"}, new String[]{"match_id"});
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			tableHost.performQuery(joinQuery, "match_natural_join_performance");
			table = tableHost.getTable("match_natural_join_performance");
			shouldDoQuery = false;
		}
		return table;
	}
	
	public void markDirty()
	{
		this.shouldDoQuery = true;
	}
	
	public void destroy()
	{
		this.markDirty();
		this.tableHost.deleteTable("match_natural_join_performance");
	}
}
